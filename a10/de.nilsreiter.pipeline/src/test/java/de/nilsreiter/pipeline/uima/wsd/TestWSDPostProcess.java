package de.nilsreiter.pipeline.uima.wsd;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.wsd.type.WSDItem;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;

public class TestWSDPostProcess {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks.");
		jcas.setDocumentLanguage("en");

	}

	@Test
	public void testCompleteCase() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		WSDItem item;
		WSDResult result;

		item = createAnnotation(jcas, 4, 7, WSDItem.class);
		result = createAnnotation(jcas, 0, 0, WSDResult.class);
		result.setWsdItem(item);

		SimplePipeline.runPipeline(jcas,
				createEngineDescription(WSDPostProcess.class));

		item = JCasUtil.selectByIndex(jcas, WSDItem.class, 0);
		result = JCasUtil.selectByIndex(jcas, WSDResult.class, 0);

		assertEquals(1, JCasUtil.select(jcas, WSDResult.class).size());
		assertEquals(1, JCasUtil.select(jcas, WSDItem.class).size());
		assertEquals(item.getBegin(), result.getBegin());
		assertEquals(item.getEnd(), result.getEnd());
	}

	@Test
	public void testIncompleteCase() throws AnalysisEngineProcessException,
	ResourceInitializationException {
		WSDItem item;
		WSDResult result;

		item = createAnnotation(jcas, 4, 7, WSDItem.class);
		result = createAnnotation(jcas, 0, 0, WSDResult.class);

		SimplePipeline.runPipeline(jcas,
				createEngineDescription(WSDPostProcess.class));

		item = JCasUtil.selectByIndex(jcas, WSDItem.class, 0);
		result = JCasUtil.selectByIndex(jcas, WSDResult.class, 0);

		assertEquals(1, JCasUtil.select(jcas, WSDResult.class).size());
		assertEquals(1, JCasUtil.select(jcas, WSDItem.class).size());
		assertEquals(item.getBegin(), result.getBegin());
		assertEquals(item.getEnd(), result.getEnd());
	}
}
