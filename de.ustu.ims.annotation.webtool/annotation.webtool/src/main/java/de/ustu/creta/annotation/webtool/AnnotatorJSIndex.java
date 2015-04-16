package de.ustu.creta.annotation.webtool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class AnnotatorJSIndex
 */
public class AnnotatorJSIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnnotatorJSIndex() {
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
		JSONArray obj = new JSONArray();
		for (JSONObject obj2 : TempStatic.annotations.values()) {
			obj.put(obj2);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		response.getWriter().print(obj.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String s = IOUtils.toString(request.getInputStream(), "UTF-8");
		JSONObject obj = new JSONObject(s);
		String id = "anno" + TempStatic.index++;
		obj.put("id", id);
		TempStatic.annotations.put(id, obj);

		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		response.setHeader("Location", "annotations/" + id);
		response.getWriter().flush();
		response.getWriter().close();
	}
}
