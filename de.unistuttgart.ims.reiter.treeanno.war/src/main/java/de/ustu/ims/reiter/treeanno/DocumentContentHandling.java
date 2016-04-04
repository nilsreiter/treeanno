package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

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
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class ControllerServlet
 */
public class DocumentContentHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("documentId") != null) {
			processDocumentId(request, response);
		} else if (request.getParameter("userDocumentId") != null) {
			processUserDocumentId(request, response);
		}

	}

	protected void processUserDocumentId(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dl = CW.getDataLayer(getServletContext());

		String[] documents = request.getParameterValues("userDocumentId");
		if (request.getSession().getAttribute(CA.USER) == null) {
			response.setStatus(Response.SC_FORBIDDEN);
			return;
		}
		try {
			if (documents.length > 0) {
				int docId = Integer.valueOf(documents[0]);
				UserDocument userDocument = dl.getUserDocument(docId);// dl.getDocument(docId);
				if (userDocument == null) {
					throw new ServletException("Document could not be loaded.");
				}
				int accessLevel =
						dl.getAccessLevel(userDocument.getDocument()
								.getProject(), CW.getUser(request));
				if (accessLevel == Perm.NO_ACCESS) {
					response.setStatus(Response.SC_FORBIDDEN);
					return;
				}
				JCas jcas = JCasConverter.getJCas(userDocument.getXmi());
				if (jcas != null) {
					JSONObject obj = new JSONObject();
					obj.put("documentId", docId);
					obj.put("document", JSONUtil.getJSONObject(userDocument));
					obj.put("list",
							new JCasConverter()
					.getJSONArrayFromAnnotations(
							jcas,
							de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
					Util.returnJSON(response, obj);
				} else {
					throw new ServletException("JCas could not be loaded: "
							+ docId);
				}

			}
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		} catch (JSONException e) {
			throw new ServletException(e);
		} catch (SQLException | SAXException | UIMAException e) {
			throw new ServletException(e);
		}
	}

	protected void processDocumentId(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dl = CW.getDataLayer(getServletContext());

		String[] documents = request.getParameterValues("documentId");
		if (request.getSession().getAttribute(CA.USER) == null) {
			response.setStatus(Response.SC_FORBIDDEN);
			return;
		}
		// if the request parameter "master" has been set
		boolean master = (request.getParameter("master") != null);
		try {
			if (documents.length > 0) {
				int docId = Integer.valueOf(documents[0]);
				Document document = dl.getDocument(docId);
				if (document == null) {
					throw new ServletException("Document could not be loaded.");
				}
				int accessLevel =
						dl.getAccessLevel(document.getProject(),
								CW.getUser(request));
				if (accessLevel == Perm.NO_ACCESS) {
					response.setStatus(Response.SC_FORBIDDEN);
					return;
				}

				JCas jcas = null;
				JSONObject obj = new JSONObject();
				if (master && accessLevel >= Perm.PADMIN_ACCESS) {
					Document doc = dl.getDocument(docId);
					jcas = JCasConverter.getJCas(doc.getXmi());
					obj.put("master", true);
				} else {
					UserDocument udoc =
							dl.getUserDocument(CW.getUser(request), document);
					jcas = JCasConverter.getJCas(udoc.getXmi());
				}
				if (jcas != null) {
					obj.put("document", JSONUtil.getJSONObject(document));
					obj.put("list",
							new JCasConverter()
					.getJSONArrayFromAnnotations(
							jcas,
							de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
					Util.returnJSON(response, obj);
				} else {
					throw new ServletException("JCas could not be loaded: "
							+ docId);
				}

			}
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		} catch (JSONException e) {
			throw new ServletException(e);
		} catch (SQLException | SAXException | UIMAException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dataLayer = CW.getDataLayer(getServletContext());

		InputStream is = request.getInputStream();
		String s = IOUtils.toString(is);
		JSONObject jObj = new JSONObject(s);
		JSONObject returnObject = new JSONObject();
		int docId = jObj.getInt("document");
		boolean master = jObj.getBoolean("master");
		boolean r = false;

		try {
			Document doc = dataLayer.getDocument(docId);
			int accessLevel =
					dataLayer.getAccessLevel(doc.getProject(),
							CW.getUser(request));

			if (master && accessLevel >= Perm.PADMIN_ACCESS) {
				// saving the master document
				// TODO: permission level check
				JCas jcas =
						Util.addAnnotationsToJCas(
								JCasConverter.getJCas(doc.getXmi()), jObj);
				doc.setXmi(JCasConverter.getXmi(jcas));
				r = dataLayer.updateDocument(doc);
			} else {
				// saving the user document
				UserDocument document =
						dataLayer.getUserDocument(CW.getUser(request), doc);
				JCas jcas =
						Util.addAnnotationsToJCas(
								JCasConverter.getJCas(document.getXmi()), jObj);
				document.setXmi(JCasConverter.getXmi(jcas));

				r = dataLayer.updateUserDocument(document);
			}
		} catch (UIMAException | JSONException | SQLException | SAXException e) {
			returnObject.put("status", "exception");
			returnObject.put("error", 1);
			returnObject.put("classname", e.getClass().getName());
			returnObject.put("message", e.getMessage());
		}
		if (r) {
			Util.returnJSON(response, new JSONObject("{error:0}"));
		} else {
			Util.returnJSON(response, returnObject);
		}
	}
}
