package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
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
		try {
			Project proj =
					CW.getDataLayer(getServletContext()).getProject(projectId);
			Collection<Document> list =
					CW.getDataLayer(getServletContext()).getDocuments(proj);
			JSONObject obj = new JSONObject();
			obj.put("documents", list);
			obj.put("project",
					new JSONObject(CW.getDataLayer(getServletContext())
							.getProject(projectId)));
			obj.put("accesslevel",
					CW.getDataLayer(getServletContext()).getAccessLevel(proj,
							(User) request.getSession().getAttribute(CA.USER)));
			Util.returnJSON(response, obj);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}
