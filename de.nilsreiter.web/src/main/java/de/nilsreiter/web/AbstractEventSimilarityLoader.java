package de.nilsreiter.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.event.similarity.EventSimilarityFunction;
import de.nilsreiter.event.similarity.SimilarityProvider;
import de.nilsreiter.web.beans.menu.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.io.DBDataReader;

/**
 * Servlet implementation class EventSimilarityLoader
 */
public abstract class AbstractEventSimilarityLoader extends AbstractServlet {
	private static final long serialVersionUID = 1L;
	SimilarityProvider<Event> database;
	DBDataReader dataReader;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AbstractEventSimilarityLoader() {
		super();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		super.init();
		database =
				(SimilarityProvider<Event>) getServletContext().getAttribute(
						"simdatabase");
		dataReader = docMan.getDataReader();
	}

	public abstract String getSelectorJSP();

	public abstract String getViewerJSP();

	public abstract List<Document> getDocuments(HttpServletRequest request)
			throws IOException;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("doc") == null) {
			RequestDispatcher view =
					request.getRequestDispatcher(this.getSelectorJSP());
			view.forward(request, response);
			return;
		}

		if (database == null) {
			RequestDispatcher view = request.getRequestDispatcher("error.jsp");
			request.setAttribute("message", "No connection to database");
			request.setAttribute("title", "Error");
			view.forward(request, response);
			return;
		}

		List<Class<? extends EventSimilarityFunction>> similarityTypes =
				docMan.getSupportedFunctions();

		List<Document> documents = this.getDocuments(request);

		request.setAttribute("map", docMan.getClassesForTokens(documents));
		request.setAttribute("arity", documents.size());
		request.setAttribute("documents", documents);
		request.setAttribute("similarityTypes", similarityTypes);
		request.setAttribute("doc", request.getParameter("doc"));

		RequestDispatcher view =
				request.getRequestDispatcher(this.getViewerJSP());
		view.forward(request, response);
	}

	protected abstract Area getArea();
}
