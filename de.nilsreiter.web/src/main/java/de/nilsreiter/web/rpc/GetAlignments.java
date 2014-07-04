package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.web.AbstractServlet;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;

public class GetAlignments extends AbstractServlet {
	EventAlignmentReader alignmentReader;

	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		super.init();
		alignmentReader = new EventAlignmentReader(docMan);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("doc") == null) {
			response.getWriter().print("");
			return;
		}

		Alignment<Event> alignment = alignmentReader.read(docMan
				.findStreamFor(request.getParameter("doc")));

		JSONObject json = new JSONObject();
		for (Link<Event> link : alignment.getAlignments()) {
			for (Event event : link.getElements()) {
				json.append(link.getId(), event.firstToken().getGlobalId());
			}
		}
		response.setContentType("application/json");
		response.getWriter().print(json.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
}
