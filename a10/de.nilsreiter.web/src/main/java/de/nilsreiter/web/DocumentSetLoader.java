package de.nilsreiter.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniheidelberg.cl.a10.data2.DocumentSet;

/**
 * Servlet implementation class AlignmentLoader
 */

public class DocumentSetLoader extends AbstractServlet {
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
					request.getRequestDispatcher("documentset/select.jsp");
			view.forward(request, response);
			return;
		}
		DocumentSet documentset = getDocumentSet(request);

		if (documentset != null) {
			request.setAttribute("documents", documentset.getSet());
			request.setAttribute("map",
					docMan.getClassesForTokens(documentset.getSet()));
			request.setAttribute("doc", documentset.getId());

			request.setAttribute("arity", documentset.size());
			RequestDispatcher view =
					request.getRequestDispatcher("documentset/documentset.jsp");
			view.forward(request, response);
		}

	}
}