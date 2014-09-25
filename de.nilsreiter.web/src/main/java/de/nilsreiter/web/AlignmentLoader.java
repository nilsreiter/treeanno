package de.nilsreiter.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

/**
 * Servlet implementation class AlignmentLoader
 */
public class AlignmentLoader extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("doc") == null) {
			RequestDispatcher view =
					request.getRequestDispatcher("alignment/select.jsp");
			view.forward(request, response);
			return;
		}
		Alignment<Event> alignment;
		alignment = getAlignment(request);

		request.setAttribute("alignment", alignment);
		request.setAttribute("documents", alignment.getDocuments());
		request.setAttribute("map", docMan.getClassesForTokens(alignment));
		request.setAttribute("doc", alignment.getId());

		request.setAttribute("arity", alignment.getDocuments().size());
		RequestDispatcher view =
				request.getRequestDispatcher("alignment/alignment.jsp");
		view.forward(request, response);

	}
}
