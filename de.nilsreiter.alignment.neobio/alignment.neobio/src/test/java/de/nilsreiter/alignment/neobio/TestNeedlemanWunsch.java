package de.nilsreiter.alignment.neobio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import neobio.alignment.IncompatibleScoringSchemeException;

import org.junit.Before;
import org.junit.Test;

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
		ScoringScheme<String> scheme =
				new BasicScoringScheme<String>(2, -1, -1);
		PairwiseAlignmentAlgorithm<String> algo =
				new NeedlemanWunsch<String>(scheme);
		algo.setSequences(sequence1, sequence2);
		try {
			PairwiseAlignment<String> alignment =
					algo.computePairwiseAlignment();
			List<IndividualAlignment> at = alignment.getScoreTagLine();
			assertEquals(4, alignment.getScoreTagLine().size());
			assertEquals(AlignmentType.Full, at.get(0).getAlignmentType());
			assertEquals(AlignmentType.Full, at.get(1).getAlignmentType());
			assertEquals(AlignmentType.None, at.get(2).getAlignmentType());
			assertEquals(AlignmentType.Full, at.get(3).getAlignmentType());
		} catch (IncompatibleScoringSchemeException e) {
			fail("This should not happen.");
		}

	}
}
