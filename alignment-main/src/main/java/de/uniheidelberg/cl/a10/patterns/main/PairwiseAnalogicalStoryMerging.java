package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.Option;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.Util;
import de.uniheidelberg.cl.a10.cluster.measure.DocumentSimilarityMeasure;
import de.uniheidelberg.cl.a10.cluster.measure.DocumentSimilarityMeasureFactory;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.RitualDocument;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentFactory;
import de.uniheidelberg.cl.a10.io.DocumentSimilarityReader;
import de.uniheidelberg.cl.a10.io.DocumentSimilarityWriter;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;
import de.uniheidelberg.cl.a10.patterns.data.EventChainExtractor;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.io.ModelWriter;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;
import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;
import de.uniheidelberg.cl.a10.patterns.train.BMMFactory;
import de.uniheidelberg.cl.a10.patterns.train.BayesianModelMerging;

public class PairwiseAnalogicalStoryMerging extends MainWithInputDocuments {

	@Option(name = "--models",
			usage = "If set, the trained models will be saved in the given directory.")
	File model = new File("models");

	@Option(name = "--nomerging",
			usage = "Initialises a hidden markov model without any merging")
	boolean nomerging = false;

	@Option(name = "--output")
	File output;

	@Option(name = "--cache")
	File cache = new File("cache/storysim");

	@Option(name = "--documentsimilaritytype",
			aliases = { "-ds" },
			usage = "The document similarity measure to use")
	DocumentSimilarityMeasure.Type similarityType = DocumentSimilarityMeasure.Type.ONE;

	@Option(name = "--extraction",
			usage = "Controls the pre-filtering of frames into events")
	protected EventChainExtractor.Extraction extraction = EventChainExtractor.Extraction.ANNOTATEDFRAMES;

	@Option(name = "--id",
			usage = "Identifier prepended to the similarity cache")
	String runId = "";

	// @Option(name = "--data")
	// File dataDirectory = new File("data2/silver");

	@Option(name = "--of")
	Output.Style outputStyle = Output.Style.CSV;

	BMMConfiguration bmmConf = new BMMConfiguration();

	Logger logger;

	public PairwiseAnalogicalStoryMerging() {
		logger = Logger.getAnonymousLogger();
	}

	public static void main(final String[] args) throws SecurityException,
			FrameNotFoundException, FrameElementNotFoundException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Main.initProperties();
		PairwiseAnalogicalStoryMerging asm = new PairwiseAnalogicalStoryMerging();
		asm.processArguments(args, asm.bmmConf);

