package de.ustu.ims.reiter.treeanno;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.json.JSONObject;

import de.ustu.ims.segmentation.type.Segment;

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
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks. It is hungry.");
		jcas.setDocumentLanguage("en");
		AnnotationFactory.createAnnotation(jcas, 0, 14, Segment.class);
		AnnotationFactory.createAnnotation(jcas, 15, 28, Segment.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] documents = request.getParameterValues("document");
		if (documents.length > 0) {
			String docId = documents[0];
			JSONObject obj = new JSONObject();
			obj.put("documentId", docId);
			obj.put("list", new JCasConverter().getJSONArrayFromAnnotations(
					jcas, Segment.class));
			Util.returnJSON(response, obj);
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
