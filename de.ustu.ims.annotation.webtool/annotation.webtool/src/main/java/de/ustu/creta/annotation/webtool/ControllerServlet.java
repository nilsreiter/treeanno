package de.ustu.creta.annotation.webtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.json.JSONArray;
import org.json.JSONObject;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

/**
 * Servlet implementation class ControllerServlet
 */
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControllerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] path = request.getPathInfo().split("/");
		String docId = null;
		String area = null;
		String annoId = null;
		if (path.length > 1) {
			docId = path[1];
		}
		if (path.length > 2) {
			area = path[2];
		} else {
			// load the document
			loadText(docId, response);
			return;
		}
		if (path.length > 3) {
			annoId = path[3];
		} else {
			// return a list of annotations for the document
			if (area.equalsIgnoreCase("annotations")) {
				JSONArray obj = new JSONArray();
				for (JSONObject obj2 : TempStatic.annotations.get(docId)
						.values()) {
					obj.put(obj2);
				}
				Util.returnJSON(response, obj);
			}
			return;
		}
		// return a single annotation (if it exists)
		if (TempStatic.annotations.get(docId).containsKey(annoId)) {
			JSONObject obj = TempStatic.annotations.get(docId).get(annoId);
			Util.returnJSON(response, obj);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setContentLength(0);
			response.getWriter().close();
		}
	}

	protected void loadText(String docId, HttpServletResponse response)
			throws IOException {
		try {
			JCas jcas = JCasFactory.createJCas();
			jcas.setDocumentText(IOUtils.toString(
					new FileInputStream(new File(
							"/Users/reiterns/Desktop/Der Blonde Eckbert.txt")),
					"UTF-8").trim());
			DocumentMetaData dmd =
					AnnotationFactory
							.createAnnotation(jcas, 0, jcas.getDocumentText()
									.length(), DocumentMetaData.class);
			dmd.setDocumentTitle("Der Blonde Eckbert");
			TempStatic.documents.put(docId, jcas);
			TempStatic.annotations
			.put(docId, new HashMap<String, JSONObject>());

			Util.returnJSON(response, new JCasConverter().getJSONObject(jcas));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace(response.getWriter());
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] path = request.getPathInfo().split("/");

		String s = IOUtils.toString(request.getInputStream(), "UTF-8");
		JSONObject obj = new JSONObject(s);
		String id = "anno" + TempStatic.index++;
		obj.put("id", id);
		TempStatic.annotations.get(path[1]).put(id, obj);

		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		response.setHeader("Location", request.getRequestURL().toString() + "/"
				+ id);
		response.setContentLength(0);
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] path = request.getPathInfo().split("/");

		String s =
				IOUtils.toString(request.getInputStream(),
						request.getCharacterEncoding());
		if (request.getContentType().contains("application/json")) {
			JSONObject newAnno = new JSONObject(s);
			String id = path[path.length - 1];
			JSONObject oldAnno = TempStatic.annotations.get(path[1]).get(id);
			for (Object okey : newAnno.keySet()) {
				String key = (String) okey;
				oldAnno.put(key, newAnno.get(key));
			}

			response.setStatus(HttpServletResponse.SC_SEE_OTHER);
			response.setHeader("Location", id);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] path = request.getPathInfo().split("/");
		if (path.length > 3) {
			TempStatic.annotations.get(path[1]).remove(path[3]);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			response.setContentLength(0);
		}
	}

}
