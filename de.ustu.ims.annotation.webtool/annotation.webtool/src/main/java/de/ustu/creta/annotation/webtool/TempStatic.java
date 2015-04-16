package de.ustu.creta.annotation.webtool;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.jcas.JCas;
import org.json.JSONObject;

public class TempStatic {
	static Map<String, JCas> documents = new HashMap<String, JCas>();
	static Map<String, Map<String, JSONObject>> annotations =
			new HashMap<String, Map<String, JSONObject>>();

	static int index = 0;

	static String text;

}
