package de.nilsreiter.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;

/**
 * Servlet implementation class AlignmentLoader
 */
public class AlignmentLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	EventAlignmentReader alignmentReader;
	ServletDocumentManager docMan = new ServletDocumentManager();

	public AlignmentLoader() {
		super();
		alignmentReader = new EventAlignmentReader(docMan);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Alignment<Event> alignment = alignmentReader.read(docMan
				.findStreamFor("/alignment/gold"));

		request.setAttribute("alignment", alignment);
		int i = 0;
		for (Document document : alignment.getDocuments()) {
			request.setAttribute("document" + i, document);
		}
		request.setAttribute("arity", alignment.getDocuments().size());
		RequestDispatcher view = request.getRequestDispatcher("alignment.jsp");
		view.forward(request, response);
	}

}
