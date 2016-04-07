package de.ustu.ims.reiter.treeanno.util;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

public class TestComparison {

	JCas jcas1 = null, jcas2 = null;

	@Test
	public void testEqualSegmentation() throws UIMAException {
		jcas1 = JCasFactory.createJCas();
		jcas2 = JCasFactory.createJCas();

		jcas1.setDocumentText("");
	}
}
