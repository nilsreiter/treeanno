package de.nilsreiter.segmentation.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.component.NoOpAnnotator;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.BoundaryCandidateAnnotator;
import de.nilsreiter.pipeline.segmentation.v1.FeatureExtractor;
import de.nilsreiter.pipeline.segmentation.wc.TimeAdverbAnnotator;
import de.nilsreiter.pipeline.segmentation.wc.TimeNounAnnotator;
import de.nilsreiter.pipeline.segmentation.wc.WordSet;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.ustu.ims.reiter.tense.annotator.AspectAnnotator;
import de.ustu.ims.reiter.tense.annotator.TenseAnnotator;

public class ClassificationExperiment extends WekaExperiment {

	public ClassificationExperiment(File wDir) {
		super(wDir);
	}

	@Override
	void runExperiment() {

	}

	@Override
	protected AnalysisEngineDescription[] getInitialization()
			throws ResourceInitializationException {
		return new AnalysisEngineDescription[] { createEngineDescription(NoOpAnnotator.class) };
	}

	@Override
	protected AnalysisEngineDescription[] getProcessing()
			throws ResourceInitializationException {
		ExternalResourceDescription extDesc =
				createExternalResourceDescription(WordSet.class, new File(
						new File(getWorkingDirectory(), "resources"),
						"timenouns.txt"));
		return new AnalysisEngineDescription[] {
				createEngineDescription(StanfordLemmatizer.class),
				createEngineDescription(StanfordPosTagger.class),
				createEngineDescription(StanfordNamedEntityRecognizer.class),
				createEngineDescription(TenseAnnotator.class),
				createEngineDescription(AspectAnnotator.class),
				createEngineDescription(TimeAdverbAnnotator.class),
				createEngineDescription(TimeNounAnnotator.class,
						TimeNounAnnotator.RESOURCE_LIST, extDesc) };
	}

	@Override
	protected AnalysisEngineDescription[] getFeatureExtraction()
			throws ResourceInitializationException {
		return new AnalysisEngineDescription[] {
				createEngineDescription(BoundaryCandidateAnnotator.class),
				createEngineDescription(FeatureExtractor.class) };
	}

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		WekaExperiment tte =
				new ClassificationExperiment(new File(
						"/Users/reiterns/Documents/SegNarr/Experiments/"));
		tte.setInputDirectoryName("/Users/reiterns/Documents/SegNarr/CrowdFlower/exp1/xmi/*.xmi");

		tte.doInitialization = true;
		tte.doProcessing = true;
		tte.setAnnotationType(de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector.class
				.getCanonicalName());
		tte.setClassFeatureName("NewSegment");

		System.exit(tte.run());
	}
}
