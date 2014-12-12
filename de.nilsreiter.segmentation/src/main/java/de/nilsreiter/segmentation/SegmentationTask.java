package de.nilsreiter.segmentation;

import static de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.INCLUDE_PREFIX;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.component.NoOpAnnotator;
import org.apache.uima.resource.ResourceInitializationException;

import weka.classifiers.bayes.BayesNet;
import de.tudarmstadt.ukp.dkpro.lab.Lab;
import de.tudarmstadt.ukp.dkpro.lab.task.Dimension;
import de.tudarmstadt.ukp.dkpro.lab.task.ParameterSpace;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask.ExecutionPolicy;
import de.tudarmstadt.ukp.dkpro.tc.core.Constants;
import de.tudarmstadt.ukp.dkpro.tc.features.length.NrOfCharsUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.style.InitialCharacterUpperCaseUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.style.IsSurroundedByCharsUFE;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.BatchCrossValidationReport;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.BatchRuntimeReport;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.ClassificationReport;
import de.tudarmstadt.ukp.dkpro.tc.weka.task.BatchTaskCrossValidation;
import de.tudarmstadt.ukp.dkpro.tc.weka.writer.WekaDataWriter;

public class SegmentationTask implements Constants {
	public static final int NUM_FOLDS = 2;

	public static void main(String[] args) throws Exception {
		System.setProperty("DKPRO_HOME", "/Users/reiterns/Documents/SegNarr");

		SegmentationTask task = new SegmentationTask();
		task.runCrossValidation(getParameterSpace());
	}

	static ParameterSpace getParameterSpace() {
		Map<String, Object> dimReaders = new HashMap<String, Object>();

		// Reader for annotated training data
		dimReaders.put(DIM_READER_TRAIN, GoldSegmentReader.class);

		// Parameters for the previous reader
		dimReaders.put(
				DIM_READER_TRAIN_PARAMS,
				Arrays.asList(new Object[] { GoldSegmentReader.PARAM_LANGUAGE,
						"en", GoldSegmentReader.PARAM_SOURCE_LOCATION,
						"src/main/resources/data/",
						GoldSegmentReader.PARAM_PATTERNS,
						INCLUDE_PREFIX + "*.xmi" }));

		@SuppressWarnings("unchecked")
		Dimension<List<String>> dimClassificationArgs =
				Dimension.create(DIM_CLASSIFICATION_ARGS, Arrays
						.asList(new String[] { BayesNet.class.getName() }));

		@SuppressWarnings("unchecked")
		Dimension<List<Object>> dimPipelineParameters =
				Dimension.create(
						DIM_PIPELINE_PARAMS,
						Arrays.asList(new Object[] {
								IsSurroundedByCharsUFE.PARAM_SURROUNDING_CHARS,
								"\"\"" }));

		@SuppressWarnings("unchecked")
		Dimension<List<String>> dimFeatureSets =
				Dimension.create(
						DIM_FEATURE_SET,
						Arrays.asList(new String[] {
								NrOfCharsUFE.class.getName(),
								InitialCharacterUpperCaseUFE.class.getName(),
								IsSurroundedByCharsUFE.class.getName() }));

		ParameterSpace pSpace =
				new ParameterSpace(
						Dimension.createBundle("readers", dimReaders),
						Dimension.create(DIM_DATA_WRITER,
								WekaDataWriter.class.getName()),
						Dimension.create(DIM_LEARNING_MODE, LM_SINGLE_LABEL),
						Dimension.create(DIM_FEATURE_MODE, FM_UNIT),
						dimPipelineParameters, dimFeatureSets,
						dimClassificationArgs);

		return pSpace;
	}

	protected void runCrossValidation(ParameterSpace pSpace) throws Exception {
		BatchTaskCrossValidation batch =
				new BatchTaskCrossValidation("SegmentationTaskCV",
						getPreprocessing(), NUM_FOLDS);
		batch.addInnerReport(ClassificationReport.class);
		batch.setParameterSpace(pSpace);
		batch.setExecutionPolicy(ExecutionPolicy.RUN_AGAIN);
		batch.addReport(BatchCrossValidationReport.class);
		batch.addReport(BatchRuntimeReport.class);

		// Run
		Lab.getInstance().run(batch);
	}

	protected AnalysisEngineDescription getPreprocessing()
			throws ResourceInitializationException {
		return createEngineDescription(NoOpAnnotator.class);
	}
}
