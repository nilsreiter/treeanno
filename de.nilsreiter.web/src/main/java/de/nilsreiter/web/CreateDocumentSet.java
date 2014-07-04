package de.nilsreiter.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.web.Location.Area;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

/**
 * Servlet implementation class CreateDocumentSet
 */
public class CreateDocumentSet extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Alignment<Event> alignment = new Alignment_impl<Event>(
				request.getParameter("setname"));

		DataReader dr = new DataReader();
		Enumeration<String> pEnum = request.getParameterNames();
		while (pEnum.hasMoreElements()) {
			String pName = pEnum.nextElement();
			if (pName.startsWith("doc")) {
				alignment.getDocuments().add(
						dr.read(docMan.findStreamFor(pName.substring(3))));
			}
		}

		// TODO: Saving needs to happen here, then maybe redirect?

		request.setAttribute("location",
				new Location("Rituals", Area.Alignment));

		request.setAttribute("alignment", alignment);
		request.setAttribute("documents", alignment.getDocuments());
		request.setAttribute("map", docMan.getClassesForTokens(alignment));
		request.setAttribute("doc", alignment.getId());

		request.setAttribute("arity", alignment.getDocuments().size());
		RequestDispatcher view = request
				.getRequestDispatcher("documentset/alignment.jsp");
		view.forward(request, response);
	}
}
