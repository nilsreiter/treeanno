package de.uniheidelberg.cl.a10.patterns.main;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import neobio.alignment.IncompatibleScoringSchemeException;

import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.FrameAlignmentDocument;
import de.uniheidelberg.cl.a10.data2.io.TokenAlignmentReader;
import de.uniheidelberg.cl.a10.eval.EvaluationSettings;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluation;
import de.uniheidelberg.cl.a10.eval.impl.Results_impl;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.main.Output.Style;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.AdvancedScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.DoubleNeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.NeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignment;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignmentAlgorithm;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.RecursiveSmithWaterman;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.ScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.SequenceAlignmentConfiguration;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.SmithWaterman;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfigurationIterator;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;

public class SequenceAlignmentExperimenter extends MainWithInputSequences {

	SequenceAlignmentConfiguration sequenceAlignmentConfig = new SequenceAlignmentConfiguration();
	EvaluationSettings evaluationSettings = new EvaluationSettings();
	SimilarityFunction<Event> similarityFunction;
	SimilarityFunctionFactory<Event> factory;
	Results_impl results;

	public static void main(final String[] args) throws Exception {
		SequenceAlignmentExperimenter sae = new SequenceAlignmentExperimenter();
		sae.processArguments(args, sae.sequenceAlignmentConfig,
				sae.evaluationSettings);
		sae.run();
	}

	public void runConfig(final SimilarityConfiguration sc)
			throws SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException,
			IncompatibleScoringSchemeException, ParserConfigurationException,
			SAXException {
		PairwiseAlignmentAlgorithm<Event> algo;
		ScoringScheme<Event> scoringScheme = new AdvancedScoringScheme<Event>(
				Probability
						.fromProbability(this.sequenceAlignmentConfig.threshold),
				factory.getSimilarityFunction(sc));
		switch (this.sequenceAlignmentConfig.algorithm) {
		case SmithWaterman:
			algo = new SmithWaterman<Event>();
			algo.setScoring(scoringScheme);
			break;
		case RecursiveSmithWaterman:
			algo = new RecursiveSmithWaterman<Event>();
			algo.setScoring(scoringScheme);
			break;
		case NeedlemanWunsch:
			algo = new NeedlemanWunsch<Event>(scoringScheme);
			break;
		default:
		case DoubleNeedlemanWunsch:
			algo = new DoubleNeedlemanWunsch<Event>(scoringScheme);
			break;
		}
		AlignmentEvaluation<Token> evaluation = de.uniheidelberg.cl.a10.eval.Evaluation
				.getAlignmentEvaluation(this.evaluationSettings.evaluationStyle);
		TokenAlignmentReader ar = new TokenAlignmentReader(
				this.evaluationSettings.dataDirectory);
		Alignment<Token> goldDocument = ar.read(this.evaluationSettings.gold);

		for (int i = 0; i < getSequences().size(); i++) {
			List<Event> seq1 = getSequences().get(i);
			for (int j = i + 1; j < getSequences().size(); j++) {
				List<Event> seq2 = getSequences().get(j);
				String pairString = seq1.get(0).getRitualDocument().getId()
						+ "," + seq2.get(0).getRitualDocument().getId();
				algo.setSequences(seq1, seq2);
				PairwiseAlignment<Event> alignment = algo
						.computePairwiseAlignment();
				Alignment<Token> doc = new EventTokenConverter()
						.convert(FrameAlignmentDocument.fromPairwiseAlignment(
								alignment, seq1.get(0).getRitualDocument(),
								seq2.get(0).getRitualDocument()));
				results.addResult(evaluation.evaluate(goldDocument, doc,
						pairString + " " + sc.getWikiDescription()));
			}
		}

		for (List<Event> seq1 : getSequences()) {
			for (List<Event> seq2 : getSequences()) {
				if (seq1 != seq2) {

				}
			}
		}
	}

	public void run() throws ParserConfigurationException, SAXException,
			IOException, IncompatibleScoringSchemeException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		results = new Results_impl();
		factory = new SimilarityFunctionFactory<Event>();
		SimilarityConfigurationIterator scIter = new SimilarityConfigurationIterator(
				this.sequenceAlignmentConfig);
		scIter.setInterestingFields();
		while (scIter.hasNext()) {
			runConfig(scIter.next());
		}
		Output output = Output.getOutput(Style.CSV);
		System.out.println(output.getString(results));
	}

}
