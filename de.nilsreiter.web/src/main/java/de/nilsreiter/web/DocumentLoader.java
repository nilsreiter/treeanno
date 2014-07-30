package de.nilsreiter.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.web.beans.menu.Location;
import de.nilsreiter.web.beans.menu.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DBDataReader;

public class DocumentLoader extends AbstractServlet {

	DBDataReader dataReader;

	public DocumentLoader() {
		super();

	}

	@Override
	public void init() throws ServletException {
		super.init();
		dataReader = docMan.getDataReader();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		Location location = getLocation(Area.Document, request);
		location.setArea(Area.Document);
		List<Document> documents = new ArrayList<Document>();

		Document document = getDocument(request);

		location.setCurrentObject(Area.Document, document.getId());
		if (document != null) documents = Arrays.asList(document);

		request.setAttribute("documents", documents);
		request.setAttribute("map", docMan.getClassesForTokens(documents));
		request.setAttribute("arity", documents.size());
		request.setAttribute("docman", docMan);
		RequestDispatcher view =
				request.getRequestDispatcher("document/document.jsp");
		view.forward(request, response);
	}
}
