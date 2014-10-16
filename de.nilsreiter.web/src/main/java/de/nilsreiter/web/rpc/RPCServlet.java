package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.nilsreiter.web.AbstractServlet;

public abstract class RPCServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;

	public void returnJSON(HttpServletResponse response, JSONObject obj)
			throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(obj.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	public void returnJSON(HttpServletResponse response, JSONArray obj)
			throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(obj.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	public void returnHTML(HttpServletResponse response, String htmlString)
			throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(htmlString);
		response.getWriter().flush();
		response.getWriter().close();
	}
}
