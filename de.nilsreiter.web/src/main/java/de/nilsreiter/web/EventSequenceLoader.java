package de.nilsreiter.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		Document document = getDocument(request);
		if (document != null) documents.add(document);
		response.setCharacterEncoding("UTF-8");

		request.setAttribute("documents", documents);
		request.setAttribute("map", docMan.getClassesForTokens(documents));
		request.setAttribute("arity", documents.size());
		request.setAttribute("docman", docMan);
		RequestDispatcher view =
				request.getRequestDispatcher("document/event-sequence.jsp");
		view.forward(request, response);
	}
}
