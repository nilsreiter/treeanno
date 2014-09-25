package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.json.JSONObject;

import de.nilsreiter.event.similarity.EventSimilarityFunction;
import de.nilsreiter.event.similarity.SimilarityProvider;
import de.nilsreiter.event.similarity.impl.SimilarityDatabase_impl;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;
import de.uniheidelberg.cl.a10.data2.io.DBDataReader;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * Servlet implementation class GetEventSimilarities
 */
public class GetEventSimilarities extends RPCServlet {
	private static final long serialVersionUID = 1L;
	DBAlignmentReader<Event> alignmentReader;
	DBDataReader dataReader;
	SimilarityProvider<Event> database;

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			alignmentReader = docMan.getAlignmentReader();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
		dataReader = docMan.getDataReader();

		Object dbAttr = getServletContext().getAttribute("simdatabase");
		if (dbAttr != null)
			database =
			(SimilarityProvider<Event>) getServletContext()
			.getAttribute("simdatabase");

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

		List<Class<? extends EventSimilarityFunction>> similarityTypes =
				docMan.getSupportedFunctions();

		Collection<? extends Document> documents = null;
		try {
			if (request.getParameter("doctype").equals("alignment")) {
				documents =
						alignmentReader.read((request.getParameter("doc")))
						.getDocuments();
			} else if (request.getParameter("doctype").equals("documentset")) {

				documents =
						docMan.getDocumentSetReader()
								.read(request.getParameter("doc")).getSet();
			} else {
				documents =
						Arrays.asList(dataReader.read((request
								.getParameter("doc"))));
			}
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cache cache = CacheManager.getInstance().getCache("main");

		if (cache.isKeyInCache(documents)) {
			returnJSON(response, (JSONObject) cache.get(documents)
					.getObjectValue());
		}

		// Create a map for similarities
		Random random = new Random();
		JSONObject json = new JSONObject();
		for (Document document1 : documents) {
			for (Document document2 : documents) {
				Map<String, Matrix<Event, Event, Double>> sims;
				try {
					sims = database.getSimilarities(document1, document2);
					for (Event token1 : document1.getEvents()) {
						String id1 = token1.firstToken().getGlobalId();
						if (json.optJSONObject(id1) == null)
							json.put(id1, new JSONObject());
						JSONObject tokenObject = json.getJSONObject(id1);
						for (Class<? extends SimilarityFunction<Event>> type : similarityTypes) {
							String typeId = type.getSimpleName();
							String typeShortId =
									typeId.substring(
											0,
											SimilarityDatabase_impl.typeNameMaxLength);
							if (tokenObject.optJSONObject(typeId) == null)
								tokenObject.put(typeId, new JSONObject());
							JSONObject typeObject =
									tokenObject.getJSONObject(typeId);

							for (Event token2 : document2.getEvents()) {
								if (token1 != token2)
									try {
										typeObject.put(
												token2.firstToken()
												.getGlobalId(),
												sims.get(typeShortId).get(
														token1, token2));
									} catch (NullPointerException e) {
										e.printStackTrace();
										// this only for development!!
										typeObject.put(token2.firstToken()
												.getGlobalId(), random
												.nextDouble());
									}
							}
							// tokenObject.put(type.getSimpleName(),
							// typeObject);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		cache.put(new Element(documents, json));
		returnJSON(response, json);

	}
}
