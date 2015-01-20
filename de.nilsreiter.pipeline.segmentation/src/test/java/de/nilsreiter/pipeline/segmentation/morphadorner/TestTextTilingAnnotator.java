package de.nilsreiter.pipeline.segmentation.morphadorner;

import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestTextTilingAnnotator {
	JCas jcas;

	@Before
	public void setUp() throws Exception {
		jcas = JCasFactory.createJCas();
		InputStreamReader isr =
				new InputStreamReader(getClass().getResourceAsStream(
						"/segmentation_test.txt"));
		StringBuilder b = new StringBuilder();
		while (isr.ready()) {
			b.append((char) isr.read());
		}

		jcas.setDocumentText(b.toString());
		jcas.setDocumentLanguage("en");
		SimplePipeline.runPipeline(jcas,
				AnalysisEngineFactory.createEngine(StanfordSegmenter.class));
	};

	@Test
	public void testTextTiling() throws Exception {
		TextTilingAnnotator tta = new TextTilingAnnotator();
		tta.stepSize = 10;
		tta.windowSize = 10;
		tta.process(jcas);
		assertTrue(JCasUtil.exists(jcas, SegmentBoundary.class));
		// assertEquals(147, JCasUtil.select(jcas,
		// SegmentBoundary.class).size());

		int i = 0;
		String text = jcas.getDocumentText();
		for (SegmentBoundary b : JCasUtil.select(jcas, SegmentBoundary.class)) {
			System.out.println(text.substring(i, b.getBegin()));
			System.out.println("\n\n=================\n\n");
			i = b.getBegin();
		}
	}
}
