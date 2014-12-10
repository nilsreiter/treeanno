package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static de.nilsreiter.pipeline.PipelineBuilder.cwb;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.pipeline.semafor.Semafor;
import de.nilsreiter.pipeline.uima.ClearAnnotation;
import de.nilsreiter.pipeline.uima.entitydetection.EntityAnnotator2;
import de.nilsreiter.pipeline.uima.entitydetection.RelationAnnotator;
import de.nilsreiter.pipeline.uima.event.EventAnnotator;
import de.nilsreiter.pipeline.uima.wsd.WSDItemCompleter;
import de.nilsreiter.pipeline.uima.wsd.WSDPostProcess;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordCoreferenceResolver;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
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
	Pipeline part = Pipeline.Ling;

	@Option(name = "--format", aliases = { "-f" },
			usage = "Export data format", multiValued = true)
	List<ExportFormat> exportFormat = new LinkedList<ExportFormat>();

	ExternalResourceDescription wordnet;
	ExternalResourceDescription mfsResource;

	enum Pipeline {
		Basic, Second, Full, Ling, Event, Shallow, SegLemma, StanfordShallow,
		StanfordDeep, NEFinder, EntityRelations
	};

	enum ExportFormat {
		XMI, DATA2, CWB, TXT_NE_FILTER, GEXF
	}

	Logger logger = LoggerFactory.getLogger(PipelineMain.class);

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

		switch (part) {
		default:
			return;
		case Full:
		case Basic:
		case Second:
		case Ling:
			logger.info("Initialising resource: WordNet");
			wordnet =
					createExternalResourceDescription(
							WordNetSynsetSenseInventoryResource.class,
							WordNetSynsetSenseInventoryResource.PARAM_WORDNET_PROPERTIES_URL,
							getConfiguration().getString("paths.extjwnl"),
							WordNetSynsetSenseInventoryResource.PARAM_SENSE_INVENTORY_NAME,
							"WordNet 3.0");

			logger.info("Initialising resource: MFS");
			mfsResource =
					createExternalResourceDescription(
							WSDResourceIndividualPOS.class,
							WSDResourceIndividualPOS.SENSE_INVENTORY_RESOURCE,
							wordnet,
							WSDResourceIndividualPOS.DISAMBIGUATION_METHOD,
							MostFrequentSenseBaseline.class.getName());
		}

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
		if (exportFormat.contains(ExportFormat.CWB))
			pl = cwb(pl, this.getOutputDirectory());
		if (exportFormat.contains(ExportFormat.TXT_NE_FILTER))
			pl = PipelineBuilder.txt(pl, this.getOutputDirectory());
		if (exportFormat.contains(ExportFormat.GEXF))
			pl = PipelineBuilder.gexf(pl, this.getOutputDirectory());
		logger.info("Running pipeline.");
		runPipeline(getXmiCollectionReader(), array(pl));
	}

	public List<AnalysisEngineDescription> getPipeline(Pipeline pl)
			throws ResourceInitializationException {
		ArrayList<AnalysisEngineDescription> ae =
				new ArrayList<AnalysisEngineDescription>();
		switch (pl) {
		case Ling:
			return this.getLingPipeline();
		case Event:
			ae.add(createEngineDescription(ClearAnnotation.class,
					ClearAnnotation.PARAM_TYPE,
					"de.nilsreiter.pipeline.uima.event.type.Role"));
			ae.add(createEngineDescription(ClearAnnotation.class,
					ClearAnnotation.PARAM_TYPE,
					"de.nilsreiter.pipeline.uima.event.type.Event"));
			ae.add(createEngineDescription(EventAnnotator.class,
					EventAnnotator.PARAM_DETECTION_STYLE,
					EventAnnotator.Detection.FNEventInheritance,
					EventAnnotator.PARAM_FNHOME,
					getConfiguration().getString("paths.fnhome")));
			return ae;
		case Full:
			return this.getFullPipeline();
		case SegLemma:
			ae.add(createEngineDescription(StanfordSegmenter.class));
			ae.add(createEngineDescription(StanfordLemmatizer.class));
			return ae;
		case StanfordShallow:
			ae.add(createEngineDescription(LanguageToolSegmenter.class));
			ae.add(createEngineDescription(StanfordPosTagger.class));
			ae.add(createEngineDescription(StanfordLemmatizer.class));
			return ae;
		case EntityRelations:
			ae.add(createEngineDescription(EntityAnnotator2.class));
			ae.add(createEngineDescription(RelationAnnotator.class));
			return ae;
		case StanfordDeep:
			ae.add(createEngineDescription(LanguageToolSegmenter.class));
			ae.add(createEngineDescription(StanfordPosTagger.class));
			ae.add(createEngineDescription(StanfordLemmatizer.class));
			ae.add(createEngineDescription(StanfordNamedEntityRecognizer.class));
			ae.add(createEngineDescription(StanfordParser.class));
			ae.add(createEngineDescription(StanfordCoreferenceResolver.class,
					StanfordCoreferenceResolver.PARAM_POSTPROCESSING, true));
			return ae;
		case Shallow:
			return this.getShallowPipeline();
		case Second:
			return this.getRowlandsonPipeline2();
		case NEFinder:
			ae.add(createEngineDescription(LanguageToolSegmenter.class));
			ae.add(createEngineDescription(StanfordLemmatizer.class));
			ae.add(createEngineDescription(StanfordPosTagger.class));
			ae.add(createEngineDescription(StanfordNamedEntityRecognizer.class));
			return ae;
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
		l.add(createEngineDescription(StanfordNamedEntityRecognizer.class));
		l.add(createEngineDescription(StanfordParser.class,
				StanfordParser.PARAM_MODE,
				StanfordParser.DependenciesMode.BASIC,
				StanfordParser.PARAM_WRITE_CONSTITUENT, true,
				StanfordParser.PARAM_WRITE_POS, false,
				StanfordParser.PARAM_WRITE_LEMMA, false,
				StanfordParser.PARAM_READ_POS, true));
		l.add(createEngineDescription(StanfordCoreferenceResolver.class,
				StanfordCoreferenceResolver.PARAM_POSTPROCESSING, true));
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

	public List<AnalysisEngineDescription> getLingPipeline()
			throws ResourceInitializationException {

		ArrayList<AnalysisEngineDescription> l =
				new ArrayList<AnalysisEngineDescription>();

		l.add(createEngineDescription(StanfordSegmenter.class));
		l.add(createEngineDescription(StanfordLemmatizer.class));
		l.add(createEngineDescription(StanfordPosTagger.class));
		l.add(createEngineDescription(StanfordParser.class,
				StanfordParser.PARAM_MODE,
				StanfordParser.DependenciesMode.BASIC,
				StanfordParser.PARAM_WRITE_CONSTITUENT, true,
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

	public List<AnalysisEngineDescription> getShallowPipeline()
			throws ResourceInitializationException {

		ArrayList<AnalysisEngineDescription> l =
				new ArrayList<AnalysisEngineDescription>();

		l.add(createEngineDescription(LanguageToolSegmenter.class));
		// l.add(createEngineDescription(OpenNlpPosTagger.class));
		return l;
	}
}