		asm.run();
	}

	public void writeModel(final SEHiddenMarkovModel_impl<Event> hmm)
			throws IOException {
		OutputStream os = null;
		ModelWriter<Event> mw = null;
		if (this.model != null) {
			try {
				os = new FileOutputStream(File.createTempFile("hmm", ".xml",
						this.model));

				mw = new ModelWriter<Event>(os);
				mw.write(hmm);
			} catch (IOException e) {
				throw e;
			} finally {
				os.close();
				try {
					mw.close();
				} catch (IOException e) {
				}
			}
		}
	}

	protected String getWiki() {
		return bmmConf.getWikiDescription();
	}

	@SuppressWarnings("unchecked")
	public void run() throws SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (!this.cache.exists()) {
			this.cache.mkdirs();
		}
		if (!this.cache.canWrite()) {
			logger.log(Level.SEVERE,
					"Can't write to directory " + this.cache.getPath());
			System.exit(1);
		}
		// ModelManager mm = ModelManager.getModelManager();
		DocumentSimilarityMeasure dsm = new DocumentSimilarityMeasureFactory()
				.getDocumentSimilarityMeasure(similarityType);
		SimilarityFunctionFactory<Event> sfFactory = new SimilarityFunctionFactory<Event>();
		AlignmentFactory<Event> af = new AlignmentFactory<Event>();
		try {
			af.setSimilarity(sfFactory.getSimilarityFunction(bmmConf));
			List<RitualDocument> documents = this.getDocuments();
			Matrix<RitualDocument, RitualDocument, Probability> results = this
					.loadResults();
			MapMatrix<RitualDocument, RitualDocument, Boolean> visited = new MapMatrix<RitualDocument, RitualDocument, Boolean>(
					false);
			EventChainExtractor ece = EventChainExtractor
					.getEventChainExtractor(extraction);
			for (RitualDocument rd0 : documents) {
				List<Event> seq1 = ece.getEventChain(rd0);
				for (RitualDocument rd1 : documents) {
					List<Event> seq2 = ece.getEventChain(rd1);
					Set<String> sourceIds = new HashSet<String>();
					sourceIds.add(rd0.getId());
					sourceIds.add(rd1.getId());
					HiddenMarkovModel_impl<Event> hmm = null;
					Probability p = null;
					if (!visited.get(rd0, rd1) && results.get(rd0, rd1) == null
							&& rd0 != rd1 && seq1.size() > 0 && seq2.size() > 0) {

						// hmm = mm.getHiddenMarkovModel(bmmConf, sourceIds);
						if (hmm == null) {
							logger.log(Level.INFO, "Merging " + rd0.getId()
									+ " and " + rd1.getId());
							hmm = runMerging(Arrays.asList(seq1, seq2));
						}
						Alignment<Token> alignment = af
								.getTokenAlignmentFromHMM(hmm);
						p = Probability.fromProbability(dsm
								.getDocumentSimilarity(alignment));
						visited.put(rd1, rd0, true);
						visited.put(rd0, rd1, true);
						results.put(rd0, rd1, p);
						results.put(rd1, rd0, p);
						storeResults(results);
					}
				}
			}
			PrintStream os = new PrintStream(getOutputStreamForFileOption(
					output, System.out), true, "UTF-8");
			os.println(Output.getOutput(outputStyle).getString(results));
			storeResults(results);
			os.close();
			return;
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	public Matrix<RitualDocument, RitualDocument, Probability> loadResults()
			throws IOException {
		File cacheFile = new File(cache, this.runId
				+ Util.md5(bmmConf.toString()) + this.similarityType + ".xml");
		DocumentSimilarityReader dsr = new DocumentSimilarityReader(new File(
				Main.defaultRitualDataDirectory));
		dsr.addDocumentsDirectory(new File(Main.defaultFableDataDirectory));
		Matrix<RitualDocument, RitualDocument, Probability> m;
		if (cacheFile.exists() && cacheFile.canRead()) {
			logger.info("Loading cached results from "
					+ cacheFile.getAbsolutePath());
			m = dsr.read(cacheFile);
		} else {
			m = new MapMatrix<RitualDocument, RitualDocument, Probability>();
		}
		return m;
	}

	public synchronized void storeResults(
			final Matrix<RitualDocument, RitualDocument, Probability> matrix)
			throws IOException {
		File cacheFile = new File(cache, this.runId
				+ Util.md5(bmmConf.toString()) + this.similarityType + ".xml");
		Matrix<RitualDocument, RitualDocument, Probability> m;
		if (cacheFile.exists()) {
			DocumentSimilarityReader dsr = new DocumentSimilarityReader(
					new File(Main.defaultRitualDataDirectory));
			dsr.addDocumentsDirectory(new File(Main.defaultFableDataDirectory));
			m = dsr.read(cacheFile);
		} else {
			m = matrix;
		}

		for (RitualDocument rd1 : matrix.getRows()) {
			for (RitualDocument rd2 : matrix.getColumns()) {
				m.put(rd1, rd2, matrix.get(rd1, rd2));
			}
		}
		logger.info("Storing results in " + cacheFile.getAbsolutePath());
		OutputStream os = new FileOutputStream(cacheFile);
		DocumentSimilarityWriter dsw = new DocumentSimilarityWriter(os);
		dsw.setConfiguration(bmmConf);
		dsw.write(m);
		dsw.close();
		os.close();

	}

	protected SEHiddenMarkovModel_impl<Event> runMerging(
			final Collection<List<Event>> sequences)
			throws FileNotFoundException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		BMMFactory<Event> trainingFactory = new BMMFactory<Event>();

		BayesianModelMerging<Event> bmm = trainingFactory.getTrainer(bmmConf);
		// Settings
		Level level = Level.parse(this.logLevel.toUpperCase());
		bmm.setLogLevel(level);
		SEHiddenMarkovModel_impl<Event> hmm;
		if (nomerging) {
			hmm = bmm.init(sequences.iterator());
		} else {
			// Training
			hmm = bmm.train(sequences.iterator());
		}
		int i = 0;
		for (File f : getArguments()) {
			hmm.setProperty("source" + (i++), f.getName());
		}
		hmm.setProperty("commandline", commandLine);
		hmm.setTrainingConfiguration(bmmConf);

		try {
			writeModel(hmm);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		/*
		 * int n = 0; for (List<Event> ev : sequences) { n += ev.size(); }
		 */
		return hmm;

		/*
		 * Prior<Event> prior = bmm.getPrior(); Probability r =
		 * PMath.scale(prior.getModelProbabilityBasedOnSize(n),
		 * prior.getModelProbabilityBasedOnSize(3), p); return r;
		 */
	}

}
