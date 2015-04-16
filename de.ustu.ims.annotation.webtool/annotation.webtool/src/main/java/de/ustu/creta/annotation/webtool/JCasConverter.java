package de.ustu.creta.annotation.webtool;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
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
}
