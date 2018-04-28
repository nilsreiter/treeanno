package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class AccessLevel
 */
@Deprecated
public class AccessLevel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int projectId = Integer.valueOf(request.getParameter("projectId"));

		User user = (User) request.getSession().getAttribute(CA.USER);

		try {
			Project proj = CW.getDataLayer(getServletContext()).getProject(projectId);

			JSONObject json = new JSONObject();
			json.put("ProjectId", projectId);
			json.put("AccessLevel", CW.getDataLayer(getServletContext()).getAccessLevel(proj, user));
			Util.returnJSON(response, json);
		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {

		}
	}

}
