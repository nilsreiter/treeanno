package de.ustu.ims.reiter.treeanno.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class JCasConverter {

	public JSONArray getJSONArrayFromAnnotations(JCas jcas,
			Class<? extends TreeSegment> annoClass) {
		TreeSet<JSONObject> ts =
				new TreeSet<JSONObject>(new Comparator<JSONObject>() {
					@Override
					public int compare(JSONObject o1, JSONObject o2) {
						int b1 = o1.getInt("begin"), b2 = o2.getInt("begin");
						int c = Integer.compare(b1, b2);
						if (c == 0) {
							int e1 = o1.getInt("end"), e2 = o2.getInt("end");
							return Integer.compare(e1, e2);
						}
						return c;
					}
				});

		for (TreeSegment anno : JCasUtil.select(jcas, annoClass)) {
			ts.add(getJSONObject(jcas, anno));
		}
		JSONArray arr = new JSONArray();
		for (JSONObject obj : ts) {
			arr.put(obj);
		}
		return arr;
	}

	public JSONObject getJSONObject(JCas jcas, TreeSegment annotation) {
		JSONObject obj = new JSONObject();
		obj.put("begin", annotation.getBegin());
		obj.put("end", annotation.getEnd());
		obj.put("text", annotation.getCoveredText());
		obj.put("Mark1", annotation.getMark1());
		if (annotation.getParent() != null)
			obj.put("parentId", annotation.getParent().getId());
		if (annotation.getCategory() != null)
			obj.put("category", annotation.getCategory());
		obj.put("id", annotation.getId());
		return obj;
	}

	public static JCas getJCas(String xmi) throws UIMAException, SAXException,
			IOException {
		JCas jcas = null;

		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory.createTypeSystemDescription();
		jcas = JCasFactory.createJCas(tsd);
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(xmi.getBytes());
			XmiCasDeserializer.deserialize(is, jcas.getCas(), true);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return jcas;
	}

	public static String getXmi(JCas jcas) throws SAXException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmiCasSerializer.serialize(jcas.getCas(), baos);

		String s = null;
		try {
			s = new String(baos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// This should not happen.
			e.printStackTrace();
		}
		return s;
	}

}
