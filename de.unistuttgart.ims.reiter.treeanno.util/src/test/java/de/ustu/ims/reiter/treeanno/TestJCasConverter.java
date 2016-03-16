package de.ustu.ims.reiter.treeanno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.util.JCasConverter;

public class TestJCasConverter {

	JCas jcas;
	JCasConverter converter = new JCasConverter();

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("In einer Gegend des Harzes wohnte ein Ritter");
		jcas.setDocumentLanguage("de");

	}

	@Test
	public void testGetJSONObjectTreeSegment() {
		TreeSegment ts =
				AnnotationFactory.createAnnotation(jcas, 0, 2,
						TreeSegment.class);

		JSONObject jsobj = converter.getJSONObject(jcas, ts);
		assertNotNull(jsobj);
		assertEquals(0, jsobj.getInt("begin"));
		assertEquals(2, jsobj.getInt("end"));
		assertEquals("In", jsobj.getString("text"));
		assertTrue(jsobj.has("id"));
		assertEquals(0, jsobj.getInt("id"));
		assertFalse(jsobj.has("parentId"));
		assertFalse(jsobj.has("bla blubb"));
		assertFalse(jsobj.has("category"));
	}

	@Test(expected = NullPointerException.class)
	public void testGetJSONObjectTreeSegment2() {
		converter.getJSONObject(jcas, null);
	}

	@Test
	public void testGetJSONArrayFromAnnotations() {
		AnnotationFactory.createAnnotation(jcas, 0, 2, TreeSegment.class)
				.setId(1);
		AnnotationFactory.createAnnotation(jcas, 3, 8, TreeSegment.class)
		.setId(2);
		JSONArray array =
				converter.getJSONArrayFromAnnotations(jcas, TreeSegment.class);
		assertNotNull(array);
		assertEquals(2, array.length());
		assertEquals("In", array.getJSONObject(0).getString("text"));
		assertEquals("einer", array.getJSONObject(1).getString("text"));

	}

	@Test
	public void testGetJSONArrayFromAnnotationsWithoutAnnotations() {
		JSONArray array =
				converter.getJSONArrayFromAnnotations(jcas, TreeSegment.class);
		assertNotNull(array);
		assertEquals(0, array.length());

	}
}
