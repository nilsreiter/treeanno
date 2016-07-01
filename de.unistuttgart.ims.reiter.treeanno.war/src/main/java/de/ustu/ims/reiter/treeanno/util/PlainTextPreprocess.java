package de.ustu.ims.reiter.treeanno.util;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class PlainTextPreprocess<T extends Annotation> {
	AnalysisEngineDescription[] ae;
	Class<T> clazz;

	public PlainTextPreprocess(Class<T> clazz)
			throws ResourceInitializationException {
		this.clazz = clazz;
		this.ae =
				new AnalysisEngineDescription[] {
				createEngineDescription(BreakIteratorSegmenter.class),
				createEngineDescription(MapToTreeAnnoClass.class,
						MapToTreeAnnoClass.PARAM_CLASSNAME,
						clazz.getCanonicalName()) };
	}

	public Iterator<JCas> process(File inputDirectory, String language)
			throws ResourceInitializationException {
		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						inputDirectory.getAbsolutePath() + File.separator
								+ "*.txt", TextReader.PARAM_LANGUAGE, language);

		return SimplePipeline.iteratePipeline(crd, ae).iterator();
	}
}
