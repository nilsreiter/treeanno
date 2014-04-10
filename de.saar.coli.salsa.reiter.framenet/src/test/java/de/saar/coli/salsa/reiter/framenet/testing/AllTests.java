package de.saar.coli.salsa.reiter.framenet.testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestFrameNet13.class, TestFrameNet15.class,
	TestFNCorpus15.class, TestFNCorpus13.class, TestPair.class,
	TestSalsaTigerXML.class, TestFlatFormat.class,
	TestAnnotationCorpus13.class, TestAnnotationCorpus15.class,
	TestExporter.class })
public class AllTests {
}
