package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentList
 */
public class DocumentList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * TODO: Add permission check
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int projectId =
				Integer.valueOf(request.getParameterValues("projectId")[0]);
		Collection<Document> list =
				CW.getDataLayer(getServletContext()).getDocuments(projectId);
		JSONObject obj = new JSONObject();
		obj.put("documents", list);
		obj.put("project", new JSONObject(CW.getDataLayer(getServletContext())
				.getProject(projectId)));
		Util.returnJSON(response, obj);
	}
}
