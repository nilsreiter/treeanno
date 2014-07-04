package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.nilsreiter.web.AbstractServlet;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

/**
 * Servlet implementation class GetEvents
 */
public class GetEvents extends AbstractServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("doc") == null) {
			response.getWriter().print("");
			return;

		}
		Document document = new DataReader().read(docMan.findStreamFor(request
				.getParameter("doc")));

		PrintWriter out = response.getWriter();
		JSONArray json = new JSONArray();
		for (Event event : document.getEvents()) {
			JSONObject eventObject = new JSONObject();
			eventObject.put("id", event.getGlobalId());
			eventObject.put("class", event.getEventClass());
			eventObject.put("anchorId", event.firstToken().getGlobalId());
			for (String role : event.getArguments().keySet()) {
				JSONObject roleObject = new JSONObject();
				roleObject.put("name", event.getArguments().get(role));
			}
		}
		out.print(json.toString());
		out.flush();
		out.close();
	}

}
