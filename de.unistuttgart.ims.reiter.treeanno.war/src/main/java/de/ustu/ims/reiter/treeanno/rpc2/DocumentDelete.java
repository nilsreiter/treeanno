package de.ustu.ims.reiter.treeanno.rpc2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;

@Path("c")
public class DocumentDelete {

	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@DELETE
	@Path("{projectId}/{documentId}")
	public Response doDelete(@PathParam("projectId") int projectId, @PathParam("documentId") int documentId)
			throws Exception {
		context.log(request.toString());
		DataLayer dataLayer = CW.getDataLayer(context);
		Document document = dataLayer.getDocument(documentId);
		dataLayer.deleteDocument(document);
		return Response.ok().build();
	}

	@DELETE
	@Path("{projectId}/{documentId}/{userDocumentId}")
	public Response doDelete(@PathParam("projectId") int projectId, @PathParam("documentId") int documentId,
			@PathParam("userDocumentId") int userDocumentId) throws Exception {
		DataLayer dataLayer = CW.getDataLayer(context);
		dataLayer.deleteUserDocument(Integer.valueOf(userDocumentId));
		return Response.ok().build();
	}

}
