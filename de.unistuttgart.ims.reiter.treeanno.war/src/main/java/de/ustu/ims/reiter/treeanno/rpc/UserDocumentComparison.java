package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.uima.UIMAException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.Comparison;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class UserDocumentComparison
 */
@WebServlet("/rpc/compare")
public class UserDocumentComparison extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int documentId = Util.getFirstDocumentId(request, response);
		int[] userIds = Util.getAllUserIds(request, response);
		DataLayer dataLayer = CW.getDataLayer(getServletContext());
		if (userIds.length == 2) {
			try {
				JSONObject object = new JSONObject();
				UserDocument udoc1 =
						dataLayer.getUserDocument(userIds[0], documentId);
				UserDocument udoc2 =
						dataLayer.getUserDocument(userIds[1], documentId);
				boolean same =
						Comparison.equalSegmentation(
								JCasConverter.getJCas(udoc1.getXmi()),
								JCasConverter.getJCas(udoc2.getXmi()));

				object.append("userDocuments", udoc1.getId());
				object.append("userDocuments", udoc2.getId());
				object.put("equalSegmentation", same);

				Util.returnJSON(response, object);
			} catch (NumberFormatException | SQLException | UIMAException
					| SAXException e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}

	}

}
