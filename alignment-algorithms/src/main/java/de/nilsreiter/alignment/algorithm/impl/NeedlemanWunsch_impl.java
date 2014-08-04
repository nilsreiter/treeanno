package de.nilsreiter.alignment.algorithm.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import neobio.alignment.IncompatibleScoringSchemeException;
import de.nilsreiter.alignment.algorithm.NeedlemanWunsch;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.AdvancedScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.DoubleNeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.ScoringScheme;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class NeedlemanWunsch_impl<T extends HasDocument> extends
AbstractAlignmentAlgorithm_impl<T> implements NeedlemanWunsch<T> {

	DoubleNeedlemanWunsch<T> dnw;

	public NeedlemanWunsch_impl(double threshold, SimilarityFunction<T> function) {
		this.function = function;
		ScoringScheme<T> scoringScheme =
				new AdvancedScoringScheme<T>(
						Probability.fromProbability(threshold), this.function);
		dnw = new DoubleNeedlemanWunsch<T>(scoringScheme);
	}

	@Override
	public Alignment<T> align(List<T> list1, List<T> list2) {
		dnw.setSequences(list1, list2);
		try {
			return this.fromPairwiseAlignment(dnw.computePairwiseAlignment(),
					list1.get(0).getRitualDocument(), list2.get(0)
							.getRitualDocument());
		} catch (IncompatibleScoringSchemeException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected
			Alignment<T>
	fromPairwiseAlignment(
			final de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignment<T> pa,
			final Document text1, final Document text2) {
		Alignment<T> document = new Alignment_impl<T>("");
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		document.getDocuments().add(text1);
		document.getDocuments().add(text2);
		int length = pa.getScoreTagLine().size();
		for (int i = 0; i < length; i++) {
			T f1 = pa.getGappedSequence1().get(i);
			T f2 = pa.getGappedSequence2().get(i);

			Set<T> aligned = new HashSet<T>();
			if (f1 != null) aligned.add(f1);
			if (f2 != null) aligned.add(f2);
			document.addAlignment(idp.getNextAlignmentId(), aligned).setScore(
					pa.getScoreTagLine().get(i).getScore());
		}
		return document;
	}

}
