package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;

/**
 * Servlet implementation class GetEventClasses
 */
public class GetEventClasses extends RPCServlet {
	private static final long serialVersionUID = 1L;

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

		Collection<? extends Document> documents = null;
		try {
			if (request.getParameter("doctype").equals("alignment")) {
				documents =
						docMan.getAlignmentReader()
								.read((request.getParameter("doc")))
						.getDocuments();
			} else if (request.getParameter("doctype").equals("documentset")) {

				documents =
						docMan.getDocumentSetReader()
								.read(request.getParameter("doc")).getSet();
			} else {
				documents =
						Arrays.asList(docMan.getDataReader().read(
								(request.getParameter("doc"))));
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

		Set<String> eventClasses = new HashSet<String>();
		for (Document document : documents) {
			for (Event event : document.getEvents()) {
				eventClasses.add(event.getEventClass());
			}
		}

		JSONObject obj = new JSONObject();
		for (String s : eventClasses) {
			obj.append("eventclasses", s);
		}
		returnJSON(response, obj);
	}

}
