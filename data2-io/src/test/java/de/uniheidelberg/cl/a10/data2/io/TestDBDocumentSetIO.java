package de.uniheidelberg.cl.a10.data2.io;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.impl.DocumentSet_impl;
import de.uniheidelberg.cl.a10.io.DatabaseDocumentStreamProvider;

public class TestDBDocumentSetIO {

	DocumentSet documentSet;
	Document[] documents;
	DBDocumentSetWriter dbsw;
	DBDocumentSetReader dbsr;

	@Before
	public void setUp() throws Exception {
		DBTestConstants.getDBDocumentSet().initTable();
		documentSet = new DocumentSet_impl("test");
		documents = new Document[5];
		for (int i = 0; i < 5; i++) {
			documents[i] = mock(Document.class);
			when(documents[i].getId()).thenReturn("r00" + (i + 11));
			when(documents[i].toString()).thenReturn("r00" + (i + 11));
			documentSet.add(documents[i]);
		}
		DatabaseDBConfiguration_impl db = new DatabaseDBConfiguration_impl(DBTestConstants.dbConf);
		DBTestConstants.createDatabaseFromFile(getClass().getResourceAsStream(
				"/ritual-documents.sql"));

		dbsw = new DBDocumentSetWriter(db);
		dbsr =
				new DBDocumentSetReader(db, new DatabaseDocumentStreamProvider(
						db));
	}

	@After
	public void tearDown() throws ClassNotFoundException, SQLException,
			IOException {
		DBTestConstants.getDBDocumentSet().deleteTable();
		// DBTestConstants.deleteTestTable();
	}

	@Test
	public void testIO() throws IOException, ValidityException, SQLException,
			ParsingException {
		dbsw.write(documentSet);
		assertNotNull(documentSet);
		DocumentSet ds2 = dbsr.read("test");
		assertNotNull(ds2);
	}
}
