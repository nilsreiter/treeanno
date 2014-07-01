package de.nilsreiter.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.web.Location.Area;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;

/**
 * Servlet implementation class AlignmentLoader
 */
public class AlignmentLoader extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	EventAlignmentReader alignmentReader;

	public AlignmentLoader() {
		super();
	}

	@Override
	public void init() {
		super.init();
		alignmentReader = new EventAlignmentReader(docMan);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("doc") == null) {
			RequestDispatcher view = request
					.getRequestDispatcher("documentset/select.jsp");
			view.forward(request, response);
			return;
		}
		Alignment<Event> alignment = alignmentReader.read(docMan
				.findStreamFor(request.getParameter("doc")));
		StringBuilder alignmentIds = new StringBuilder();
		for (Link<Event> link : alignment.getAlignments()) {
			alignmentIds.append(link.getId());
			alignmentIds.append(' ');
		}

		request.setAttribute("location",
				new Location("Rituals", Area.Alignment));
		request.setAttribute("alignment", alignment);
		request.setAttribute("documents", alignment.getDocuments());
		request.setAttribute("map", docMan.getClassesForTokens(alignment));
		request.setAttribute("alignmentIds", alignmentIds);
		request.setAttribute("doc", alignment.getId());

		request.setAttribute("arity", alignment.getDocuments().size());
		RequestDispatcher view = request
				.getRequestDispatcher("documentset/alignment.jsp");
		view.forward(request, response);
	}
}
