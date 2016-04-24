package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentDelete
 */
@WebServlet("/rpc/document/delete")
public class DocumentDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int[] docIds;
		DataLayer dataLayer = CW.getDataLayer(getServletContext());

		if (request.getParameter("documentId") != null) {
			docIds = Util.getAllDocumentIds(request, response);
			for (int i = 0; i < docIds.length; i++) {
				try {
					Document document = dataLayer.getDocument(docIds[i]);
					dataLayer.deleteDocument(document);
				} catch (NumberFormatException | SQLException e) {
					throw new ServletException(e);
				}
			}
			Util.returnJSON(response, new JSONObject());
		} else if (request.getParameter("userDocumentId") != null) {
			docIds = Util.getAllUserDocumentIds(request, response);
			for (int i = 0; i < docIds.length; i++) {
				try {
					dataLayer.deleteUserDocument(Integer.valueOf(docIds[i]));
				} catch (NumberFormatException | SQLException e) {
					throw new ServletException(e);
				}
			}
			Util.returnJSON(response, new JSONObject());
		}
	}

}
