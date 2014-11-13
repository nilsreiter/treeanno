package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.nilsreiter.util.db.Database;
import de.nilsreiter.web.db.SVGDatabase;

/**
 * Servlet implementation class StoreSVG
 */
public class StoreSVG extends RPCServlet {
	private static final long serialVersionUID = 1L;

	SVGDatabase db = null;

	public StoreSVG() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (db == null) {
			ServletContext sc = getServletContext();
			db = new SVGDatabase((Database) sc.getAttribute("database"));
		}
		db.putSVGString(request.getParameter("document"),
				request.getParameter("event"), request.getParameter("svg"));
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (db == null) {
			ServletContext sc = getServletContext();
			db = new SVGDatabase((Database) sc.getAttribute("database"));
		}
		String svgString =
				db.getSVGString(request.getParameter("document"),
						request.getParameter("event"));
		if (svgString != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("{error:0,svg:'" + svgString + "'}");
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(
					"{message:'No SVG string in database.',error:1}");
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
}
