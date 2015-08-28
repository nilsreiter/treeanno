package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.TypeSystem2Xml;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentHandling
 */
public class DocumentHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentHandling() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameterValues("action") == null
				|| request.getParameterValues("action").length != 1) {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		String action = request.getParameterValues("action")[0];
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		String[] docIds = request.getParameterValues("documentId");
		if (action.equalsIgnoreCase("delete")) {
			for (int i = 0; i < docIds.length; i++) {
				try {
					Document document =
							dataLayer.getDocument(Integer.valueOf(docIds[i]));
					dataLayer.deleteDocument(document);
				} catch (NumberFormatException | SQLException e) {
					throw new ServletException(e);
				}
			}
			Util.returnJSON(response, new JSONObject());
		} else if (action.equalsIgnoreCase("clone")) {
			throw new UnsupportedOperationException();
		} else if (action.equalsIgnoreCase("export")) {
			ZipOutputStream zos = null;
			zos = new ZipOutputStream(response.getOutputStream());
			zos.setLevel(9);
			for (int i = 0; i < docIds.length;) {
				int docId = Integer.valueOf(docIds[i]);
				try {
					Document document =
							dataLayer.getDocument(Integer.valueOf(docIds[i]));
					response.setContentType("application/zip");
					response.setHeader("Content-Disposition",
							"attachment; filename=\"file.zip\"");

					JCas jcas = JCasConverter.getJCas(document.getXmi());
					String name = document.getName();
					if (name == null || name.isEmpty())
						JCasUtil.selectSingle(jcas, DocumentMetaData.class)
								.getDocumentTitle();
					if (name == null || name.isEmpty())
						name =
						JCasUtil.selectSingle(jcas,
								DocumentMetaData.class).getDocumentId();

					// root folder
					zos.putNextEntry(new ZipEntry(name + "/"));

					// original document
					zos.putNextEntry(new ZipEntry(name + "/" + docId + ".xmi"));
					XmiCasSerializer.serialize(jcas.getCas(), zos);

					// type system
					zos.putNextEntry(new ZipEntry(name + "/typesystem.xml"));
					TypeSystem2Xml.typeSystem2Xml(jcas.getTypeSystem(), zos);

					// annotations folder
					zos.putNextEntry(new ZipEntry(name + "/annotations/"));

					for (UserDocument ud : document.getUserDocuments()) {
						zos.putNextEntry(new ZipEntry(name + "/annotations/"
								+ ud.getId() + ".xmi"));
						XmiCasSerializer.serialize(
								JCasConverter.getJCas(ud.getXmi()).getCas(),
								zos);
					}
				} catch (SAXException | NumberFormatException | SQLException
						| UIMAException e) {
					throw new ServletException(e);
				} finally {
					if (zos != null) zos.close();
				}
			}
			zos.flush();
			return;

		} else if (action.equalsIgnoreCase("rename")) {
			try {
				String docId = request.getParameterValues("documentId")[0];
				Document document =
						dataLayer.getDocument(Integer.valueOf(docId));
				document.setName(request.getParameter("name"));
				dataLayer.updateDocument(document);
				Util.returnJSON(response, new JSONObject());
			} catch (NullPointerException | NumberFormatException
					| SQLException e) {
				throw new ServletException(e);
			}
		}

	}
}
