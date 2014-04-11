package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import neobio.alignment.IncompatibleScoringSchemeException;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.AdvancedScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.AlignmentType;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.BasicScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.DoubleNeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.IndividualAlignment;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.NeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignment;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignmentAlgorithm;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.ScoringScheme;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class TestNeedlemanWunsch {

	List<String> sequence1;
	List<String> sequence2;

	@Before
	public void setUp() throws Exception {
		sequence1 = Arrays.asList("A", "B", "C", "A");
		sequence2 = Arrays.asList("A", "B", "D", "A");
	}

	@Test
	public void testComputePairwiseAlignment() {
		ScoringScheme<String> scheme = new BasicScoringScheme<String>(2, -1, -1);
		PairwiseAlignmentAlgorithm<String> algo = new NeedlemanWunsch<String>(
				scheme);
		algo.setSequences(sequence1, sequence2);
		try {
			PairwiseAlignment<String> alignment = algo
					.computePairwiseAlignment();
			List<IndividualAlignment> at = alignment.getScoreTagLine();
			assertEquals(4, alignment.getScoreTagLine().size());
			assertEquals(AlignmentType.Full, at.get(0).getAlignmentType());
			assertEquals(AlignmentType.Full, at.get(1).getAlignmentType());
			assertEquals(AlignmentType.None, at.get(2).getAlignmentType());
			assertEquals(AlignmentType.Full, at.get(3).getAlignmentType());
		} catch (IncompatibleScoringSchemeException e) {
			fail("This should not happen.");
		}

		scheme = new AdvancedScoringScheme<String>(
				Probability.fromProbability(0.5),
				new SimilarityFunction<String>() {

					@Override
					public Probability sim(final String arg0, final String arg1) {
						if (arg0 == "C" && arg1 == "D")
							return Probability.fromProbability(0.6);
						if (arg0.equals(arg1))
							return Probability.ONE;
						return Probability.NULL;
					}

					@Override
					public String toString() {
						return "";
					}

					@Override
					public void readConfiguration(
							final SimilarityConfiguration tc) {
					}

				});
		algo = new DoubleNeedlemanWunsch<String>(scheme);
		algo.setSequences(sequence1, sequence2);
		try {
			PairwiseAlignment<String> alignment = algo
					.computePairwiseAlignment();
			List<IndividualAlignment> at = alignment.getScoreTagLine();
			assertEquals(4, alignment.getScoreTagLine().size());
			assertEquals(AlignmentType.Full, at.get(0).getAlignmentType());
			assertEquals(AlignmentType.Full, at.get(1).getAlignmentType());
			assertEquals(AlignmentType.Partial, at.get(2).getAlignmentType());
			assertEquals(1.2, at.get(2).getScore(), 0.0);
			assertEquals(AlignmentType.Full, at.get(3).getAlignmentType());
		} catch (IncompatibleScoringSchemeException e) {
			fail("This should not happen.");
		}
	}
}
