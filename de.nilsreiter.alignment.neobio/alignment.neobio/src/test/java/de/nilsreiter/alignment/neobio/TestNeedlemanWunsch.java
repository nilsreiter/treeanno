package de.nilsreiter.alignment.neobio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
	public void setUp() throws Exception {}

	@Test
	public void testComputePairwiseAlignment() {
		sequence1 = Arrays.asList("A", "B", "C", "A");
		sequence2 = Arrays.asList("A", "B", "D", "A");

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

	@Test
	public void testGappedAlignment() throws IncompatibleScoringSchemeException {
		sequence1 = Arrays.asList("A", "B", "D");
		sequence2 = Arrays.asList("A", "B", "C", "D");
		ScoringScheme<String> scheme =
				new BasicScoringScheme<String>(2, -1, -1);
		PairwiseAlignmentAlgorithm<String> algo =
				new NeedlemanWunsch<String>(scheme);
		algo.setSequences(sequence1, sequence2);
		PairwiseAlignment<String> alignment = algo.computePairwiseAlignment();
		assertEquals(4, alignment.getGappedSequence1().size());
		assertEquals(4, alignment.getGappedSequence2().size());
		assertNull(alignment.getGappedSequence1().get(2));
		assertNotNull(alignment.getGappedSequence2().get(2));
		assertEquals("B", alignment.getGappedSequence1().get(1));
		assertEquals("B", alignment.getGappedSequence2().get(1));

		assertEquals(3, alignment.getIndexMap1().size());
		assertEquals(0, (int) alignment.getIndexMap1().get(0));
		assertEquals(1, (int) alignment.getIndexMap1().get(1));
		assertEquals(3, (int) alignment.getIndexMap1().get(2));

		assertEquals(3, alignment.getIndexMap2().size());
		assertEquals(0, (int) alignment.getIndexMap2().get(0));
		assertEquals(1, (int) alignment.getIndexMap2().get(1));
		assertEquals(2, (int) alignment.getIndexMap2().get(3));
	}
}
