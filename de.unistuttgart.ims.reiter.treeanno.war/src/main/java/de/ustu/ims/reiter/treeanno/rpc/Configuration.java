package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration2.ConfigurationMap;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.ProjectType;

/**
 * Servlet implementation class Configuration
 */
public class Configuration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ConfigurationMap cnf =
				(ConfigurationMap) getServletContext().getAttribute("conf");

		JSONObject o = new JSONObject(cnf);
		StringBuilder b = new StringBuilder();
		b.append("var configuration = ");
		b.append(o.toString());
		b.append(";\n");

		b.append("var Perm = ");
		b.append(new JSONObject(new Perm()).toString());
		b.append(";\n");

		b.append("var ProjectType = ");
		b.append(new JSONObject(new ProjectType()).toString());
		b.append(";\n");

		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(b.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
}
