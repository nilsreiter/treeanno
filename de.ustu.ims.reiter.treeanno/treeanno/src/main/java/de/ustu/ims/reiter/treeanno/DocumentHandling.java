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
		DocumentIndex di =
				((DocumentIndex) this.getServletContext().getAttribute(
						"documentIndex"));
		if (action.equalsIgnoreCase("delete")) {
			String[] docIds = request.getParameterValues("documentId");
			for (int i = 0; i < docIds.length; i++) {
				try {
					di.getDatabaseIO().deleteDocument(
							Integer.valueOf(docIds[i]));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
			}
			Util.returnJSON(response, new JSONObject());
		} else if (action.equalsIgnoreCase("clone")) {
			String[] docIds = request.getParameterValues("documentId");
			for (int i = 0; i < docIds.length; i++) {
				try {
					di.cloneDocument(Integer.valueOf(docIds[i]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Util.returnJSON(response, new JSONObject());
		} else if (action.equalsIgnoreCase("export")) {
			String[] docIds = request.getParameterValues("documentId");
			for (int i = 0; i < docIds.length; i++) {
				int docId = Integer.valueOf(docIds[i]);

				try {
					response.setContentType("application/zip");
					response.setHeader("Content-Disposition",
							"attachment; filename=\"file.zip\"");

					JCas jcas = di.getDocument(docId);
					Document doc = di.getDatabaseIO().getDocument(docId);
					String name = doc.getName();
					if (name == null || name.isEmpty())
						JCasUtil.selectSingle(jcas, DocumentMetaData.class)
								.getDocumentTitle();
					if (name == null || name.isEmpty())
						name =
								JCasUtil.selectSingle(jcas,
										DocumentMetaData.class).getDocumentId();
					ZipOutputStream zos =
							new ZipOutputStream(response.getOutputStream());
					zos.putNextEntry(new ZipEntry(name + "/"));
					ZipEntry ze = new ZipEntry(name + "/" + docId + ".xmi");
					zos.putNextEntry(ze);
					XmiCasSerializer.serialize(jcas.getCas(), zos);
					zos.putNextEntry(new ZipEntry(name + "/typesystem.xml"));
					TypeSystem2Xml.typeSystem2Xml(jcas.getTypeSystem(), zos);
					zos.flush();
					zos.close();
					return;
				} catch (UIMAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
