package de.nilsreiter.ocr;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.ocr.resources.SubstitutionRules;
import de.nilsreiter.ocr.resources.WordList;
import de.nilsreiter.ocr.uima.OCRCorrectedExport;
import de.nilsreiter.ocr.uima.analysis.FrequencyDictionaryExtractor;
import de.nilsreiter.ocr.uima.detect.HyphenationDetection;
import de.nilsreiter.ocr.uima.detect.InfrequentWordsMarker;
import de.nilsreiter.ocr.uima.detect.OCRErrorDetectionLMBased;
import de.nilsreiter.ocr.uima.detect.OCRErrorDetectionWordList;
import de.nilsreiter.pipeline.uima.ocr.OCRTokenizer;
import de.nilsreiter.pipeline.uima.ocr.fix.EditDistanceFix;
import de.nilsreiter.pipeline.uima.ocr.fix.HyphenationCorrection;
import de.nilsreiter.pipeline.uima.ocr.fix.Substitution;
import de.nilsreiter.util.IOUtil;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class OCRCorrectMain extends MainWithIODir {

	Logger logger = LoggerFactory.getLogger(getClass());

	ExternalResourceDescription wordList;
	ExternalResourceDescription substitutionRules;

	public enum Step {
		Rules, Info, Hyphenation
	};

	@Option(name = "--wordlist", usage = "A file containing a list of words")
	File wordListFile = null;

	@Option(name = "--rules", usage = "A file containing substitution rules")
	File rulesFile = null;

	@Option(name = "--language", usage = "The language")
	String language;

	@Option(name = "--xmi", usage = "Also print XMI files for inspection")
	boolean printxmi;

	@Option(name = "--step", aliases = { "-s" }, usage = "Processing step(s)",
			multiValued = true)
	List<Step> steps = new LinkedList<Step>();

	public static void main(String[] args) throws Exception {
		OCRCorrectMain pm = new OCRCorrectMain();
		pm.processArguments(args);
		pm.initResources();
		pm.run();
	}

	private void initResources() throws MalformedURLException {
		if (wordListFile != null) {
			logger.info("Initialising word list.");
			wordList =
					createExternalResourceDescription(WordList.class,
							wordListFile.toURI().toURL());
		}
		if (rulesFile != null) {
			logger.info("Initialising substitution rules.");
			substitutionRules =
					createExternalResourceDescription(SubstitutionRules.class,
							rulesFile.toURI().toURL());
		}
	}

	public void run() throws IOException, ResourceInitializationException,
			UIMAException {
		File tempdir = IOUtil.createTempDir("pipeline", "");

		int stepNumber = 0;
		for (Step step : steps) {
			File stepdir = new File(tempdir, String.valueOf(stepNumber++));
			switch (step) {
			case Hyphenation:
				fixHyphenation(input, output);
				break;
			case Rules:
				fixRules(input, output);
				break;
			default:
				info(input, output);
			}
		}
		// fixHyphenation(input, stepdir);

		if (steps.contains(Step.Hyphenation)) {}
		if (steps.contains(Step.Rules)) {
			stepNumber++;
		}
	}

	private void info(File input, File output) throws UIMAException,
			IOException {
		CollectionReader cr = this.getCollectionReader(input);

		AnalysisEngineDescription detect =
				createEngineDescription(InfrequentWordsMarker.class);
		List<AnalysisEngineDescription> pl =
				new ArrayList<AnalysisEngineDescription>();
		pl.add(createEngineDescription(OCRTokenizer.class));
		pl.add(createEngineDescription(FrequencyDictionaryExtractor.class));
		pl.add(detect);
		pl.add(createEngineDescription(XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION,
				new File(output, "xmi").getAbsolutePath()));
		runPipeline(cr, pl.toArray(new AnalysisEngineDescription[pl.size()]));

	}

	protected CollectionReader getCollectionReader(File directory)
			throws ResourceInitializationException {
		return createReader(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
				directory.getAbsolutePath() + File.separator + "*.txt",
				TextReader.PARAM_LANGUAGE, language);
	}

	public void fixRules(File inputDirectory, File outputDirectory)
			throws UIMAException, IOException {
		CollectionReader cr = this.getCollectionReader(inputDirectory);

		AnalysisEngineDescription detect =
				createEngineDescription(OCRErrorDetectionWordList.class,
						OCRErrorDetectionWordList.RESOURCE_WORDLIST, wordList,
						OCRErrorDetectionWordList.PARAM_EXCLUDE_UPPER_CASE,
						true,
						OCRErrorDetectionWordList.PARAM_EXCLUDE_PUNCTUATION,
						true);

		List<AnalysisEngineDescription> pl =
				new ArrayList<AnalysisEngineDescription>();
		pl.add(createEngineDescription(OCRTokenizer.class));
		pl.add(detect);
		pl.add(createEngineDescription(Substitution.class,
				Substitution.RESOURCE_WORDLIST, wordList,
				Substitution.RESOURCE_RULES, substitutionRules));
		pl.add(createEngineDescription(OCRCorrectedExport.class,
				OCRCorrectedExport.PARAM_OUTPUT_DIRECTORY,
				outputDirectory.getAbsolutePath()));

		if (printxmi) {
			pl.add(createEngineDescription(XmiWriter.class,
					XmiWriter.PARAM_TARGET_LOCATION, new File(outputDirectory,
							"xmi").getAbsolutePath()));
		}

		runPipeline(cr, pl.toArray(new AnalysisEngineDescription[pl.size()]));
	}

	public void fixHyphenation(File inputDirectory, File outputDirectory)
			throws ResourceInitializationException, UIMAException, IOException {

		runPipeline(
				getCollectionReader(inputDirectory),
				createEngineDescription(HyphenationDetection.class),
				createEngineDescription(HyphenationCorrection.class,
						HyphenationCorrection.RESOURCE_WORDLIST, wordList),
						createEngineDescription(OCRCorrectedExport.class,
								OCRCorrectedExport.PARAM_OUTPUT_DIRECTORY,
								outputDirectory.getAbsolutePath()));
	}

	public AnalysisEngineDescription[] getPipeline()
			throws ResourceInitializationException {

		ExternalResourceDescription wordList =
				createExternalResourceDescription(WordList.class, getClass()
						.getClassLoader().getResource("wordlist.txt")
						.toString());

		AnalysisEngineDescription lmEngine =
				createEngineDescription(
						OCRErrorDetectionLMBased.class,
						OCRErrorDetectionLMBased.PARAM_LM_FILE,
						"/Users/reiterns/Documents/Workspace/a10/de.nilsreiter.lm/target/charactermodel.gz");
		AnalysisEngineDescription editFix =
				createEngineDescription(EditDistanceFix.class,
						EditDistanceFix.RESOURCE_WORDLIST, wordList);
		AnalysisEngineDescription detect =
				createEngineDescription(OCRErrorDetectionWordList.class,
						OCRErrorDetectionWordList.RESOURCE_WORDLIST, wordList,
						OCRErrorDetectionWordList.PARAM_EXCLUDE_UPPER_CASE,
						true);
		return new AnalysisEngineDescription[] {
				createEngineDescription(HyphenationDetection.class),
				createEngineDescription(HyphenationCorrection.class,
						HyphenationCorrection.RESOURCE_WORDLIST, wordList),
						createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION, "target/out/"),
								createEngineDescription(OCRCorrectedExport.class) };
	}

}
