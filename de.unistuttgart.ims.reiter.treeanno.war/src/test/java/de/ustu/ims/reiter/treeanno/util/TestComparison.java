package de.ustu.ims.reiter.treeanno.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class TestComparison {

	String text =
			"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam";
	JCas jcas1 = null, jcas2 = null;

	@Test
	public void testEqualSegmentation() throws UIMAException {
		jcas1 = JCasFactory.createJCas();
		jcas2 = JCasFactory.createJCas();

		jcas1.setDocumentText(text);
		jcas2.setDocumentText(text);

		int b, e;
		b = 0;
		e = 5;
		AnnotationFactory.createAnnotation(jcas1, b, e, TreeSegment.class);
		AnnotationFactory.createAnnotation(jcas2, b, e, TreeSegment.class);

		b = 6;
		e = 8;
		AnnotationFactory.createAnnotation(jcas1, b, e, TreeSegment.class);
		AnnotationFactory.createAnnotation(jcas2, b, e, TreeSegment.class);

		b = 10;
		e = 13;
		AnnotationFactory.createAnnotation(jcas1, b, e, TreeSegment.class);
		AnnotationFactory.createAnnotation(jcas2, b, e, TreeSegment.class);

		assertNotNull(jcas1);
		assertNotNull(jcas2);
		assertTrue(Comparison.equalSegmentation(jcas1, jcas2));
	}
}
