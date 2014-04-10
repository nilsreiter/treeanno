/**
 * 
 */
package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElement;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNetRelation;
import de.saar.coli.salsa.reiter.framenet.FrameNetVersion;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.SemanticType;
import de.saar.coli.salsa.reiter.framenet.SemanticTypeNotFoundException;

/**
 * @author reiter
 * 
 */
public class TestFrameNet15 extends TestBase15 {

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();

	/**
	 * @throws java.lang.Exception
	 */

	@Test
	public final void testLoading() {
		FrameNet fn = new FrameNet();
		try {
			assertTrue(fn.readData(FNDatabaseReader.createInstance(new File(
					fnhome), FrameNetVersion.V15)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		}
		fn = null;
		System.gc();
	}

	/*
	 * @Ignore @Test public final void testRemoteLoading() { String baseURI =
	 * "http://www.cl.uni-heidelberg.de/~reiter/FrameNetAPI/"; FrameNet fn = new
	 * FrameNet(); try { assertTrue(fn.readData(new FNDatabaseReader( new
	 * URI(baseURI + "frXML/frames.xml"), new URI(baseURI +
	 * "frXML/frRelation.xml"), new URI(baseURI + "frDiffXML/framesDiff.xml"),
	 * new URI(baseURI + "frXML/semtypes.xml")))); assertEquals(795,
	 * fn.getFrames().size()); } catch (URISyntaxException e) {
	 * e.printStackTrace(); } }
	 */

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getFrameElement(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetFrameElement() {
		try {
			FrameElement fe = frameNet.getFrameElement("Attack.Assailant");
			assertEquals("Assailant", fe.getName());
			assertNotSame("Victim", fe.getName());
			assertEquals("Attack", fe.getFrame().getName());
			assertNotSame("Getting", fe.getFrame().getName());
		} catch (ParsingException e) {
			e.printStackTrace();
			fail();
		} catch (FrameElementNotFoundException e) {
			e.printStackTrace();
			fail();
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getFrame(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetFrame() {
		try {
			Frame frame = frameNet.getFrame("Attack");
			assertEquals("Attack", frame.getName());
			assertEquals("11/22/2002 02:05:22 PST Fri", frameNet
					.getDateFormat().format(frame.getCreationDate()));
			assertEquals("424", frame.getIdString());
			assertEquals(20, frame.getFrameElements().size());
			assertEquals(30, frame.getLexicalUnits().size());
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#allFrames()}.
	 */
	@Test
	public final void testAllFrames() {
		assertEquals(1019, frameNet.getFrames().size());
	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getRootFrames()}.
	 */
	@Test
	public final void testGetRootFrames() {
		assertEquals(446, frameNet.getRootFrames().size());
	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getFrameNetRelations()}
	 * .
	 */

	@Test
	public final void testGetFrameNetRelations() {
		assertEquals(9, frameNet.getFrameNetRelations().size());
	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getFrameNetRelation(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetFrameNetRelation() {
		FrameNetRelation frel = null;
		frel = frameNet.getFrameNetRelation("Inheritance");
		assertNotNull(frel);
		assertEquals("Child", frel.getSubName());
		assertEquals("Parent", frel.getSuperName());
		assertEquals(617, frel.getFrameRelations().size());
		try {
			assertEquals(1, frel.getSuper(frameNet.getFrame("Activity")).size());
			assertEquals(5, frameNet.getFrame("Attack").allInheritedFrames()
					.size());
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getLexicalUnits(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetLexicalUnitsString() {
		assertEquals(1, frameNet.getLexicalUnits("hand", "n").size());
		assertEquals(2, frameNet.getLexicalUnits("hand").size());
		LexicalUnit lu = frameNet.getLexicalUnits("hand", "n").iterator()
				.next();
		assertEquals("hand", lu.getLexemes().get(0).getValue());
		assertEquals("Observable_body_parts", lu.getFrame().getName());
		assertEquals("Finished_Initial", lu.getStatus());
		assertEquals("2409", lu.getIdString());
		assertEquals(1, frameNet.getLexicalUnits("possession_of_goods", "n")
				.size());
		assertEquals(990782669000L, lu.getCreationDate().getTime());
		lu = frameNet.getLexicalUnits("possession_of_goods", "n").iterator()
				.next();
		assertEquals(1, lu.getLexemes().size());
		assertEquals("possession", lu.getLexemes().iterator().next().getValue());
		assertEquals("possession", lu.getLexemeString());
		lu = frameNet.getLexicalUnits("hand_over").iterator().next();
		assertEquals(2, lu.getLexemes().size());
		assertEquals("hand over", lu.getLexemeString());
	}

	/**
	 * Test method for
	 * {@link de.saar.coli.salsa.reiter.framenet.FrameNet#getSemanticType(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetSemanticTypeString() {
		try {
			SemanticType st = frameNet.getSemanticType("Sentient");
			assertEquals("Sentient", st.getName());
			assertEquals(561, st.getFrameElements().size());
			assertEquals(1, st.getSuperTypes().size());
			assertEquals(1, st.getSubTypes().size());
		} catch (SemanticTypeNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for all the aliasing functions.
	 * 
	 * @throws FrameNotFoundException
	 */
	@Test
	public final void testAliasing() throws FrameNotFoundException {
		Frame frame = null;
		try {
			frame = frameNet.getFrame("Attack");
		} catch (FrameNotFoundException e) {
			e.printStackTrace();
		}
		frameNet.addFrameAlias(frame, "Blubb");

		assertEquals(frame, frameNet.getFrame("Blubb"));

		assertEquals(frame, frameNet.removeFrameAlias("Blubb"));

		try {
			frameNet.getFrame("Blubb");
		} catch (FrameNotFoundException e) {
			assertTrue(true);
		}

	}

	// public static junit.framework.Test suite() {
	// junit.framework.TestSuite tc = new
	// junit.framework.TestSuite(TestFrameNet.class);
	// return tc;
	// }

}
