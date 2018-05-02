package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.configuration2.ConfigurationMap;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.VirtualIdProvider;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

@Path("c")
public class DocumentContentHandling {
	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	@Path("{projectId}/{documentId}")
	public JSONObject processUserDocumentId(@PathParam("projectId") int projectId,
			@PathParam("documentId") int documentId) throws Exception {
		return getDocument(projectId, documentId, -1);

	}

	@GET
	@Path("{projectId}/{documentId}/{userId}")
	public JSONObject processDocumentId(@PathParam("projectId") int projectId, @PathParam("documentId") int docId,
			@PathParam("userId") int userId) throws Exception {
		return getDocument(projectId, docId, userId);
	}

	protected JSONObject getDocument(int projectId, int documentId, int userId) throws Exception {
		DataLayer dl = CW.getDataLayer(context);
		User user = CW.getUser(request);

		boolean master = userId < 0;
		Document document = dl.getDocument(documentId);
		if (document == null) {
			throw new ServletException("Document could not be loaded.");
		}

		int accessLevel = dl.getAccessLevel(document.getProject(), user);
		if (accessLevel == Perm.NO_ACCESS) {
			throw new ForbiddenException();
		}

		JCas jcas = null;
		JSONObject obj = new JSONObject();
		if (master && accessLevel >= Perm.PADMIN_ACCESS) {
			Document doc = dl.getDocument(documentId);
			jcas = JCasConverter.getJCas(doc.getXmi());
			obj.put("master", true);
		} else {
			UserDocument udoc = dl.getUserDocument(user, document);
			jcas = JCasConverter.getJCas(udoc.getXmi());
		}
		if (jcas != null) {
			obj.put("document", JSONUtil.getJSONObject(document));
			obj.put("list",
					new JCasConverter(
							VirtualIdProvider.Scheme.valueOf((String) ((ConfigurationMap) context.getAttribute("conf"))
									.getOrDefault("treeanno.id.scheme", "NONE"))).getJSONArrayFromAnnotations(jcas,
											de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
			return obj;
		} else {
			throw new ServletException("JCas could not be loaded: " + documentId);
		}
	}

	protected JSONObject putDocument(JSONObject jObj, int projectId, int documentId, int userId) {
		DataLayer dataLayer = CW.getDataLayer(context);

		JSONObject returnObject = new JSONObject();
		boolean r = false;

		try {
			Document doc = dataLayer.getDocument(documentId);
			int accessLevel = dataLayer.getAccessLevel(doc.getProject(), CW.getUser(request));

			if (userId < 0 && accessLevel >= Perm.PADMIN_ACCESS) {
				// saving the master document
				// TODO: permission level check
				JCas jcas = Util.addAnnotationsToJCas(JCasConverter.getJCas(doc.getXmi()), jObj);
				doc.setXmi(JCasConverter.getXmi(jcas));
				r = dataLayer.updateDocument(doc);
			} else {
				// saving the user document
				UserDocument document = dataLayer.getUserDocument(CW.getUser(request), doc);
				JCas jcas = Util.addAnnotationsToJCas(JCasConverter.getJCas(document.getXmi()), jObj);
				document.setXmi(JCasConverter.getXmi(jcas));

				r = dataLayer.updateUserDocument(document);
			}
		} catch (UIMAException | JSONException | SQLException | SAXException | IOException e) {
			returnObject.put("status", "exception");
			returnObject.put("error", 1);
			returnObject.put("classname", e.getClass().getName());
			returnObject.put("message", e.getMessage());
		}
		if (r) {
			return new JSONObject("{error:0}");
		} else {
			return returnObject;
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{projectId}/{documentId}")
	public JSONObject doPost(JSONObject jObj, @PathParam("projectId") int projectId,
			@PathParam("documentId") int documentId) throws Exception {
		return putDocument(jObj, projectId, documentId, -1);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{projectId}/{documentId}/{userId}")
	public JSONObject doPost(JSONObject jObj, @PathParam("projectId") int projectId,
			@PathParam("documentId") int documentId, @PathParam("userId") int userId) throws Exception {
		return putDocument(jObj, projectId, documentId, userId);
	}
}
