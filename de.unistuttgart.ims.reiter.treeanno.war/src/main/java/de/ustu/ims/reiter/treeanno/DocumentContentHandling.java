package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.DocumentStatus;
import de.ustu.ims.reiter.treeanno.beans.DocumentType;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet("/DocumentContentHandling")
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

	@Deprecated
	protected void processUserDocumentId(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dl = CW.getDataLayer(getServletContext());
		int[] documents = Util.getAllUserDocumentIds(request, response);
		if (request.getSession().getAttribute(CA.USER) == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		try {
			if (documents.length > 0) {
				UserDocument userDocument = dl.getUserDocument(documents[0]);// dl.getDocument(docId);
				if (userDocument == null) {
					throw new ServletException("Document could not be loaded.");
				}
				int accessLevel =
						dl.getAccessLevel(userDocument.getDocument()
								.getProject(), CW.getUser(request));
				if (accessLevel == Perm.NO_ACCESS) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				JCas jcas = JCasConverter.getJCas(userDocument.getXmi());
				if (jcas != null) {
					JSONObject obj = new JSONObject();
					obj.put("documentId", userDocument.getId());
					obj.put("document", JSONUtil.getJSONObject(userDocument));
					obj.put("list",
							new JCasConverter()
					.getJSONArrayFromAnnotations(
							jcas,
							de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
					Util.returnJSON(response, obj);
				} else {
					throw new ServletException("JCas could not be loaded: "
							+ userDocument.getId());
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
		User currentUser = CW.getUser(request);

		int userId = currentUser.getId();

		int targetUserId = userId;
		if (request.getParameter("userId") == null) {} else {
			targetUserId = Util.getFirstUserId(request, response);
		}
		int docId = Util.getFirstDocumentId(request, response);

		try {
			int accessLevel =
					dl.getAccessLevel(dl.getDocument(docId).getProject(),
							CW.getUser(request));
			if (currentUser == null
					|| (userId != targetUserId && accessLevel < Perm.PADMIN_ACCESS)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			User targetUser = dl.getUser(targetUserId);
			// if the request parameter "master" has been set
			DocumentType dType = null;
			if (request.getParameter("type") != null)
				dType = DocumentType.valueOf(request.getParameter("type"));
			Document document = dl.getDocument(docId);
			if (document == null) {
				throw new ServletException("Document could not be loaded.");
			}

			if (accessLevel == Perm.NO_ACCESS) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}

			JCas jcas = null;
			JSONObject obj = new JSONObject();
			if (dType == DocumentType.MASTER
					&& accessLevel >= Perm.PADMIN_ACCESS) {
				Document doc = dl.getDocument(docId);
				jcas = JCasConverter.getJCas(doc.getXmi());
				obj.put("master", true);

			} else {
				UserDocument udoc = dl.createUserDocument(targetUser, document);
				jcas = JCasConverter.getJCas(udoc.getXmi());
			}
			if (jcas != null) {
				obj.put("user", JSONUtil.getJSONObject(targetUser));
				obj.put("document", JSONUtil.getJSONObject(document));
				obj.put("list",
						new JCasConverter()
				.getJSONArrayFromAnnotations(
						jcas,
						de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
				Util.returnJSON(response, obj);
			} else {
				throw new ServletException("JCas could not be loaded: " + docId);
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

		User user = CW.getUser(request);

		int userId;
		if (request.getParameter("userId") == null) {
			userId = user.getId();
		} else {
			userId = Util.getFirstUserId(request, response);
		}

		if (user == null || userId != user.getId()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		InputStream is = request.getInputStream();
		String s = IOUtils.toString(is, "UTF-8");
		JSONObject jObj = new JSONObject(s);
		JSONObject returnObject = new JSONObject();
		int docId = Util.getFirstDocumentId(request, response);
		boolean r = false;

		try {
			Document doc = dataLayer.getDocument(docId);
			int accessLevel =
					dataLayer.getAccessLevel(doc.getProject(),
							CW.getUser(request));

			if (request.getParameter("type") != null && accessLevel >= Perm.PADMIN_ACCESS) {
				JCas jcas;
				DocumentType dType = DocumentType.valueOf(request.getParameter("type"));

				switch (dType) {
				case MERGED_SEG:
					Document newDoc = new Document();
					newDoc.setType(dType);
					newDoc.setName(doc.getName());
					newDoc.setProject(doc.getProject());
					newDoc.setOrigin(doc);
					jcas =
							Util.addAnnotationsToJCas(
									JCasConverter.getJCas(doc.getXmi()), jObj);
					newDoc.setXmi(JCasConverter.getXmi(jcas));
					dataLayer.createNewDocument(newDoc);
					r = dataLayer.updateDocument(newDoc);
					break;
				default:
					jcas =
					Util.addAnnotationsToJCas(
							JCasConverter.getJCas(doc.getXmi()), jObj);
					doc.setXmi(JCasConverter.getXmi(jcas));
					r = dataLayer.updateDocument(doc);
				}
			} else {
				// saving the user document
				UserDocument document =
						dataLayer.createUserDocument(CW.getUser(request), doc);
				JCas jcas =
						Util.addAnnotationsToJCas(
								JCasConverter.getJCas(document.getXmi()), jObj);
				document.setXmi(JCasConverter.getXmi(jcas));
				document.setStatus(DocumentStatus.INPROGRESS);
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
