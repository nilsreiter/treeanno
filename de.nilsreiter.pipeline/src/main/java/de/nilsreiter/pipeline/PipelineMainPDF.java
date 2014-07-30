package de.nilsreiter.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.uima.OCRCorrectedExport;
import de.nilsreiter.pipeline.uima.PdfReader;
import de.nilsreiter.pipeline.uima.ocr.OCRErrorAnalysis;
import de.nilsreiter.pipeline.uima.ocr.OCRTokenizer;
import de.nilsreiter.pipeline.uima.ocr.SubstitutionRules;
import de.nilsreiter.pipeline.uima.ocr.WordList;
import de.nilsreiter.pipeline.uima.ocr.detect.HyphenationDetection;
import de.nilsreiter.pipeline.uima.ocr.detect.OCRErrorDetection;
import de.nilsreiter.pipeline.uima.ocr.detect.OCRErrorDetectionLMBased;
import de.nilsreiter.pipeline.uima.ocr.fix.EditDistanceFix;
import de.nilsreiter.pipeline.uima.ocr.fix.HyphenationCorrection;
import de.nilsreiter.pipeline.uima.ocr.fix.Substitution;
import de.nilsreiter.util.IOUtil;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.wsd.algorithm.MostFrequentSenseBaseline;
import de.tudarmstadt.ukp.dkpro.wsd.annotator.WSDAnnotatorIndividualPOS;
import de.tudarmstadt.ukp.dkpro.wsd.resource.WSDResourceIndividualPOS;
import de.tudarmstadt.ukp.dkpro.wsd.si.wordnet.resource.WordNetSynsetSenseInventoryResource;
import de.uniheidelberg.cl.a10.MainWithIO;

public class PipelineMainPDF extends MainWithIO {

	ExternalResourceDescription wordList;
	ExternalResourceDescription substitutionRules;

	public PipelineMainPDF() {
		wordList =
				createExternalResourceDescription(WordList.class, getClass()
						.getClassLoader().getResource("wordlist.txt")
						.toString());
		substitutionRules =
				createExternalResourceDescription(SubstitutionRules.class,
						getClass().getClassLoader().getResource("rules.txt")
						.toString());
	}

	public static void main(String[] args) throws Exception {
		PipelineMainPDF pm = new PipelineMainPDF();
		pm.processArguments(args);

		pm.run();
	}

	public void run() throws IOException, ResourceInitializationException,
			UIMAException {
		File tempdir = IOUtil.createTempDir("pipeline", "");
		fixHyphenation(input, tempdir);
		fixRules(tempdir, output);
	}

	public void fixRules(File inputDirectory, File outputDirectory)
			throws UIMAException, IOException {
		CollectionReader cr =
				createReader(TextReader.class,
						TextReader.PARAM_SOURCE_LOCATION,
						inputDirectory.getAbsolutePath() + File.separator
								+ "*.txt");

		AnalysisEngineDescription detect =
				createEngineDescription(OCRErrorDetection.class,
						OCRErrorDetection.RESOURCE_WORDLIST, wordList,
						OCRErrorDetection.PARAM_EXCLUDE_UPPER_CASE, true);

		AnalysisEngineDescription[] pl =
				new AnalysisEngineDescription[] {
				createEngineDescription(OCRTokenizer.class),
				detect,
				createEngineDescription(OCRErrorAnalysis.class),
				createEngineDescription(Substitution.class,
						Substitution.RESOURCE_WORDLIST, wordList,
						Substitution.RESOURCE_RULES, substitutionRules),
						createEngineDescription(OCRCorrectedExport.class,
								OCRCorrectedExport.PARAM_OUTPUT_DIRECTORY,
								outputDirectory.getAbsolutePath()),
								createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION, "target/xmi") };

		runPipeline(cr, pl);
	}

	public void fixHyphenation(File inputDirectory, File outputDirectory)
			throws ResourceInitializationException, UIMAException, IOException {

		runPipeline(
				createReader(PdfReader.class, PdfReader.PARAM_SOURCE_LOCATION,
						inputDirectory.getAbsolutePath() + File.separator
						+ "*.pdf", PdfReader.PARAM_LANGUAGE, "en"),
				createEngineDescription(HyphenationDetection.class),
				createEngineDescription(HyphenationCorrection.class,
						HyphenationCorrection.RESOURCE_WORDLIST, wordList),
				createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION, "target/out/"),
				createEngineDescription(OCRCorrectedExport.class,
												OCRCorrectedExport.PARAM_OUTPUT_DIRECTORY,
												outputDirectory.getAbsolutePath()));
	}

	public AnalysisEngineDescription[] getPipeline()
			throws ResourceInitializationException {
		ExternalResourceDescription wordnet =
				createExternalResourceDescription(
						WordNetSynsetSenseInventoryResource.class,
						WordNetSynsetSenseInventoryResource.PARAM_WORDNET_PROPERTIES_URL,
						getClass().getClassLoader()
								.getResource("extjwnl_properties.xml")
								.toString(),
						WordNetSynsetSenseInventoryResource.PARAM_SENSE_INVENTORY_NAME,
						"WordNet 3.0");
		ExternalResourceDescription mfsResource =
				createExternalResourceDescription(
						WSDResourceIndividualPOS.class,
						WSDResourceIndividualPOS.SENSE_INVENTORY_RESOURCE,
						wordnet,
						WSDResourceIndividualPOS.DISAMBIGUATION_METHOD,
						MostFrequentSenseBaseline.class.getName());

		ExternalResourceDescription wordList =
				createExternalResourceDescription(WordList.class, getClass()
						.getClassLoader().getResource("wordlist.txt")
						.toString());

		AnalysisEngineDescription wsd =
				createEngineDescription(
						WSDAnnotatorIndividualPOS.class,
						WSDAnnotatorIndividualPOS.WSD_ALGORITHM_RESOURCE,
						mfsResource,
						WSDAnnotatorIndividualPOS.PARAM_DISAMBIGUATION_METHOD_NAME,
						MostFrequentSenseBaseline.class.getName());
		AnalysisEngineDescription lmEngine =
				createEngineDescription(
						OCRErrorDetectionLMBased.class,
						OCRErrorDetectionLMBased.PARAM_LM_FILE,
						"/Users/reiterns/Documents/Workspace/a10/de.nilsreiter.lm/target/charactermodel.gz");
		AnalysisEngineDescription editFix =
				createEngineDescription(EditDistanceFix.class,
						EditDistanceFix.RESOURCE_WORDLIST, wordList);
		AnalysisEngineDescription detect =
				createEngineDescription(OCRErrorDetection.class,
						OCRErrorDetection.RESOURCE_WORDLIST, wordList,
						OCRErrorDetection.PARAM_EXCLUDE_UPPER_CASE, true);
		return new AnalysisEngineDescription[] {
				createEngineDescription(HyphenationDetection.class),
				createEngineDescription(HyphenationCorrection.class,
						HyphenationCorrection.RESOURCE_WORDLIST, wordList),
				createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION, "target/out/"),
				createEngineDescription(OCRCorrectedExport.class) };
	}

}
