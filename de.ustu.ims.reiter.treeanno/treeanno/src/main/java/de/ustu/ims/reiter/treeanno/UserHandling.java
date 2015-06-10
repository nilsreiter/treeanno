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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import de.ustu.ims.reiter.treeanno.beans.User;

/**
 * Servlet implementation class UserHandling
 */
public class UserHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] unames = request.getParameterValues("username");
		String[] pwords = request.getParameterValues("password");
		if (unames.length == 1 && pwords.length == 1) {
			if (check(unames[0], pwords[0])) {
				HttpSession session = request.getSession();
				User user = null;
				try {
					user = getUser(Integer.valueOf(unames[0]));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (user != null) {
					session.setAttribute("user", user);
					response.sendRedirect("projects.jsp");
					return;
				}
			}
		}
		response.sendRedirect("index.jsp");

	}

	protected User getUser(int username) throws SQLException {
		DataSource ds =
				(DataSource) getServletContext().getAttribute("dataSource");
		Connection conn = ds.getConnection();
		PreparedStatement ps =
				conn.prepareStatement("SELECT * FROM treeanno_users WHERE id=?");
		ps.setInt(1, username);
		ResultSet rs = ps.executeQuery();
		User user = null;
		if (rs.next()) {
			user = new User();
			user.setDatabaseId(rs.getInt(1));
			user.setName(rs.getString(2));

			// user.setEmail(rs.getString(3));
		}
		rs.close();
		ps.close();
		conn.close();

		return user;
	}

	protected boolean check(String username, String password) {
		return true;
	}
}
