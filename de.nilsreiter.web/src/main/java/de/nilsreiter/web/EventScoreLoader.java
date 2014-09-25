package de.nilsreiter.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

/**
 * Servlet implementation class EventScoreLoader
 */
public class EventScoreLoader extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("doc") == null) {
			request.setAttribute("target", "view-event-scores");
			RequestDispatcher view =
					request.getRequestDispatcher("alignment/select.jsp");
			view.forward(request, response);
			return;
		}

		Alignment<Event> alignment = getAlignment(request);

		request.setAttribute("doc", alignment.getId());
		request.setAttribute("alignment", alignment);
		request.setAttribute("documents", alignment.getDocuments());

		RequestDispatcher view =
				request.getRequestDispatcher("alignment/event-scores.jsp");
		view.forward(request, response);

	}
}
