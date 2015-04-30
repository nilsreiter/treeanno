package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import neobio.alignment.IncompatibleScoringSchemeException;

import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.alignment.neobio.AlignmentType;
import de.nilsreiter.alignment.neobio.IndividualAlignment;
import de.nilsreiter.alignment.neobio.PairwiseAlignment;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class TestDoubleNeedlemanWunsch {

	List<String> sequence1;
	List<String> sequence2;

	@Before
	public void setUp() throws Exception {
		sequence1 = Arrays.asList("A", "B", "C", "A");
		sequence2 = Arrays.asList("A", "B", "D", "A");
	}

	@Test
	public void testComputePairwiseAlignment() {
		AdvancedScoringScheme<String> scheme =

				new AdvancedScoringScheme<String>(
						Probability.fromProbability(0.5),
						new SimilarityFunction<String>() {

							@Override
							public Probability sim(final String arg0,
									final String arg1) {
								if (arg0 == "C" && arg1 == "D")
									return Probability.fromProbability(0.6);
								if (arg0.equals(arg1)) return Probability.ONE;
								return Probability.NULL;
							}

							@Override
							public String toString() {
								return "";
							}

							@Override
							public void readConfiguration(final Object tc) {}

						});
		DoubleNeedlemanWunsch<String> algo =
				new DoubleNeedlemanWunsch<String>(scheme);
		algo.setSequences(sequence1, sequence2);
		try {
			PairwiseAlignment<String> alignment =
					algo.computePairwiseAlignment();
			List<IndividualAlignment> at = alignment.getScoreTagLine();
			assertEquals(4, alignment.getScoreTagLine().size());
			assertEquals(AlignmentType.Full, at.get(0).getAlignmentType());
			assertEquals(AlignmentType.Full, at.get(1).getAlignmentType());
			assertEquals(AlignmentType.Partial, at.get(2).getAlignmentType());
			assertEquals(0.6, at.get(2).getScore(), 0.0);
			assertEquals(AlignmentType.Full, at.get(3).getAlignmentType());
		} catch (IncompatibleScoringSchemeException e) {
			fail("This should not happen.");
		}
	}
}
