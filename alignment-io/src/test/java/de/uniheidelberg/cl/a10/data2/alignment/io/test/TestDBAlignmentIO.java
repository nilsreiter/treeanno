package de.uniheidelberg.cl.a10.data2.alignment.io.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentReader;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentWriter;
import de.uniheidelberg.cl.a10.io.ResourceBasedStreamProvider;

public class TestDBAlignmentIO {
	Document[] documents;
	DBAlignmentWriter dbsw;
	DBAlignmentReader<Token> dbsr;
	DataStreamProvider dsp;
	AlignmentReader<Token> tar;
	Alignment<Token> alignment;
	DBAlignment dba;

	@Before
	public void setUp() throws Exception {
		DatabaseDBConfiguration_impl db = new DatabaseDBConfiguration_impl(DBTestConstants.dbConf);
		dba = new DBAlignment(db);
		dba.initTable();
		dbsw = new DBAlignmentWriter(db);
		dbsr = new DBAlignmentReader<Token>(db);
		dsp = new ResourceBasedStreamProvider();
		tar = new AlignmentReader<Token>(dsp);
		alignment = tar.read(dsp.findStreamFor("alignment1.xml"));

	}

	@After
	public void tearDown() throws SQLException {
		dba.deleteTable();
	}

	@Test
	public void test() throws IOException, ValidityException, SQLException,
	ParsingException {
		dbsw.write(alignment);

		Alignment<Token> ral = dbsr.read("alignment1");
		assertEquals(alignment.getId(), ral.getId());

	}

}
