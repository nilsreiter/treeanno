package de.ustu.creta.annotation.webtool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/**
 * Servlet implementation class AnnotatorJSRead
 */
public class AnnotatorJSRead extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected String getId(HttpServletRequest request) {
		String[] path = request.getPathInfo().split("/");
		return path[path.length - 1];
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String s = request.getPathInfo().substring(1);

		if (TempStatic.annotations.containsKey(s)) {
			JSONObject obj = TempStatic.annotations.get(s);
			Util.returnJSON(response, obj);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String s =
				IOUtils.toString(request.getInputStream(),
						request.getCharacterEncoding());
		if (request.getContentType().contains("application/json")) {
			JSONObject newAnno = new JSONObject(s);
			String[] path = request.getPathInfo().split("/");
			String id = path[path.length - 1];
			JSONObject oldAnno = TempStatic.annotations.get(id);
			for (Object okey : newAnno.keySet()) {
				String key = (String) okey;
				oldAnno.put(key, newAnno.get(key));
			}

			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
			response.setHeader("Location", id);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = this.getId(request);
		TempStatic.annotations.remove(id);
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		response.setContentLength(0);
	}
}
