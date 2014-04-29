package de.nilsreiter.util.db.test;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;

public class TestDatabase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstruction() throws ClassNotFoundException, SQLException {
		Database db = new Database(
				DatabaseConfiguration.getDefaultConfiguration());
		assertNotNull(db);
	}

}
