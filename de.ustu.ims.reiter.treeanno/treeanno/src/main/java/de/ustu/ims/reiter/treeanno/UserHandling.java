package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.ustu.ims.reiter.treeanno.beans.User;

/**
 * Servlet implementation class UserHandling
 */
public class UserHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final String defaultLanguage = "en-US";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
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
				DataLayer dl = CW.getDataLayer(getServletContext());
				try {
					user = dl.getUser(Integer.valueOf(unames[0]));
				} catch (NumberFormatException | SQLException e) {
					this.log(e.getMessage(), e);
					throw new ServletException(e);
				}
				if (user != null) {
					session.setAttribute(CA.USER, user);
					response.sendRedirect("projects.jsp");
					this.log("User " + user.getId() + " logged in.");
					return;
				}
			}
		}
		response.sendRedirect("index.jsp");

	}

	protected boolean check(String username, String password) {
		return true;
	}
}
