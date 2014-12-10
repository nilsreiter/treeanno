package de.nilsreiter.pipeline.segmentation;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.component.ViewTextCopierAnnotator;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class TestGoldSegmentAnnotator {
	JCas jcas;

	@Test
	public void testGoldSegmentAnnotator()
			throws ResourceInitializationException, UIMAException, IOException {
		SimplePipeline.runPipeline(CollectionReaderFactory.createReader(
				TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
				"/Users/reiterns/Documents/SegNarr/txt/*.txt"),
				AnalysisEngineFactory.createEngine(
						ViewTextCopierAnnotator.class,
						ViewTextCopierAnnotator.PARAM_DESTINATION_VIEW_NAME,
						"gold", ViewTextCopierAnnotator.PARAM_SOURCE_VIEW_NAME,
						"_InitialView"), AnalysisEngineFactory.createEngine(
						GoldSegmentAnnotator.class,
								GoldSegmentAnnotator.PARAM_ANNOTATIONS_DIRECTORY,
						"/Users/reiterns/Documents/SegNarr/anno",
								GoldSegmentAnnotator.PARAM_VIEW, "gold"),
				AnalysisEngineFactory.createEngine(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						"/Users/reiterns/Documents/SegNarr/xmi/"));
	}
}
