package de.nilsreiter.goodreads.io.xml2db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestXml2DB {

	static DataSource ds;

	Xml2DB xml2db;

	@BeforeClass
	public static void setUpClass() {
		String host = "localhost";
		String port = "3309";
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("reiterns");
		dataSource.setPassword("bybNoaKni");
		dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/reiter");
		ds = dataSource;
	}

	@AfterClass
	public static void tearDownClass() {

	}

	@Before
	public void setUp() {
		xml2db = new Xml2DB(ds);
	}

	@After
	public void tearDown() throws SQLException {
		ds.getConnection().close();
	}

	@Test
	public void testConnection() throws SQLException {
		Connection conn = ds.getConnection();
		assertNotNull(conn);
		assertFalse(conn.isClosed());

	}

	// @Test
	public void testDirectStream() throws ValidityException, ParsingException,
			IOException, SQLException {
		URL url =
				new URL(
						"https://www.goodreads.com/book/show/3?format=xml&key=XGb9Cp3QPbvL4eRz3WfyA&text_only=true");
		xml2db.readStream(url.openStream());
	}

	@Test
	public void testXml2DB() throws ValidityException, FileNotFoundException,
	ParsingException, IOException, SQLException {
		assertTrue(xml2db.readFile(new File("src/test/resources/50.xml")));
	}

	// @Test
	public void testReadBook() throws ValidityException, InterruptedException,
	ParsingException, IOException, SQLException {
		xml2db.readBook(4);
	}
}
