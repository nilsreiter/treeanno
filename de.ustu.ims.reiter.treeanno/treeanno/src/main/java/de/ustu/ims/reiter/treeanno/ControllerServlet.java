package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.User;

/**
 * Servlet implementation class ControllerServlet
 */
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	JCas jcas;

	/**
	 * Default constructor. 
	 * @throws UIMAException 
	 */
	public ControllerServlet() throws UIMAException {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DocumentIndex di =
				((DocumentIndex) this.getServletContext().getAttribute(
						"documentIndex"));

		String[] documents = request.getParameterValues("documentId");
		if (request.getSession().getAttribute("user") == null) {
			response.setStatus(Response.SC_FORBIDDEN);
			return;
		}
		try {
			if (documents.length > 0) {
				int docId = Integer.valueOf(documents[0]);

				int accessLevel =
						di.getDatabaseIO().getAccessLevel(
								Integer.valueOf(docId),
								(User) request.getSession()
								.getAttribute("user"));
				if (accessLevel < 10) {
					response.setStatus(Response.SC_FORBIDDEN);
					return;
				}
				if (di.getDatabaseIO().isHidden(docId)) {
					response.setStatus(Response.SC_NOT_FOUND);
					return;
				}
				JSONObject obj = new JSONObject();
				obj.put("documentId", docId);
				obj.put("list",
						new JCasConverter().getJSONArrayFromAnnotations(
								di.getDocument(Integer.valueOf(docId)),
								de.ustu.ims.reiter.treeanno.api.type.TreeSegment.class));
				Util.returnJSON(response, obj);
			}
		} catch (UIMAException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DocumentIndex di =
				((DocumentIndex) this.getServletContext().getAttribute(
						"documentIndex"));
		InputStream is = request.getInputStream();
		String s = IOUtils.toString(is);
		JSONObject jObj = new JSONObject(s);
		int docId = jObj.getInt("document");
		boolean r = false;
		try {
			JCas jcas = Util.addAnnotationsToJCas(di.getDocument(docId), jObj);
			r = di.getDatabaseIO().updateJCas(docId, jcas);
		} catch (UIMAException | JSONException | SQLException | SAXException e) {
			e.printStackTrace();
		}
		if (r) {
			Util.returnJSON(response, new JSONObject());
		} else {
			response.sendError(1);
		}
	}
}
