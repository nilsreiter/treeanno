package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

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
import de.ustu.ims.reiter.treeanno.VirtualIdProvider;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.tree.PrintDotWalker;
import de.ustu.ims.reiter.treeanno.tree.PrintParenthesesWalker;
import de.ustu.ims.reiter.treeanno.tree.PrintSpanningTableWalker;
import de.ustu.ims.reiter.treeanno.tree.PrintXmlWalker;
import de.ustu.ims.reiter.treeanno.tree.Walker;
import de.ustu.ims.reiter.treeanno.uima.GraphExporter;
import de.ustu.ims.reiter.treeanno.util.CsvGenerator;
import de.ustu.ims.reiter.treeanno.util.Generator;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.PngGenerator;
import de.ustu.ims.reiter.treeanno.util.TxtGenerator;

@Path("")
public class DocumentExport {

	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("{format}/{projectId}/{documentId}")
	public Response doGet(@PathParam("documentId") int docId, @PathParam("format") String format,
			@PathParam("projectId") String projectId) throws Exception {
		DataLayer dataLayer = CW.getDataLayer(context);
		Document document = dataLayer.getDocument(docId);

		StreamingOutput so = new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				ZipOutputStream zos = null;
				zos = new ZipOutputStream(output);
				zos.setLevel(9);
				try {
					if (format.equalsIgnoreCase("XMI")) {
						exportXMI(document, zos);
					}
					if (format.equalsIgnoreCase("PAR")) {
						exportPAR(document, zos);
					}
					if (format.equalsIgnoreCase("XML")) {
						exportXML(document, zos);
					}
					if (format.equalsIgnoreCase("CHART")) {
						exportWithWalker(document, zos, new PrintSpanningTableWalker<TreeSegment>(treeSegmentId), "csv",
								new CsvGenerator<TreeSegment>(treeSegmentId, new Comparator<TreeSegment>() {

									@Override
									public int compare(TreeSegment o1, TreeSegment o2) {
										return Integer.compare(o1.getBegin(), o2.getBegin());
									}
								}));
					}
					if (format.equalsIgnoreCase("PAR_ID")) {
						exportWithWalker(document, zos, new PrintParenthesesWalker<TreeSegment>(treeSegmentId), "par",
								new TxtGenerator("par"));
					}
					if (format.equalsIgnoreCase("DOT")) {
						ConfigurationMap cnf = (ConfigurationMap) context.getAttribute("conf");
						PrintDotWalker pdw = new PrintDotWalker(
								(String) cnf.getOrDefault("treeanno.dot.style.segment", "shape=oval"),
								(String) cnf.getOrDefault("treeanno.dot.style.vsegment", "shape=box"));
						pdw.setLabelFunction(treeSegmentId);
						exportWithWalker(document, zos, pdw, "dot", Arrays.asList(new TxtGenerator("dot"),
								new PngGenerator((String) cnf.get("treeanno.dot.path"))));
					}
				} catch (SAXException | UIMAException e) {
					throw new WebApplicationException(e);
				}
				zos.flush();
				zos.close();

			}
		};

		return Response.ok(so, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-disposition", "attachment; filename=\"export.zip\"").build();

	}

	// TODO: merge into exportWithWalker
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

	protected <T> void exportWithWalker(Document document, ZipOutputStream zos, Walker<TreeSegment, T> walker,
			String suffix, Generator<T> generator) throws UIMAException, SAXException, IOException {
		exportWithWalker(document, zos, walker, suffix, Arrays.asList(generator));
	}

	protected <T> void exportWithWalker(Document document, ZipOutputStream zos, Walker<TreeSegment, T> walker,
			String suffix, List<Generator<T>> generator) throws UIMAException, SAXException, IOException {
		JCas jcas = JCasConverter.getJCas(document.getXmi());
		String name = document.getName();
		if (name == null || name.isEmpty())
			JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentTitle();
		if (name == null || name.isEmpty())
			name = JCasUtil.selectSingle(jcas, DocumentMetaData.class).getDocumentId();

		// root folder
		// zos.putNextEntry(new ZipEntry(name + "/"));

		// original document
		T treeString = GraphExporter.getWalkerResult(jcas, walker);
		for (Generator<T> gen : generator) {
			zos.putNextEntry(new ZipEntry(name + "/" + document.getId() + "." + gen.getSuffix()));
			gen.setInput(treeString);
			if (gen instanceof CsvGenerator) {
				((CsvGenerator<TreeSegment>) gen).setKeys(JCasUtil.select(jcas, TreeSegment.class));
			}
			IOUtils.copy(gen.generate(), zos);
		}

		// annotations folder
		zos.putNextEntry(new ZipEntry(name + "/annotations/"));

		for (UserDocument ud : document.getUserDocuments()) {
			JCas udJcas = JCasConverter.getJCas(ud.getXmi());
			treeString = GraphExporter.getWalkerResult(udJcas, walker);
			for (Generator<T> gen : generator) {
				zos.putNextEntry(new ZipEntry(name + "/annotations/" + ud.getId() + "." + gen.getSuffix()));
				gen.setInput(treeString);
				if (gen instanceof CsvGenerator) {
					((CsvGenerator<TreeSegment>) gen).setKeys(JCasUtil.select(udJcas, TreeSegment.class));
				}

				IOUtils.copy(gen.generate(), zos);
			}
		}
	}

	// TODO: merge into exportWithWalker
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

	// TODO: merge into exportWithWalker
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

	public Function<TreeSegment, String> treeSegmentId = (TreeSegment ts) -> {
		VirtualIdProvider.Scheme scheme = VirtualIdProvider.Scheme.valueOf(
				(String) ((ConfigurationMap) context.getAttribute("conf")).getOrDefault("treeanno.id.scheme", "NONE"));
		return VirtualIdProvider.getVirtualId(scheme, ts);
	};
}
