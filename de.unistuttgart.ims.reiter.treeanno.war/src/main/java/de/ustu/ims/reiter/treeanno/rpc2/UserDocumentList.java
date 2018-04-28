package de.ustu.ims.reiter.treeanno.rpc2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;

@Path("")
public class UserDocumentList {
	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	@Path("{projectId}/{documentId}")
	public JSONObject doGet(@PathParam("projectId") int projectId, @PathParam("documentId") String docId)
			throws Exception {
		DataLayer dataLayer = CW.getDataLayer(context);
		User user = CW.getUser(request);
		Document document;
		JSONObject json = new JSONObject();
		document = dataLayer.getDocument(Integer.valueOf(docId));
		if (dataLayer.getAccessLevel(document.getProject(), user) >= Perm.PADMIN_ACCESS) {
			json.put("src", JSONUtil.getJSONObject(document));
			for (UserDocument ud : document.getUserDocuments()) {
				json.append("documents", JSONUtil.getJSONObject(ud));
			}
		} else {
			throw new ForbiddenException();
		}
		return json;

	}

}
