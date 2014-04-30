package de.uniheidelberg.cl.a10.patterns.similarity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.HasGlobalId;

public class TestSimilarityDatabase {

	HasGlobalId[] events;

	@Before
	public void setUp() throws Exception {
		events = new HasGlobalId[10];
		for (int i = 0; i < 10; i++) {
			events[i] = mock(HasGlobalId.class);
			when(events[i].getGlobalId()).thenReturn(String.valueOf(i));
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		SimilarityDatabase sd = new SimilarityDatabase(
				DatabaseConfiguration.getDefaultConfiguration());
		sd.initTable();
		sd.putSimilarity(Null.class, events[0], events[1], 0.5);
	}

}
