package de.ustu.ims.reiter.treeanno.uima;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.tree.PrintParenthesesWalker;

public class TestGraphExporter {
	JCas jcas;
	TreeSegment[] ts;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("Bla Bla Bla Bla");
		DocumentMetaData.create(jcas).setDocumentId("Test");
		ts =
				new TreeSegment[] {
				AnnotationFactory.createAnnotation(jcas, 0, 3,
						TreeSegment.class),
						AnnotationFactory.createAnnotation(jcas, 4, 7,
								TreeSegment.class),
								AnnotationFactory.createAnnotation(jcas, 8, 11,
										TreeSegment.class),
										AnnotationFactory.createAnnotation(jcas, 12, 14,
												TreeSegment.class) };
	}

	@Test
	public void testGraphExporter() throws AnalysisEngineProcessException,
			ResourceInitializationException, IOException {
		String result = GraphExporter.getTreeString(jcas, new PrintParenthesesWalker<TreeSegment>());
		assertEquals("((())()())", result);
	}
}
