package de.nilsreiter.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class DocumentLoader extends HttpServlet {

	DataReader dataReader;
	ServletDocumentManager docMan = new ServletDocumentManager();

	public DocumentLoader() {
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

		String docId = "r0003";
		if (request.getParameter("doc") != null)
			docId = request.getParameter("doc");

		Document document = dataReader.read(docMan.findStreamFor(docId));

		Map<Token, String> classesForTokens = new HashMap<Token, String>();
		for (Frame frame : document.getFrames()) {
			if (!classesForTokens.containsKey(frame.firstToken())) {
				classesForTokens.put(frame.firstToken(), "");
			}
			classesForTokens.put(frame.firstToken(),
					classesForTokens.get(frame.firstToken()) + " frame "
							+ frame.getId());
		}
		for (Event frame : document.getEvents()) {
			if (!classesForTokens.containsKey(((HasTokens) frame.getAnchor())
					.firstToken())) {
				classesForTokens.put(
						((HasTokens) frame.getAnchor()).firstToken(), "");
			}
			classesForTokens.put(
					((HasTokens) frame.getAnchor()).firstToken(),
					classesForTokens.get(((HasTokens) frame.getAnchor())
							.firstToken()) + " event " + frame.getId());

		}
		request.setAttribute("document", document);
		request.setAttribute("map", classesForTokens);
		RequestDispatcher view = request.getRequestDispatcher("document.jsp");
		view.forward(request, response);
	}
}
