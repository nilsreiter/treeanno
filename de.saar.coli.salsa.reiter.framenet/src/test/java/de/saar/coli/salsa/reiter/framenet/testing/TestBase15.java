package de.saar.coli.salsa.reiter.framenet.testing;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNetVersion;

public abstract class TestBase15 {
	protected static String fnhome =
			"/Users/reiterns/Documents/Resources/framenet-1.5";
	// "/Network/resources/corpora/monolingual/annotated/framenet-1.5";
	protected static String fnhome13 =
			"/Network/resources/corpora/monolingual/annotated/framenet-1.3";

	protected static String version = "1.5";
	protected static FrameNet frameNet;

	@BeforeClass
	public static void setUpClass() throws Exception {
		frameNet = new FrameNet();
		frameNet.readData(FNDatabaseReader.createInstance(new File(fnhome),
				FrameNetVersion.fromString(version)));
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		frameNet = null;
		System.gc();
	}
}
