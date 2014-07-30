package de.nilsreiter.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.kohsuke.args4j.Option;

import de.nilsreiter.pipeline.uima.Data2Exporter;
import de.nilsreiter.pipeline.uima.event.EventAnnotator;
import de.nilsreiter.pipeline.uima.wsd.WSDItemCompleter;
import de.tudarmstadt.ukp.dkpro.core.clearnlp.ClearNlpSemanticRoleLabeler;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordCoreferenceResolver;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.wsd.algorithm.MostFrequentSenseBaseline;
import de.tudarmstadt.ukp.dkpro.wsd.annotator.WSDAnnotatorIndividualPOS;
import de.tudarmstadt.ukp.dkpro.wsd.candidates.WSDItemAnnotator;
import de.tudarmstadt.ukp.dkpro.wsd.resource.WSDResourceIndividualPOS;
import de.tudarmstadt.ukp.dkpro.wsd.si.wordnet.resource.WordNetSynsetSenseInventoryResource;
import de.uniheidelberg.cl.a10.MainWithIO;

public class PipelineMain extends MainWithIO {

	@Option(name = "--corpusName")
	String corpusName;

	public static void main(String[] args) throws Exception {
		PipelineMain pm = new PipelineMain();
		pm.processArguments(args);
		pm.run();
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
		AnalysisEngineDescription wsd =
				createEngineDescription(
						WSDAnnotatorIndividualPOS.class,
						WSDAnnotatorIndividualPOS.WSD_ALGORITHM_RESOURCE,
						mfsResource,
						WSDAnnotatorIndividualPOS.PARAM_DISAMBIGUATION_METHOD_NAME,
						MostFrequentSenseBaseline.class.getName());
		return new AnalysisEngineDescription[] {
				createEngineDescription(StanfordSegmenter.class),
				createEngineDescription(StanfordLemmatizer.class),
				createEngineDescription(StanfordPosTagger.class),
				createEngineDescription(StanfordParser.class),
				createEngineDescription(StanfordCoreferenceResolver.class),
				createEngineDescription(WSDItemAnnotator.class,
						WSDItemAnnotator.PARAM_FEATURE_PATH,
						"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN"),
				createEngineDescription(WSDItemCompleter.class),
				wsd,
				createEngineDescription(ClearNlpSemanticRoleLabeler.class),
				createEngineDescription(EventAnnotator.class),
				createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION, "target/xmi"),
				createEngineDescription(Data2Exporter.class,
						Data2Exporter.PARAM_OUTPUT_DIRECTORY, "target/data2") };
	}

	public CollectionReader getCollectionReader()
			throws ResourceInitializationException {
		return createReader(
				TextReader.class,
				TextReader.PARAM_SOURCE_LOCATION,
				this.input.getAbsolutePath() + File.separator + "*.oneline.txt",
				TextReader.PARAM_LANGUAGE, "en");
	}

	void run() throws Exception {
		runPipeline(getCollectionReader(), getPipeline());

	}
}
