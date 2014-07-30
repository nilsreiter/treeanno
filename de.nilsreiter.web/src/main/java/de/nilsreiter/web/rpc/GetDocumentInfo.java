package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.nilsreiter.web.beans.DocumentInfo;

/**
 * Servlet implementation class GetDocumentInfo
 */
public class GetDocumentInfo extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONArray jsarr = new JSONArray();

		for (DocumentInfo di : docMan.getDocumentInfo()) {
			jsarr.put(new JSONObject(di));
		}

		returnJSON(response, jsarr);

	}
}
