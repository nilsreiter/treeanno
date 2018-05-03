package de.ustu.ims.reiter.treeanno.rpc2;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;

@Path("document")
public class DocumentMeta {
	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	@Path("rename")
	public JSONObject rename(@QueryParam("documentId") int docId, @QueryParam("name") String name) throws SQLException {
		DataLayer dataLayer = CW.getDataLayer(context);
		Document document = dataLayer.getDocument(docId);
		document.setName(name);
		dataLayer.updateDocument(document);
		return new JSONObject();
	}

	@GET
	@Path("describe")
	public JSONObject describe(@QueryParam("documentId") int docId, @QueryParam("description") String desc)
			throws SQLException {
		DataLayer dataLayer = CW.getDataLayer(context);
		Document document = dataLayer.getDocument(docId);
		document.setDescription(desc);
		dataLayer.updateDocument(document);
		return new JSONObject();
	}

}
