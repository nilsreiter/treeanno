package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.flatformat.FlatFormat;

public class TestFlatFormat extends TestBase15 {

	static String example1 = System.getProperty("user.dir")
			+ "/data/FlatFormatExample.txt";

	public void testCorpusData() {
		try {
			CorpusReader cr =
					new FlatFormat(frameNet, Logger.getAnonymousLogger());
			cr.parse(new File(example1));
			assertEquals(1, cr.getSentences().size());
			assertEquals(9, cr.getSentences().get(0).getTokenList().size());
			assertEquals(4, cr.getRealizedFrames().size());
			// assertEquals("Gizmo", cr.getRealizedFrames().get(3).getFrame()
			// .getName());
			// assertEquals("Libya", cr.getSentences().get(0).getToken(
			// new Range(0, 5)).toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		} catch (FrameElementNotFoundException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

}
