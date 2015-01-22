package de.nilsreiter.segmentation.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.morphadorner.TextTilingAnnotator;
import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.uima.ClearAnnotation;
import de.nilsreiter.segmentation.evaluation.Metric;
import de.nilsreiter.segmentation.evaluation.MetricFactory;
import de.nilsreiter.segmentation.evaluation.WindowDifference;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TextTilingExperiment extends Experiment {

	public TextTilingExperiment(File wDir) {
		super(wDir);
	}

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		TextTilingExperiment tte =
				new TextTilingExperiment(new File(
						"/Users/reiterns/Documents/SegNarr/Experiments"));
		tte.setInputDirectoryName("/Users/reiterns/Documents/Workspace/Segmentation Corpus/corpus/infinite_jest_annotated.txt.xmi");

		System.exit(tte.run());
	}

	public int run() throws ResourceInitializationException, UIMAException,
			IOException {
		int step = 1;
		// runStep(step, getInitialization());
		step++;
		// runStep(step, getProcessing());
		step++;
		runStep(step, getSegmentation());

		Map<String, Double> scores =
				runEvaluation(step, MetricFactory.getMetric(
						WindowDifference.class, SegmentBoundary.class));
		for (String key : scores.keySet()) {
			System.out.println(key + ": " + scores.get(key));
		}
		return step;

	}

	public Map<String, Double> runEvaluation(int step, Metric metric)
			throws UIMAException, IOException {
		File silverDirectory =
				new File(this.getWorkingDirectory(), String.valueOf(step));
		File goldDirectory;
		File originalInputDirectory = new File(this.getInputDirectoryName());
		if (originalInputDirectory.isDirectory())
			goldDirectory = originalInputDirectory;
		else
			goldDirectory = originalInputDirectory.getParentFile();

		Map<String, Double> scores = new HashMap<String, Double>();
		for (File silverFile : silverDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		})) {
			File goldFile = new File(goldDirectory, silverFile.getName());
			if (goldFile.exists() && goldFile.canRead()) {
				TypeSystemDescription tsd =
						TypeSystemDescriptionFactory
								.createTypeSystemDescription();
				JCas silverJCas =
						JCasFactory.createJCas(silverFile.getAbsolutePath(),
								tsd);
				JCas goldJCas =
						JCasFactory.createJCas(goldFile.getAbsolutePath(), tsd);
				double score = metric.score(goldJCas, silverJCas);
				scores.put(goldFile.getName(), score);
			}
		}
		return scores;
	}

	protected AnalysisEngine[] getSegmentation()
			throws ResourceInitializationException {
		return new AnalysisEngine[] {
				createEngine(SegmentationUnitAnnotator.class),
				createEngine(TextTilingAnnotator.class) };
	}

	protected AnalysisEngine[] getInitialization()
			throws ResourceInitializationException {
		return new AnalysisEngine[] {
				createEngine(ClearAnnotation.class, ClearAnnotation.PARAM_TYPE,
						SegmentBoundary.class.getCanonicalName()),
				createEngine(ClearAnnotation.class, ClearAnnotation.PARAM_TYPE,
						Segment.class.getCanonicalName()) };
	}

	protected AnalysisEngine[] getProcessing()
			throws ResourceInitializationException {
		return new AnalysisEngine[] { createEngine(StanfordSegmenter.class),
				createEngine(StanfordLemmatizer.class),
				createEngine(StanfordPosTagger.class),
				createEngine(StanfordNamedEntityRecognizer.class) };
	}

	public File runStep(int step, AnalysisEngine... engines)
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
		AnalysisEngine[] ae = Arrays.copyOf(engines, engines.length + 1);
		ae[ae.length - 1] =
				createEngine(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
						output.getAbsolutePath());

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
}
