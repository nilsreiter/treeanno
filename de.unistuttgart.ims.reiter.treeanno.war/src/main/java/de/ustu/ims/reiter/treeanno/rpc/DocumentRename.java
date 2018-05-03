package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentRename
 */
@Deprecated
public class DocumentRename extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int docId = Util.getFirstDocumentId(request, response);
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		try {
			Document document = dataLayer.getDocument(docId);
			document.setName(request.getParameter("name"));
			dataLayer.updateDocument(document);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
		Util.returnJSON(response, new JSONObject());
	}

}
