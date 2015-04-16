package de.ustu.creta.annotation.webtool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class AnnotatorJSRead
 */
public class AnnotatorJSRead extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnnotatorJSRead() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject obj = TempStatic.annotations.values().iterator().next();
		response.setContentType("text/json");
		response.getWriter().print(obj.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

}
