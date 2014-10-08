package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;

public class GetAlignments extends RPCServlet {
	DBAlignmentReader<Event> alignmentReader;

	private static final long serialVersionUID = 1L;

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

		Alignment<Event> alignment;

		try {
			alignment = alignmentReader.read(request.getParameter("doc"));
			JSONObject json = new JSONObject();
			for (Link<Event> link : alignment.getAlignments()) {
				for (Event event : link.getElements()) {
					JSONObject ev = new JSONObject();
					ev.put("d", event.getRitualDocument().getId());
					ev.put("t", event.firstToken().getId());
					json.append(link.getId(), ev);
					// json.append(link.getId(),
					// event.firstToken().getGlobalId());
				}
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
}
