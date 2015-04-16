package de.ustu.creta.annotation.webtool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class ListAnnotatedStrings
 */
@Deprecated
public class ListAnnotatedStrings extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONArray obj = new JSONArray();
		int d = -15;
		for (JSONObject object : TempStatic.annotations.get(null).values()) {
			JSONObject rangeObj =
					(JSONObject) ((JSONArray) object.get("ranges")).get(0);
			int start = rangeObj.getInt("startOffset") + d;
			int end = rangeObj.getInt("endOffset") + d;
			obj.put(TempStatic.text.substring(start, end));
		}

		Util.returnJSON(response, obj);
	}

}
