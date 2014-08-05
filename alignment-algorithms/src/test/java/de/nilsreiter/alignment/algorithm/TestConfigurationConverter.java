package de.nilsreiter.alignment.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.similarity.Operation;
import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;

public class TestConfigurationConverter {

	ConfigurationConverter cc = null;

	@Before
	public void setUp() {
		cc = new ConfigurationConverter();
	}

	@Test
	public void testGetBMMConfiguration() throws ConfigurationException {
		Configuration config =
				new HierarchicalINIConfiguration(getClass().getClassLoader()
						.getResource("configuration-BMMConfiguration.ini"));
		BMMConfiguration bmmc = cc.getBMMConfiguration(config);
		assertNotNull(bmmc);
		assertEquals(config.getDouble("BayesianModelMerging.threshold"),
				bmmc.getThreshold(), 1e10);
		assertEquals(config.getBoolean("BayesianModelMerging.threaded", false),
				bmmc.isThreaded());
		assertEquals(Operation.AVG, bmmc.getCombination());
		bmmc.getSimilarityFunctions();
	}
}
