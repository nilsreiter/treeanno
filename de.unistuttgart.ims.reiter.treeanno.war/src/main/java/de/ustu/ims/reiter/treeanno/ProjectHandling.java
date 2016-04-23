package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import de.ustu.ims.reiter.treeanno.beans.Project;

/**
 * Servlet implementation class ProjectHandling
 */
public class ProjectHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	@Deprecated
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int projectId =
				Integer.valueOf(request.getParameterValues("projectId")[0]);
		try {
			request.getSession().setAttribute("project",
					CW.getDataLayer(getServletContext()).getProject(projectId));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		response.sendRedirect("main.jsp");
		return;
	}

	@Deprecated
	protected Project getProject(int projectId) throws SQLException {
		DataSource ds =
				(DataSource) getServletContext().getAttribute("dataSource");
		Connection conn = ds.getConnection();
		PreparedStatement ps =
				conn.prepareStatement("SELECT * FROM projects WHERE id=?");
		ps.setInt(1, projectId);
		ResultSet rs = ps.executeQuery();
		Project proj = null;
		if (rs.next()) {
			proj = new Project();
			proj.setDatabaseId(rs.getInt(1));
			proj.setName(rs.getString(2));
		}
		rs.close();
		ps.close();
		conn.close();

		return proj;
	}

}
