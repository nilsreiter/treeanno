package de.ustu.ims.reiter.treeanno;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.json.JSONArray;
import org.json.JSONObject;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class JCasConverter {
	public JSONObject getJSONObject(JCas jcas) {
		JSONObject object = new JSONObject();
		DocumentMetaData dmd =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class);
		object.put("text", jcas.getDocumentText());
		object.put("title", dmd.getDocumentTitle());
		return object;
	}

	public JSONArray getJSONArrayFromAnnotations(JCas jcas,
			Class<? extends TreeSegment> annoClass) {
		JSONArray arr = new JSONArray();
		for (TreeSegment anno : JCasUtil.select(jcas, annoClass)) {
			arr.put(getJSONObject(jcas, anno));
		}
		return arr;
	}

	public JSONObject getJSONObject(JCas jcas, TreeSegment annotation) {
		JSONObject obj = new JSONObject();
		obj.put("begin", annotation.getBegin());
		obj.put("end", annotation.getEnd());
		obj.put("text", annotation.getCoveredText());
		if (annotation.getParent() == null) {
			/*
			 * if (annotation.getId() == 5) {
			 * obj.put("parentId", 4);
			 * }
			 */
		} else
			obj.put("parentId", annotation.getParent().getId());
		obj.put("category", annotation.getCategory());
		obj.put("id", annotation.getId());
		return obj;
	}
}
