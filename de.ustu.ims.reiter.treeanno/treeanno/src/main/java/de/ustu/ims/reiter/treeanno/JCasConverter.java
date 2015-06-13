package de.ustu.ims.reiter.treeanno;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.json.JSONArray;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class JCasConverter {

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
		if (annotation.getParent() != null)
			obj.put("parentId", annotation.getParent().getId());
		if (annotation.getCategory() != null)
			obj.put("category", annotation.getCategory());
		obj.put("id", annotation.getId());
		return obj;
	}
}
