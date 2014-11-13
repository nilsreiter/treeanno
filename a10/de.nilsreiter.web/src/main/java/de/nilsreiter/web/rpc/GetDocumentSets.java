package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.nilsreiter.web.beans.DocumentSetInfo;

/**
 * Servlet implementation class GetDocumentSets
 */
public class GetDocumentSets extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<DocumentSetInfo> list = docMan.getDocumentSetInfo();

		JSONArray jsarr = new JSONArray();
		for (DocumentSetInfo dsi : list) {
			jsarr.put(new JSONObject(dsi));
		}
		this.returnJSON(response, jsarr);
	}

}
