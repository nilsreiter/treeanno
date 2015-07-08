package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.json.JSONException;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class ControllerServlet
 */
public class DocumentContentHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	JCas jcas;

	/**
	 * Default constructor. 
	 * @throws UIMAException 
	 */
	public DocumentContentHandling() throws UIMAException {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dl = CW.getDataLayer(getServletContext());

		String[] documents = request.getParameterValues("documentId");
		if (request.getSession().getAttribute(CA.USER) == null) {
			response.setStatus(Response.SC_FORBIDDEN);
			return;
		}
		try {
			if (documents.length > 0) {
				int docId = Integer.valueOf(documents[0]);
				Document document = dl.getDocument(docId);
				if (document == null) {
					throw new ServletException("Document could not be loaded.");
				}
				int accessLevel =
						dl.getAccessLevel(document.getProject(), (User) request
								.getSession().getAttribute(CA.USER));
				if (accessLevel == Perm.NO_ACCESS) {
					response.setStatus(Response.SC_FORBIDDEN);
					return;
				}
				if (document.isHidden()) {
					response.setStatus(Response.SC_NOT_FOUND);
					return;
				}

				JCas jcas = dl.getJCas(document);
				if (jcas != null) {
					JSONObject obj = new JSONObject();
					obj.put("documentId", docId);
					obj.put("document", new JSONObject(document));
					obj.put("list",
							new JCasConverter()
					.getJSONArrayFromAnnotations(
							jcas,
							de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
					Util.returnJSON(response, obj);
				} else {
					throw new ServletException(
							"JCas could not be loaded (documentId=" + docId
									+ ".");
				}

			}
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		} catch (JSONException e) {
			throw new ServletException(e);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dataLayer = CW.getDataLayer(getServletContext());

		InputStream is = request.getInputStream();
		String s = IOUtils.toString(is);
		JSONObject jObj = new JSONObject(s);
		int docId = jObj.getInt("document");
		Document document = dataLayer.getDocument(docId);
		boolean r = false;
		try {
			JCas jcas =
					Util.addAnnotationsToJCas(dataLayer.getJCas(document), jObj);
			r = dataLayer.updateJCas(document, jcas);
		} catch (UIMAException | JSONException e) {
			e.printStackTrace();
		}
		if (r) {
			Util.returnJSON(response, new JSONObject());
		} else {
			response.sendError(1);
		}
	}
}
