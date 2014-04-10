package de.saar.coli.salsa.reiter.framenet.testing;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNetVersion;

public abstract class TestBase13 {
	protected static String fnhome = "/Users/reiter/Library/Resources/corpora/monolingual/annotated/framenet-1.3";

	protected static String version = "1.3";
	protected static FrameNet frameNet;

	@BeforeClass
	public static void setUp() throws Exception {
		frameNet = new FrameNet();
		frameNet.readData(FNDatabaseReader.createInstance(new File(fnhome),
				FrameNetVersion.fromString(version)));
	}

	@AfterClass
	public static void tearDown() throws Exception {
		frameNet = null;
		System.gc();
	}
}
