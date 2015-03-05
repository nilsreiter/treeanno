package de.nilsreiter.segmentation.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.uima.ClearAnnotation;
import de.nilsreiter.pipeline.weka.ArffConsumer;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public abstract class WekaExperiment {

	Logger logger = java.util.logging.Logger.getLogger(getClass().getName());

	File workingDirectory;

	String inputDirectoryName;
	String outputDirectoryName;

	boolean doInitialization = true;
	boolean doProcessing = true;
	boolean doExtraction = true;

	String annotationType;
	String classFeatureName;
	String arffFileName = "data.arff";

	public WekaExperiment(File wDir) {
		workingDirectory = new File(wDir, this.getDirectoryName());
		if (!workingDirectory.exists()) workingDirectory.mkdirs();
	}

	public File getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public String getOutputDirectory() {
		return outputDirectoryName;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectoryName = outputDirectory;
	}

	public String getInputDirectoryName() {
		return inputDirectoryName;
	}

	public void setInputDirectoryName(String inputDirectoryName) {
		this.inputDirectoryName = inputDirectoryName;
	}

	protected AnalysisEngineDescription[] getInitialization()
			throws ResourceInitializationException {

		return new AnalysisEngineDescription[] {
				createEngineDescription(ClearAnnotation.class,
						ClearAnnotation.PARAM_TYPE,
						SegmentBoundary.class.getCanonicalName()),
						createEngineDescription(ClearAnnotation.class,
								ClearAnnotation.PARAM_TYPE,
								Segment.class.getCanonicalName()) };
	}

	protected AnalysisEngineDescription[] getArffConversion()
			throws ResourceInitializationException {
		return new AnalysisEngineDescription[] { createEngineDescription(
				ArffConsumer.class, ArffConsumer.PARAM_ANNOTATION_TYPE,
				getAnnotationType(), ArffConsumer.PARAM_CLASS_FEATURE,
				getClassFeatureName(), ArffConsumer.PARAM_OUTPUT_FILE,
				getArffFile().getAbsolutePath()) };
	};

	protected AnalysisEngineDescription[] getProcessing()
			throws ResourceInitializationException {
		return new AnalysisEngineDescription[] {
				createEngineDescription(StanfordSegmenter.class),
				createEngineDescription(StanfordLemmatizer.class),
				createEngineDescription(StanfordPosTagger.class),
				createEngineDescription(StanfordNamedEntityRecognizer.class) };
	}

	public int run() throws ResourceInitializationException, UIMAException,
	IOException {
		int step = 1;
		if (doInitialization) runStep(step, getInitialization());
		step++;
		if (doProcessing) runStep(step, getProcessing());
		step++;
		if (doExtraction) runStep(step, getFeatureExtraction());
		step++;
		runStep(step, getArffConversion());

		runExperiment();

		return step;

	}

	abstract void runExperiment();

	protected abstract AnalysisEngineDescription getFeatureExtraction()
			throws ResourceInitializationException;

	public File runStep(int step, AnalysisEngineDescription... engines)
			throws ResourceInitializationException, UIMAException, IOException {
		File input;
		if (step <= 1) {
			input = new File(this.getInputDirectoryName());
		} else {
			input =
					new File(this.getWorkingDirectory(),
							String.valueOf(step - 1));
		}
		File output =
				new File(this.getWorkingDirectory(), String.valueOf(step));
		if (!output.exists()) output.mkdirs();

		// if (input.lastModified() <= output.lastModified()) return output;

		// Appending an xmi writer to the array
		AnalysisEngineDescription[] ae =
				Arrays.copyOf(engines, engines.length + 1);
		ae[ae.length - 1] =
				createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						output.getAbsolutePath());

		logger.info("Running step " + step);
		// Run the pipeline
		String inp =
				(input.isDirectory() ? input.getAbsolutePath() + "/*.xmi"
						: input.getAbsolutePath());
		CollectionReader reader =
				createReader(XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						inp);

		SimplePipeline.runPipeline(reader, ae);

		return output;

	}

	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	public String getClassFeatureName() {
		return classFeatureName;
	}

	public void setClassFeatureName(String classFeatureName) {
		this.classFeatureName = classFeatureName;
	}

	public File getExperimentDirectory() {
		File f = new File(getWorkingDirectory(), "experiment");
		if (!f.exists()) f.mkdirs();
		return f;
	}

	public File getArffFile() {
		return new File(getExperimentDirectory(), arffFileName);
	}

	public String getDirectoryName() {
		return getClass().getSimpleName();
	}
}
