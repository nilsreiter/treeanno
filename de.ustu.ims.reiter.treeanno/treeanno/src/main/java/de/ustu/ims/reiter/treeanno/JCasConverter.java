package de.ustu.ims.reiter.treeanno;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.json.JSONArray;
import org.json.JSONObject;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

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
			Class<? extends Annotation> annoClass) {
		JSONArray arr = new JSONArray();
		for (Annotation anno : JCasUtil.select(jcas, annoClass)) {
			arr.put(this.getJSONObject(jcas, anno));
		}
		return arr;
	}

	public JSONObject getJSONObject(JCas jcas, Annotation annotation) {
		JSONObject obj = new JSONObject();
		obj.put("begin", annotation.getBegin());
		obj.put("end", annotation.getEnd());
		obj.put("text", annotation.getCoveredText());
		return obj;
	}
}
