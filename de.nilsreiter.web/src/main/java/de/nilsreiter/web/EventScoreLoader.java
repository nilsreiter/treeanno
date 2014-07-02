package de.nilsreiter.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;

/**
 * Servlet implementation class EventScoreLoader
 */
public class EventScoreLoader extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	EventAlignmentReader alignmentReader;

	@Override
	public void init() {
		super.init();
		alignmentReader = new EventAlignmentReader(docMan);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("doc") == null) {
			request.setAttribute("target", "view-event-scores");
			RequestDispatcher view = request
					.getRequestDispatcher("documentset/select.jsp");
			view.forward(request, response);
			return;
		}

		Alignment<Event> alignment = alignmentReader.read(docMan
				.findStreamFor(request.getParameter("doc")));
		StringBuilder alignmentIds = new StringBuilder();
		for (Link<Event> link : alignment.getAlignments()) {
			alignmentIds.append(link.getId());
			alignmentIds.append(' ');
		}
		Random random = new Random();
		Map<Event, Double> scoreMap = new HashMap<Event, Double>();
		for (Document document : alignment.getDocuments()) {
			for (Event event : document.getEvents()) {
				scoreMap.put(event, random.nextDouble());
			}
		}

		// Create graph object and events map
		StringBuilder eventsMapString = new StringBuilder();
		eventsMapString.append("var events = new Object();");

		StringBuilder graphString = new StringBuilder();
		graphString.append("var graph = new Rickshaw.Graph({\n");
		graphString.append("element: document.querySelector(\"#chart\"),\n");
		graphString.append("\trenderer: 'line',\n");
		// b.append("\twidth:500,\n");
		graphString.append("\tseries: [");
		for (Document document : alignment.getDocuments()) {
			graphString.append("{");
			graphString.append("\t\tdata: [ ");
			for (int i = 0; i < document.getEvents().size(); i++) {
				Event event = document.getEvents().get(i);

				graphString.append("{");
				graphString.append("x: ").append(i).append(",");
				graphString.append("y: ").append(scoreMap.get(event))
						.append(",");
				graphString.append("id:'").append(event.getGlobalId())
						.append("'");
				graphString.append("},");

				eventsMapString.append("events[").append(event.getGlobalId())
						.append("]");
			}
			graphString.append(" ],\n");
			graphString.append("\t\tname:'").append(document.getId())
					.append("',");
			graphString.append("\t\tcolor: palette.color()\n");
			graphString.append("},");
		}
		graphString.append("]");
		graphString.append("});");
		request.setAttribute("graph", graphString.toString());

		request.setAttribute("doc", alignment.getId());
		request.setAttribute("alignment", alignment);
		request.setAttribute("documents", alignment.getDocuments());

		RequestDispatcher view = request
				.getRequestDispatcher("documentset/event-scores.jsp");
		view.forward(request, response);
	}
}
