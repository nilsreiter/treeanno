package de.nilsreiter.event.similarity.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.similarity.EventSimilarityFunction;
import de.nilsreiter.event.similarity.FrameNet;
import de.nilsreiter.event.similarity.SimilarityFunctionFactory;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityCalculationException;

public class TestSimilarityFunctionFactory {

	Configuration configuration;
	Database database;

	Document[] documents;
	Event[] events;

	@Before
	public void setUp() throws ClassNotFoundException, SQLException {

		documents = new Document[1];
		events = new Event[5];
		for (int i = 0; i < documents.length; i++) {
			documents[i] = mock(Document.class);
			when(documents[i].getId()).thenReturn("r0003");
		}

		for (int i = 0; i < events.length; i++) {
			events[i] = mock(Event.class);
			when(events[i].getId()).thenReturn("ev" + i);
			when(events[i].getRitualDocument()).thenReturn(documents[0]);
		}

		configuration = new BaseConfiguration();
		configuration.setProperty(
				"similarity." + FrameNet.class.getCanonicalName(), 0.5);
		DatabaseConfiguration dbConf =
				DatabaseConfiguration.getLocalConfiguration();
		dbConf.setPrefix("rituals_");
		database = new DatabaseDBConfiguration_impl(dbConf);
	}

	@Test
	public void test() throws IncompatibleException,
			SimilarityCalculationException {
		EventSimilarityFunction function;

		function =
				SimilarityFunctionFactory.getSimilarityFunction(database,
						configuration);

		assertEquals(0.5, function.sim(events[0], events[0]).getProbability(),
				1e-4);
		assertEquals(0.125,
				function.sim(events[0], events[1]).getProbability(), 1e-4);

	}
}
