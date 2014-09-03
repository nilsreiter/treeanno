package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static de.nilsreiter.pipeline.PipelineBuilder.data2;
import static de.nilsreiter.pipeline.PipelineBuilder.xmi;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.kohsuke.args4j.Option;

import de.nilsreiter.pipeline.semafor.Semafor;
import de.nilsreiter.pipeline.uima.event.EventAnnotator;
import de.nilsreiter.pipeline.uima.wsd.WSDItemCompleter;
import de.nilsreiter.pipeline.uima.wsd.WSDPostProcess;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
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
import de.uniheidelberg.cl.a10.MainWithIODir;

public class PipelineMain extends MainWithIODir {

	@Option(name = "--part", aliases = { "-p" }, usage = "The pipeline to use")
	Pipeline part = Pipeline.Basic;

	@Option(name = "--format", aliases = { "-f" },
			usage = "Export data format", multiValued = true)
	List<ExportFormat> exportFormat = new LinkedList<ExportFormat>();

	ExternalResourceDescription wordnet;
	ExternalResourceDescription mfsResource;

	enum Pipeline {
		Basic, Second, Full, Event
	};

	enum ExportFormat {
		XMI, DATA2
	}

	public static void main(String[] args) throws Exception {
		PipelineMain pm = new PipelineMain();
		pm.processArguments(args);
		if (pm.exportFormat.isEmpty()) {
			pm.exportFormat.add(ExportFormat.XMI);
		}
		pm.initResources();
		pm.run();
	}

	public void initResources() {
		wordnet =
				createExternalResourceDescription(
						WordNetSynsetSenseInventoryResource.class,
						WordNetSynsetSenseInventoryResource.PARAM_WORDNET_PROPERTIES_URL,
						getConfiguration().getString("paths.extjwnl"),
						WordNetSynsetSenseInventoryResource.PARAM_SENSE_INVENTORY_NAME,
						"WordNet 3.0");
		mfsResource =
				createExternalResourceDescription(
						WSDResourceIndividualPOS.class,
						WSDResourceIndividualPOS.SENSE_INVENTORY_RESOURCE,
						wordnet,
						WSDResourceIndividualPOS.DISAMBIGUATION_METHOD,
						MostFrequentSenseBaseline.class.getName());
	}

	public CollectionReader getXmiCollectionReader()
			throws ResourceInitializationException {
		return createReader(XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
				this.input.getAbsolutePath() + File.separator + "*.xmi");
	}

	void run() throws Exception {

		List<AnalysisEngineDescription> pl = this.getPipeline(part);
		if (exportFormat.contains(ExportFormat.XMI))
			pl = xmi(pl, this.getOutputDirectory());
		if (exportFormat.contains(ExportFormat.DATA2))
			pl = data2(pl, this.getOutputDirectory());
		runPipeline(getXmiCollectionReader(), array(pl));
	}

	public List<AnalysisEngineDescription> getPipeline(Pipeline pl)
			throws ResourceInitializationException {
		switch (pl) {
		case Event:
			ArrayList<AnalysisEngineDescription> ae =
			new ArrayList<AnalysisEngineDescription>();
			ae.add(createEngineDescription(EventAnnotator.class));
			return ae;
		case Full:
			return this.getFullPipeline();
		case Second:
			return this.getRowlandsonPipeline2();
		default:
			return this.getBasicRowlandsonPipeline();
		}
	}

	public List<AnalysisEngineDescription> getFullPipeline()
			throws ResourceInitializationException {

		ArrayList<AnalysisEngineDescription> l =
				new ArrayList<AnalysisEngineDescription>();

		l.add(createEngineDescription(StanfordSegmenter.class));
		l.add(createEngineDescription(StanfordLemmatizer.class));
		l.add(createEngineDescription(StanfordPosTagger.class));
		l.add(createEngineDescription(StanfordParser.class,
				StanfordParser.PARAM_MODE,
				StanfordParser.DependenciesMode.BASIC,
				StanfordParser.PARAM_WRITE_CONSTITUENT, false,
				StanfordParser.PARAM_WRITE_POS, false,
				StanfordParser.PARAM_WRITE_LEMMA, false,
				StanfordParser.PARAM_READ_POS, true));
		l.add(createEngineDescription(StanfordCoreferenceResolver.class));
		l.add(createEngineDescription(WSDItemAnnotator.class,
				WSDItemAnnotator.PARAM_FEATURE_PATH,
				"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN"));
		l.add(createEngineDescription(WSDItemCompleter.class));
		l.add(createEngineDescription(WSDAnnotatorIndividualPOS.class,
				WSDAnnotatorIndividualPOS.WSD_ALGORITHM_RESOURCE, mfsResource,
				WSDAnnotatorIndividualPOS.PARAM_DISAMBIGUATION_METHOD_NAME,
				MostFrequentSenseBaseline.class.getName()));
		l.add(createEngineDescription(WSDPostProcess.class));
		l.add(createEngineDescription(Semafor.class, Semafor.PARAM_MODEL,
				getConfiguration().getString("Semafor.model"),
				Semafor.PARAM_EXCLUDE_PUNCTUATION, false));
		l.add(createEngineDescription(EventAnnotator.class));

		return l;
	}

	public List<AnalysisEngineDescription> getRowlandsonPipeline2()
			throws ResourceInitializationException {
		ArrayList<AnalysisEngineDescription> l =
				new ArrayList<AnalysisEngineDescription>();
		l.add(createEngineDescription(Semafor.class, Semafor.PARAM_MODEL,
				"/Users/reiterns/Documents/Resources/semafor-model/0",
				Semafor.PARAM_EXCLUDE_PUNCTUATION, false));
		l.add(createEngineDescription(EventAnnotator.class));
		return l;
	}

	public List<AnalysisEngineDescription> getBasicRowlandsonPipeline()
			throws ResourceInitializationException {

		AnalysisEngineDescription wsd =
				createEngineDescription(
						WSDAnnotatorIndividualPOS.class,
						WSDAnnotatorIndividualPOS.WSD_ALGORITHM_RESOURCE,
						mfsResource,
						WSDAnnotatorIndividualPOS.PARAM_DISAMBIGUATION_METHOD_NAME,
						MostFrequentSenseBaseline.class.getName());

		ArrayList<AnalysisEngineDescription> l =
				new ArrayList<AnalysisEngineDescription>();

		l.add(createEngineDescription(StanfordSegmenter.class));
		l.add(createEngineDescription(StanfordLemmatizer.class));
		l.add(createEngineDescription(StanfordPosTagger.class));
		l.add(createEngineDescription(StanfordParser.class,
				StanfordParser.PARAM_MODE,
				StanfordParser.DependenciesMode.BASIC,
				StanfordParser.PARAM_WRITE_CONSTITUENT, false,
				StanfordParser.PARAM_WRITE_POS, false,
				StanfordParser.PARAM_WRITE_LEMMA, false,
				StanfordParser.PARAM_READ_POS, true));
		l.add(createEngineDescription(StanfordCoreferenceResolver.class));
		l.add(createEngineDescription(WSDItemAnnotator.class,
				WSDItemAnnotator.PARAM_FEATURE_PATH,
				"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN"));
		l.add(createEngineDescription(WSDItemCompleter.class));
		l.add(wsd);
		l.add(createEngineDescription(WSDPostProcess.class));

		return l;

	}
}
