package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration2.ConfigurationMap;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.TypeSystem2Xml;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.tree.PrintDotWalker;
import de.ustu.ims.reiter.treeanno.tree.PrintParenthesesWalker;
import de.ustu.ims.reiter.treeanno.tree.PrintXmlWalker;
import de.ustu.ims.reiter.treeanno.tree.Walker;
import de.ustu.ims.reiter.treeanno.uima.GraphExporter;
import de.ustu.ims.reiter.treeanno.util.Generator;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.PngGenerator;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentExport
 */
public class DocumentExport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		int[] docIds = Util.getAllDocumentIds(request, response);

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(response.getOutputStream());
			zos.setLevel(9);
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=\"export.zip\"");

			for (int i = 0; i < docIds.length;) {
				Document document = dataLayer.getDocument(docIds[i]);
				if (request.getParameter("format") == null
						|| request.getParameterValues("format")[0].equalsIgnoreCase("XMI")) {
					try {
						exportXMI(document, zos);
					} catch (SAXException | UIMAException e) {
						throw new ServletException(e);
					}
				}
				if (request.getParameterValues("format")[0].equalsIgnoreCase("PAR")) {
					exportPAR(document, zos);
				}
				if (request.getParameterValues("format")[0].equalsIgnoreCase("XML")) {
					exportXML(document, zos);
				}
				if (request.getParameterValues("format")[0].equalsIgnoreCase("PAR_ID")) {
					exportWithWalker(document, zos,
							new PrintParenthesesWalker<TreeSegment>(PrintParenthesesWalker.treeSegmentId), "par", null);
				}
				if (request.getParameterValues("format")[0].equalsIgnoreCase("DOT")) {
					ConfigurationMap cnf = (ConfigurationMap) getServletContext().getAttribute("conf");
					exportWithWalker(document, zos,
							new PrintDotWalker((String) cnf.getOrDefault("treeanno.dot.style.segment", "shape=oval"),
									(String) cnf.getOrDefault("treeanno.dot.style.vsegment", "shape=box")),
							"dot", new PngGenerator((String) cnf.get("treeanno.dot.path")));
				}
			}
			zos.flush();
		} catch (SQLException | UIMAException | SAXException e1) {
			throw new ServletException(e1);
		} finally {
			IOUtils.closeQuietly(zos);
		}
		return;
	}

	protected void exportXML(Document document, ZipOutputStream zos) throws UIMAException, SAXException, IOException {
		JCas jcas = JCasConverter.getJCas(document.getXmi());
		String name = document.getName();
		if (name == null || name.isEmpty())
			JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentTitle();
		if (name == null || name.isEmpty())
			name = JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentId();

		// root folder
		// zos.putNextEntry(new ZipEntry(name + "/"));

		Walker<TreeSegment, String> walker = new PrintXmlWalker();

		// original document
		zos.putNextEntry(new ZipEntry(name + "/" + document.getId() + ".xml"));
		String treeString = GraphExporter.getTreeString(jcas, walker);
		zos.write(treeString.getBytes());
		zos.flush();

		// annotations folder
		zos.putNextEntry(new ZipEntry(name + "/annotations/"));

		for (UserDocument ud : document.getUserDocuments()) {
			zos.putNextEntry(new ZipEntry(name + "/annotations/" + ud.getId() + ".xml"));
			treeString = GraphExporter.getTreeString(JCasConverter.getJCas(ud.getXmi()), walker);
			zos.write(treeString.getBytes());
			zos.flush();
		}
	}

	protected void exportWithWalker(Document document, ZipOutputStream zos, Walker<TreeSegment, String> walker,
			String suffix,
			Generator<String> generator) throws UIMAException, SAXException, IOException {
		JCas jcas = JCasConverter.getJCas(document.getXmi());
		String name = document.getName();
		if (name == null || name.isEmpty())
			JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentTitle();
		if (name == null || name.isEmpty())
			name = JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentId();

		// root folder
		// zos.putNextEntry(new ZipEntry(name + "/"));

		// original document
		zos.putNextEntry(new ZipEntry(name + "/" + document.getId() + "." + suffix));
		String treeString = GraphExporter.getTreeString(jcas, walker);
		zos.write(treeString.getBytes());

		// annotations folder
		zos.putNextEntry(new ZipEntry(name + "/annotations/"));

		for (UserDocument ud : document.getUserDocuments()) {
			zos.putNextEntry(new ZipEntry(name + "/annotations/" + ud.getId() + "." + suffix));
			treeString = GraphExporter.getTreeString(JCasConverter.getJCas(ud.getXmi()), walker);
			zos.write(treeString.getBytes());
			if (generator != null) {
				zos.putNextEntry(new ZipEntry(name + "/annotations/" + ud.getId() + "." + generator.getSuffix()));
				generator.setInput(treeString);
				IOUtils.copy(generator.generate(), zos);
			}

		}
	}

	protected void exportPAR(Document document, ZipOutputStream zos) throws UIMAException, SAXException, IOException {
		JCas jcas = JCasConverter.getJCas(document.getXmi());
		String name = document.getName();
		if (name == null || name.isEmpty())
			JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentTitle();
		if (name == null || name.isEmpty())
			name = JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentId();
		Walker<TreeSegment, String> walker = new PrintParenthesesWalker<TreeSegment>();

		// root folder
		// zos.putNextEntry(new ZipEntry(name + "/"));

		// original document
		zos.putNextEntry(new ZipEntry(name + "/" + document.getId() + ".par"));
		String treeString = GraphExporter.getTreeString(jcas, walker);
		zos.write(treeString.getBytes());

		// annotations folder
		zos.putNextEntry(new ZipEntry(name + "/annotations/"));

		for (UserDocument ud : document.getUserDocuments()) {
			zos.putNextEntry(new ZipEntry(name + "/annotations/" + ud.getId() + ".par"));
			treeString = GraphExporter.getTreeString(JCasConverter.getJCas(ud.getXmi()), walker);
			zos.write(treeString.getBytes());

		}
	}

	protected void exportXMI(Document document, ZipOutputStream zos) throws UIMAException, SAXException, IOException {

		JCas jcas = JCasConverter.getJCas(document.getXmi());
		String name = document.getName();
		if (name == null || name.isEmpty())
			JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentTitle();
		if (name == null || name.isEmpty())
			name = JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentId();

		// root folder
		// zos.putNextEntry(new ZipEntry(name + "/"));

		// original document
		zos.putNextEntry(new ZipEntry(name + "/" + document.getId() + ".xmi"));
		XmiCasSerializer.serialize(jcas.getCas(), zos);

		// type system
		zos.putNextEntry(new ZipEntry(name + "/typesystem.xml"));
		TypeSystem2Xml.typeSystem2Xml(jcas.getTypeSystem(), zos);

		// annotations folder
		zos.putNextEntry(new ZipEntry(name + "/annotations/"));

		for (UserDocument ud : document.getUserDocuments()) {
			zos.putNextEntry(new ZipEntry(name + "/annotations/" + ud.getId() + ".xmi"));
			XmiCasSerializer.serialize(JCasConverter.getJCas(ud.getXmi()).getCas(), zos);
		}
	}

}
