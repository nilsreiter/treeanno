package de.nilsreiter.pipeline.uima.wsd;

import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.PipelineBuilder;
import de.nilsreiter.pipeline.PipelineMain;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;

public class TestWSD {
	JCas jcas;
	Configuration configuration;

	@Before
	public void setUp() throws UIMAException, ConfigurationException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks.");
		jcas.setDocumentLanguage("en");
		configuration =
				new HierarchicalINIConfiguration(this.getClass()
						.getClassLoader().getResource("configuration.ini"));
	}

	@Test
	public void testWSD() throws UIMAException {

		PipelineMain pm = new PipelineMain();
		pm.setConfiguration(configuration);
		pm.initResources();
		SimplePipeline.runPipeline(jcas,
				PipelineBuilder.array(pm.getBasicRowlandsonPipeline()));

		assertEquals(1, JCasUtil.select(jcas, WSDResult.class).size());
	}
}
