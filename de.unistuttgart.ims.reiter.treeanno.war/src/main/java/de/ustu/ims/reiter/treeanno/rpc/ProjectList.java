package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class ProjectList
 */
@WebServlet("/rpc/projects")
public class ProjectList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Collection<Project> list =
					CW.getDataLayer(getServletContext()).getProjects();
			User user = (User) request.getSession().getAttribute(CA.USER);

			JSONArray array = new JSONArray();
			for (Project project : list) {
				if (CW.getDataLayer(getServletContext()).getAccessLevel(
						project, user) >= Perm.READ_ACCESS) {
					array.put(JSONUtil.getJSONObject(project));
				}
			}
			this.log(user.getId() + " requests project list.");
			Util.returnJSON(response, array);
		} catch (SQLException e) {
			this.log(e.getMessage(), e);
			throw new ServletException(e);
		}
	}
}
