package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.web.beans.menu.Location;
import de.nilsreiter.web.beans.menu.Location.Area;

/**
 * Servlet implementation class CloseObject
 */
public class CloseObject extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String docId = request.getParameter("doc");
		String areaString = request.getParameter("area");

		if (docId != null && areaString != null) {
			Area area = Area.valueOf(areaString);
			Location loc = getLocation(area, request);
			if (loc.getOpenObjects(area).containsKey(docId)) {
				loc.getOpenObjects(area).remove(docId);

				JSONObject json = new JSONObject();
				json.append("closed", docId);

				returnJSON(response, json);
			} else {

			}
		}

	}
}
