package de.nilsreiter.pipeline.uima.event;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.uima.event.type.Event;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticArgument;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;

public class TestEventAnnotator {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks.");
		jcas.setDocumentLanguage("en");

		SemanticArgument arg =
				createAnnotation(jcas, 0, 7, SemanticArgument.class);
		arg.setRole("EVENT_TEST_ROLE");
		SemanticPredicate sp =
				createAnnotation(jcas, 8, 15, SemanticPredicate.class);
		sp.setCategory("EVENT_TEST_CATEGORY");
		sp.setArguments(new FSArray(jcas, 1));
		sp.setArguments(0, arg);
	}

	@Test
	public void testEventAnnotator() throws AnalysisEngineProcessException,
			ResourceInitializationException {
		SimplePipeline.runPipeline(jcas,
				createEngineDescription(EventAnnotator.class));

		assertTrue(JCasUtil.exists(jcas, Event.class));
		assertEquals("EVENT_TEST_ROLE",
				JCasUtil.selectByIndex(jcas, Event.class, 0).getName());
	}

}
