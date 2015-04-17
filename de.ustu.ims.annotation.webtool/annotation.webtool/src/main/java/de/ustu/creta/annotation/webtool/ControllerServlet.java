package de.ustu.creta.annotation.webtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

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
import de.ustu.creta.annotation.webtool.beans.TextDocument;

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
				for (JSONObject obj2 : getDocument(path[1]).getAnnotations()
						.values()) {
					obj.put(obj2);
				}
				Util.returnJSON(response, obj);
			}
			return;
		}
		// return a single annotation (if it exists)
		if (getDocument(path[1]).getAnnotations().containsKey(annoId)) {
			JSONObject obj = getDocument(path[1]).getAnnotations().get(annoId);
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

			TextDocument td = new TextDocument();
			td.setJcas(jcas);
			td.setId(docId);

			@SuppressWarnings("unchecked")
			Map<String, TextDocument> documentMap =
			(Map<String, TextDocument>) this.getServletContext()
			.getAttribute("documents");
			documentMap.put(docId, td);

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
		getDocument(path[1]).getAnnotations().put(id, obj);

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
			JSONObject oldAnno = getDocument(path[1]).getAnnotations().get(id);
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
			getDocument(path[1]).getAnnotations().remove(path[3]);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			response.setContentLength(0);
		}
	}

	@SuppressWarnings("unchecked")
	protected TextDocument getDocument(String docId) {
		return ((Map<String, TextDocument>) this.getServletContext()
				.getAttribute("documents")).get(docId);
	}

}
