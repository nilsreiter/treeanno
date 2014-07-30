package de.nilsreiter.util.db.test;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.util.db.DatabaseConfiguration;
import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;

public class TestDatabase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstruction() throws ClassNotFoundException, SQLException {
		DatabaseDBConfiguration_impl db = new DatabaseDBConfiguration_impl(
				DatabaseConfiguration.getDefaultConfiguration());
		assertNotNull(db);
	}

}
