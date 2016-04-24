package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentHandling
 */
public class DocumentHandling extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public enum ExportFormat {
		XMI, PAR
	};

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		int[] docIds = Util.getAllDocumentIds(request, response);
		if (action.equalsIgnoreCase("delete")) {
			// empty, use HTTP DELETE instead
		} else if (action.equalsIgnoreCase("clone")) {
			throw new UnsupportedOperationException();
		} else if (action.equalsIgnoreCase("export")) {
			// empty, now done by DocumentExport

		} else if (action.equalsIgnoreCase("rename")) {
			try {
				String docId = request.getParameterValues("documentId")[0];
				Document document =
						dataLayer.getDocument(Integer.valueOf(docId));
				document.setName(request.getParameter("name"));
				dataLayer.updateDocument(document);
				Util.returnJSON(response, new JSONObject());
			} catch (NullPointerException | NumberFormatException
					| SQLException e) {
				throw new ServletException(e);
			}
		}

	}
}
