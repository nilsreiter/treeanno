package de.nilsreiter.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.event.similarity.FrameNet;
import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.event.similarity.WordNet;
import de.nilsreiter.util.StringMap;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * Servlet implementation class EventSimilarityLoader
 */
public abstract class AbstractEventSimilarityLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletDocumentManager docMan = new ServletDocumentManager();
	SimilarityDatabase<Event> database;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AbstractEventSimilarityLoader() {
		super();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		database = (SimilarityDatabase<Event>) getServletContext()
				.getAttribute("database");
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
			RequestDispatcher view = request.getRequestDispatcher(this
					.getSelectorJSP());
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

		List<Class<? extends SimilarityFunction<Event>>> similarityTypes = Arrays
				.asList(WordNet.class, FrameNet.class);

		boolean devel = this.getServletContext().getAttribute("devel")
				.equals("true");

		List<Document> documents = this.getDocuments(request);

		// Create a map for actions
		Map<Token, Map<Class<? extends SimilarityFunction<Event>>, String>> actionMap = new HashMap<Token, Map<Class<? extends SimilarityFunction<Event>>, String>>();
		for (Document document1 : documents) {
			for (Event token1 : document1.getEvents()) {
				actionMap
						.put(token1.firstToken(),
								new HashMap<Class<? extends SimilarityFunction<Event>>, String>());
				for (Class<? extends SimilarityFunction<Event>> obj : similarityTypes) {
					actionMap.get(token1.firstToken()).put(
							obj,
							"show_similarities('" + obj.getSimpleName() + "','"
									+ token1.firstToken().getGlobalId() + "')");
				}
			}
		}
		Random random = new Random();
		// Create a map for similarities
		StringMap<Token> similaritiesForToken = new StringMap<Token>();
		for (Document document1 : documents) {
			for (Event token1 : document1.getEvents()) {
				for (Document document2 : documents) {

					for (Event token2 : document2.getEvents()) {
						for (Class<? extends SimilarityFunction<Event>> type : similarityTypes) {
							try {
								double similarity;
								if (!devel) {
									similarity = database.getSimilarity(type,
											token1, token2);
								} else {
									similarity = random.nextDouble();
								}
								StringBuilder b = new StringBuilder();

								if (token1 != token2) {
									b.append(token2.firstToken().getGlobalId())
											.append(':');
									b.append(similarity).append(':');
									b.append(type.getSimpleName()).append(' ');
									similaritiesForToken.append(
											token1.firstToken(), b.toString());
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}
			}
		}
		request.setAttribute("similarities", similaritiesForToken);
		request.setAttribute("map", docMan.getClassesForTokens(documents));
		request.setAttribute("arity", documents.size());
		request.setAttribute("documents", documents);
		request.setAttribute("action", actionMap);
		request.setAttribute("similarityTypes", similarityTypes);
		request.setAttribute("location", getLocation());

		RequestDispatcher view = request.getRequestDispatcher(this
				.getViewerJSP());
		view.forward(request, response);
	}

	protected abstract Location getLocation();
}
