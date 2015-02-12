package de.nilsreiter.segmentation.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.ParagraphAnnotator;
import de.nilsreiter.pipeline.segmentation.Segment2Boundary;
import de.nilsreiter.pipeline.segmentation.SegmentationSubUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.topicmodeling.NarrativeDisentanglement;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class NMDExperiment extends Experiment {

	public NMDExperiment(File wDir) {
		super(wDir);
	}

	@Override
	protected AnalysisEngine[] getSegmentation()
			throws ResourceInitializationException {
		return new AnalysisEngine[] {
				createEngine(SegmentationUnitAnnotator.class,
						SegmentationUnitAnnotator.PARAM_BASE_TYPE,
						"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph"),
						createEngine(SegmentationSubUnitAnnotator.class,
								SegmentationSubUnitAnnotator.PARAM_BASE_TYPE,
								"de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity"),
				createEngine(NarrativeDisentanglement.class),
				// createEngine(SegmentMerger.class),
								createEngine(Segment2Boundary.class) };
	}

	@Override
	protected AnalysisEngine[] getProcessing()
			throws ResourceInitializationException {
		return new AnalysisEngine[] { createEngine(StanfordSegmenter.class),
				createEngine(StanfordLemmatizer.class),
				createEngine(StanfordPosTagger.class),
				createEngine(StanfordNamedEntityRecognizer.class),
				createEngine(ParagraphAnnotator.class) };
	}

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		Experiment tte =
				new NMDExperiment(new File(
						"/Users/reiterns/Documents/SegNarr/Experiments/"));
		tte.setInputDirectoryName("/Users/reiterns/Documents/Workspace/Segmentation Corpus/corpus/infinite_jest_annotated.txt.xmi");

		tte.doInitialization = false;
		tte.doProcessing = false;
		System.exit(tte.run());
	}

	@Override
	public String getDirectoryName() {
		return getClass().getSimpleName();
	}

}