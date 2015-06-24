package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class ProjectList
 */
public class ProjectList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<Project> list =
		(List<Project>) getServletContext()
						.getAttribute(CA.PROJECTLIST);
		Util.returnJSON(response, new JSONArray(list));
	}
}
