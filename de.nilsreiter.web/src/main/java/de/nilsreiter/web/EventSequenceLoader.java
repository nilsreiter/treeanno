package de.nilsreiter.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.web.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class EventSequenceLoader extends AbstractServlet {

	DataReader dataReader;

	public EventSequenceLoader() {
		super();
		dataReader = new DataReader();

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		List<Document> documents = new ArrayList<Document>();
		String docId = "r0003";
		if (request.getParameter("doc") != null) {
			docId = request.getParameter("doc");
			Document document = dataReader.read(docMan.findStreamFor(docId));
			documents = Arrays.asList(document);
		}

		request.setAttribute("location", new Location("Rituals", Area.Document));
		request.setAttribute("documents", documents);
		request.setAttribute("map", docMan.getClassesForTokens(documents));
		request.setAttribute("arity", documents.size());
		request.setAttribute("docman", docMan);
		RequestDispatcher view = request
				.getRequestDispatcher("document/event-sequence.jsp");
		view.forward(request, response);
	}
}
