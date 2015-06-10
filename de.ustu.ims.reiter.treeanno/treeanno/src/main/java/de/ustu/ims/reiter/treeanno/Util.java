package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.ustu.ims.narratology.util.ClearAnnotation;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

/**
 * Servlet implementation class JSONServlet
 */
public class Util {

	public static void returnJSON(HttpServletResponse response,
			JSONObject object) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(object.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	public static void
	returnJSON(HttpServletResponse response, JSONArray object)
			throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(object.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	public static JCas addAnnotationsToJCas(JCas jcas, JSONObject annotations)
			throws AnalysisEngineProcessException,
			ResourceInitializationException {

		// remove old annotations
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(ClearAnnotation.class,
						ClearAnnotation.PARAM_TYPE,
						TreeSegment.class.getCanonicalName()));

		// add annotations to JCas
		Map<Integer, TreeSegment> idMap = new HashMap<Integer, TreeSegment>();
		JSONArray items = annotations.getJSONArray("items");
		for (int i = 0; i < items.length(); i++) {
			if (!items.isNull(i)) {
				JSONObject item = items.getJSONObject(i);

				int begin = item.getInt("begin");
				int end = item.getInt("end");
				int id = item.getInt("id");

				TreeSegment ts =
						AnnotationFactory.createAnnotation(jcas, begin, end,
								TreeSegment.class);
				ts.setId(id);

				try {
					if (!item.getString("category").isEmpty())
						ts.setCategory(item.getString("category"));
				} catch (JSONException e) {};
				idMap.put(id, ts);
			}
		}
		for (int i = 0; i < items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			int parentId = -1;
			int id = item.getInt("id");
			try {
				parentId = item.getInt("parentId");
			} catch (JSONException e) {}
			if (parentId >= 0) {
				idMap.get(id).setParent(idMap.get(parentId));
			}
		}
		return jcas;
	}
}
