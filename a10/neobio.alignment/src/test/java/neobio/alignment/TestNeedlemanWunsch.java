package neobio.alignment;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TestNeedlemanWunsch {
	NeedlemanWunsch nw;

	@Test
	public void testNeedlemanWunsch() throws IOException,
			InvalidSequenceException, IncompatibleScoringSchemeException {
		nw = new NeedlemanWunsch();
		nw.setScoringScheme(new BasicScoringScheme(2, -1, -1));
		nw.loadSequences(new StringReader("ABCD"), new StringReader("ABC"));

		PairwiseAlignment pa = nw.getPairwiseAlignment();
		assertNotNull(pa);
	}
}
