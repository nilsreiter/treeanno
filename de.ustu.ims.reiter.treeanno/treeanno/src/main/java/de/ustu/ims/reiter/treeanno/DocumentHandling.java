package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentHandling
 */
public class DocumentHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentHandling() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameterValues("action") == null
				|| request.getParameterValues("action").length != 1) {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		String action = request.getParameterValues("action")[0];
		DocumentIndex di =
				((DocumentIndex) this.getServletContext().getAttribute(
						"documentIndex"));
		if (action.equalsIgnoreCase("delete")) {
			String[] docIds = request.getParameterValues("documentId");
			for (int i = 0; i < docIds.length; i++) {
				try {
					di.getDatabaseIO().deleteDocument(
							Integer.valueOf(docIds[i]));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
			}
			Util.returnJSON(response, new JSONObject());
		} else if (action.equalsIgnoreCase("clone")) {
			String[] docIds = request.getParameterValues("documentId");
			for (int i = 0; i < docIds.length; i++) {
				try {
					di.cloneDocument(Integer.valueOf(docIds[i]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Util.returnJSON(response, new JSONObject());
		}

	}

}
