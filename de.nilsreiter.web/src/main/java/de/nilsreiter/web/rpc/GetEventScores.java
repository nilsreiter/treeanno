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

import de.nilsreiter.web.AbstractServlet;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;
import de.uniheidelberg.cl.reiter.util.Counter;

/**
 * Servlet implementation class GetEventScores
 */
public class GetEventScores extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	DBAlignmentReader<Event> alignmentReader;

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
		if (request.getParameter("doc") == null) {
			response.getWriter().print("");
			return;

		}
		boolean scale = request.getParameter("scale") != null;

		Alignment<Event> alignment;
		try {
			alignment = alignmentReader.read((request.getParameter("doc")));

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
}
