package de.saar.coli.salsa.reiter.framenet.testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestFrameNet15.class, TestFNCorpus15.class, TestPair.class,
	TestAnnotationCorpus15.class, TestExporter.class })
public class AllTestsCoreFrameNet15 {
}
