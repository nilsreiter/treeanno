package de.ustu.creta.annotation.webtool;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class JSONServlet
 */
public class Util {

	public static void returnJSON(HttpServletResponse response,
			JSONObject object) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(object.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	public static void
			returnJSON(HttpServletResponse response, JSONArray object)
					throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(object.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
}
