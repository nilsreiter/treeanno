package de.ustu.creta.annotation.webtool.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.jcas.JCas;
import org.json.JSONObject;

public class TextDocument {

	String id;
	JCas jcas;

	Map<String, JSONObject> annotations = new HashMap<String, JSONObject>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JCas getJcas() {
		return jcas;
	}

	public void setJcas(JCas jcas) {
		this.jcas = jcas;
	}

	public Map<String, JSONObject> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Map<String, JSONObject> annotations) {
		this.annotations = annotations;
	}
}
