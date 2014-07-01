package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.web.AbstractServlet;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;
import de.uniheidelberg.cl.reiter.util.Counter;

/**
 * Servlet implementation class GetEventScores
 */
public class GetEventScores extends AbstractServlet {
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
		String docId = "alignment/gold";
		boolean scale = request.getParameter("scale") != null;

		if (request.getParameter("doc") != null) {
			docId = request.getParameter("doc");
		}
		Alignment<Event> alignment = alignmentReader.read(docMan
				.findStreamFor(docId));

		// make or retrieve scores
		Random random = new Random();
		int longest = 0;
		Counter<Event> counter = new Counter<Event>();
		for (Document document : alignment.getDocuments()) {
			for (Event event : document.getEvents()) {
				counter.put(event, random.nextInt(100));
			}
			if (document.getEvents().size() > longest) {
				longest = document.getEvents().size();
			}
		}

		// convert to JSON
		JSONObject json = new JSONObject();
		for (Document document : alignment.getDocuments()) {
			JSONObject serie = new JSONObject();
			serie.put("name", document.getId());
			int currlength = document.getEvents().size();
			int i = 0;
			for (Event event : document.getEvents()) {
				JSONObject point = new JSONObject();
				point.put("y", counter.get(event));
				if (scale)
					point.put("x", i++
							* ((double) longest / (double) (currlength)));
				else
					point.put("x", i++);

				point.put("name", event.getId());
				serie.append("data", point);
			}
			json.append("series", serie);
		}

		response.getWriter().print(json.toString());
	}
}
