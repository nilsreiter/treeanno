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
		jcas.setDocumentText("The dog barks. It is hungry. Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		jcas.setDocumentLanguage("en");
		AnnotationFactory.createAnnotation(jcas, 0, 14, Segment.class);
		AnnotationFactory.createAnnotation(jcas, 15, 28, Segment.class);
		AnnotationFactory.createAnnotation(jcas, 29, 35, Segment.class);
		AnnotationFactory.createAnnotation(jcas, 36, 46, Segment.class);
		AnnotationFactory.createAnnotation(jcas, 46, jcas.getDocumentText()
				.length(), Segment.class);
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
