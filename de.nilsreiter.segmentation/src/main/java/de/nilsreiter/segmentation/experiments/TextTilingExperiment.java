package de.nilsreiter.segmentation.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.SegmentationSubUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.morphadorner.TextTilingAnnotator;

public class TextTilingExperiment extends Experiment {

	public TextTilingExperiment(File wDir) {
		super(wDir);
	}

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		Experiment tte =
				new TextTilingExperiment(new File(
						"/Users/reiterns/Documents/SegNarr/Experiments"));
		tte.setInputDirectoryName("/Users/reiterns/Documents/Workspace/Segmentation Corpus/corpus/infinite_jest_annotated.txt.xmi");

		tte.doInitialization = false;
		tte.doProcessing = false;
		System.exit(tte.run());
	}

	@Override
	protected AnalysisEngine[] getSegmentation()
			throws ResourceInitializationException {
		return new AnalysisEngine[] {
				createEngine(SegmentationUnitAnnotator.class),
				createEngine(SegmentationSubUnitAnnotator.class),
				createEngine(TextTilingAnnotator.class,
						TextTilingAnnotator.PARAM_WINDOW_SIZE, 250,
						TextTilingAnnotator.PARAM_STEP_SIZE, 10) };
	}

}
