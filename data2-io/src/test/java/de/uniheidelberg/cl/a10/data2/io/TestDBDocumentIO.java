package de.uniheidelberg.cl.a10.data2.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.data2.Document;

public class TestDBDocumentIO {

	Document document;

	DBDataWriter writer;
	DBDataReader reader;

	@Before
	public void setUp() throws Exception {
		DBTestConstants.getDBDocument().initDatabase();
		DataReader dr = new DataReader();
		document = dr.read(this.getClass().getResourceAsStream("/r0010.xml"));
		DatabaseDBConfiguration_impl db = new DatabaseDBConfiguration_impl(DBTestConstants.dbConf);
		writer = new DBDataWriter(db);
		reader = new DBDataReader(db);

	}

	@After
	public void tearDown() throws SQLException, ClassNotFoundException {
		DBTestConstants.getDBDocument().deleteTable();
	}

	@Test
	public void testDocumentIO() throws SQLException, IOException,
			ValidityException, ParsingException {
		assertNotNull(document);
		writer.write(document);

		Document rDocument = reader.read(document.getId());
		assertNotNull(rDocument);
		assertEquals(document.getId(), rDocument.getId());
		assertEquals(document, rDocument);
	}

}
