package de.nilsreiter.segmentation.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.SegmentationSubUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.morphadorner.C99Annotator;

public class C99Experiment extends Experiment {

	public C99Experiment(File wDir) {
		super(wDir);
	}

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		Experiment tte =
				new C99Experiment(new File(
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
				createEngine(C99Annotator.class, C99Annotator.PARAM_MASK_SIZE,
						5) };
	}

	@Override
	public String getDirectoryName() {
		return getClass().getSimpleName();
	}

}
