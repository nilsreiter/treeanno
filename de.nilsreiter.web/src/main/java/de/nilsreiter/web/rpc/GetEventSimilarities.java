package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.event.similarity.FrameNet;
import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.event.similarity.WordNet;
import de.nilsreiter.event.similarity.impl.SimilarityDatabase_impl;
import de.nilsreiter.web.AbstractServlet;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * Servlet implementation class GetEventSimilarities
 */
public class GetEventSimilarities extends AbstractServlet {
	private static final long serialVersionUID = 1L;
	EventAlignmentReader alignmentReader;
	DataReader dataReader;
	SimilarityDatabase<Event> database;

	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		super.init();
		alignmentReader = new EventAlignmentReader(docMan);
		dataReader = new DataReader();

		Object dbAttr = getServletContext().getAttribute("database");
		if (dbAttr != null)
			database = (SimilarityDatabase<Event>) getServletContext()
					.getAttribute("database");

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("doc") == null
				|| request.getParameter("doctype") == null) {
			response.getWriter().print("");
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

		Collection<? extends Document> documents;
		if (request.getParameter("doctype").equals("alignment")) {
			documents = alignmentReader.read(
					docMan.findStreamFor(request.getParameter("doc")))
					.getDocuments();
		} else {
			documents = Arrays.asList(dataReader.read(docMan
					.findStreamFor(request.getParameter("doc"))));
		}

		// Create a map for similarities

		JSONObject json = new JSONObject();
		for (Document document1 : documents) {
			for (Event token1 : document1.getEvents()) {
				JSONObject tokenObject = new JSONObject();
				for (Class<? extends SimilarityFunction<Event>> type : similarityTypes) {
					// tokenObject.put("id", token1.getGlobalId());
					JSONObject typeObject = new JSONObject();
					for (Document document2 : documents) {
						for (Event token2 : document2.getEvents()) {
							if (token1 != token2)
								try {
									double similarity = database.getSimilarity(
												type, token1, token2);
									typeObject.put(token2.firstToken()
											.getGlobalId(), similarity);
								} catch (SQLException e) {

								}
						}
					}
					tokenObject.put(type.getSimpleName(), typeObject);

				}
				json.put(token1.firstToken().getGlobalId(), tokenObject);
			}
		}
		response.setContentType("application/json");
		response.getWriter().print(json.toString());
		response.getWriter().flush();
		response.getWriter().close();

	}
}
