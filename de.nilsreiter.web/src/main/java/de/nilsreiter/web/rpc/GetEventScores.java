package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.alignment.rwalk.NRWalk;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.graph.AlignmentGraphFactory;
import de.uniheidelberg.cl.a10.data2.alignment.graph.Edge;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;
import de.uniheidelberg.cl.reiter.util.Counter;
import edu.uci.ics.jung.graph.UndirectedGraph;

/**
 * Servlet implementation class GetEventScores
 */
public class GetEventScores extends RPCServlet {
	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(getClass());

	DBAlignmentReader<Event> alignmentReader;

	int k = 1, n = 10;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			alignmentReader = docMan.getAlignmentReader();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// getLocation(request).setArea(Area.Alignment);
		if (request.getParameter("doc") == null) {
			response.getWriter().print("");
			return;

		}
		if (request.getParameter("k") == null) {
			response.getWriter().print("");
			return;
		}
		if (request.getParameter("n") == null) {
			response.getWriter().print("");
			return;
		}

		boolean scale = request.getParameter("scale") != null;

		k = Integer.parseInt(request.getParameter("k"));
		n = Integer.parseInt(request.getParameter("n"));

		Alignment<Event> alignment;
		try {
			alignment = alignmentReader.read((request.getParameter("doc")));

			Counter<Event> connectivityScores;

			// find longer sequence
			int longest = 0;
			for (Document document : alignment.getDocuments()) {
				if (document.getEvents().size() > longest) {
					longest = document.getEvents().size();
				}
			}

			connectivityScores = makeWalkScores(alignment);

			// make or retrieve scores
			// makeRandomScores(connectivityScores, alignment);

			// convert to JSON
			JSONObject json = new JSONObject();
			for (Document document : alignment.getDocuments()) {
				JSONObject serie = new JSONObject();
				serie.put("name", document.getId());
				int currlength = document.getEvents().size();
				int i = 0;
				for (Event event : document.getEvents()) {
					JSONObject point = new JSONObject();
					point.put("y", connectivityScores.get(event));
					if (scale)
						point.put("x", i++ * ((double) longest / (currlength)));
					else
						point.put("x", i++);

					point.put("name", event.getId());
					serie.append("data", point);
				}
				json.append("series", serie);
			}
			returnJSON(response, json);
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

	}

	protected Counter<Event> makeWalkScores(Alignment<Event> alignment) {
		logger.debug("Creating graph for alignment document {}.",
				alignment.getId());
		AlignmentGraphFactory<Event> agf = new AlignmentGraphFactory<Event>();
		UndirectedGraph<Event, Edge<Event>> graph =
				agf.getUndirectedGraph(alignment, alignment.getDocument(0)
						.getEvents(), alignment.getDocument(1).getEvents());
		logger.info("Initialising random walk algorithm with k={} and n={}.",
				k, n);
		NRWalk<Event> nrw = new NRWalk<Event>(k, n);
		return nrw.doWalk(graph);
	}

	protected Counter<Event> makeRandomScores(Alignment<Event> alignment) {
		Random random = new Random();
		int longest = 0;
		Counter<Event> connectivityScores = new Counter<Event>();
		for (Document document : alignment.getDocuments()) {
			for (Event event : document.getEvents()) {
				connectivityScores.put(event, random.nextInt(100));
			}
			if (document.getEvents().size() > longest) {
				longest = document.getEvents().size();
			}
		}
		return connectivityScores;
	}
}
