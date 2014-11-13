package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.web.beans.AlignmentInfo;

/**
 * Servlet implementation class GetAlignmentInfo
 */
public class GetAlignmentInfo extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();

		for (AlignmentInfo ali : docMan.getAlignmentDocuments()) {
			JSONObject obj = new JSONObject(ali);
			json.append("alignments", obj);
		}

		returnJSON(response, json);
	}
}
