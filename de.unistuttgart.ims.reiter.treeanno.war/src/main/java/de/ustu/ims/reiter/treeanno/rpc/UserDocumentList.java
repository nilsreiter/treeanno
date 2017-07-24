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
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class UserDocumentList
 */
public class UserDocumentList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] docIds = request.getParameterValues("documentId");
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		User user = CW.getUser(request);
		if (docIds.length == 1) {
			Document document;
			try {
				JSONObject json = new JSONObject();
				document = dataLayer.getDocument(Integer.valueOf(docIds[0]));
				if (dataLayer.getAccessLevel(document.getProject(), user) >= Perm.PADMIN_ACCESS) {
					json.put("src", JSONUtil.getJSONObject(document));
					for (UserDocument ud : document.getUserDocuments()) {
						json.append("documents", JSONUtil.getJSONObject(ud));
					}
				} else {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
				Util.returnJSON(response, json);
			} catch (NumberFormatException | SQLException e) {
				throw new ServletException(e);
			}
		}
	}

}
