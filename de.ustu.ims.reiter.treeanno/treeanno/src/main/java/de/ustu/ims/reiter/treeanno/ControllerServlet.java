package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.json.JSONException;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.io.DatabaseReader;

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
		DatabaseReader dr =
				((DatabaseReader) request.getServletContext().getAttribute(
						"databaseReader"));
		String[] documents = request.getParameterValues("document");
		try {
			if (documents.length > 0) {
				String docId = documents[0];
				JSONObject obj = new JSONObject();
				obj.put("documentId", docId);
				obj.put("list",
						new JCasConverter().getJSONArrayFromAnnotations(
								dr.getJCas(Integer.valueOf(docId)),
								(Class<? extends Annotation>) Class
								.forName("webanno.custom.Zusammenfassung")));
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
		} catch (ClassNotFoundException e) {
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
		// TODO Auto-generated method stub
	}

}