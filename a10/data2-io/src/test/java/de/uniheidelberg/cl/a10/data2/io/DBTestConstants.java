package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import de.nilsreiter.util.db.DatabaseConfiguration;
import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;

public class DBTestConstants {
	public static final DatabaseConfiguration dbConf =
			new DatabaseConfiguration("localhost", "reiterns", "password",
					"test", 3306, "mysql", "");
	static DBDocument dbDoc = null;
	static DBDocumentSet dbDocSet = null;

	public static DBDocument getDBDocument() throws ClassNotFoundException,
	SQLException {
		if (dbDoc == null) dbDoc = new DBDocument(new DatabaseDBConfiguration_impl(dbConf));
		return dbDoc;
	}

	public static DBDocumentSet getDBDocumentSet()
			throws ClassNotFoundException, SQLException {
		if (dbDocSet == null)
			dbDocSet = new DBDocumentSet(new DatabaseDBConfiguration_impl(dbConf));
		return dbDocSet;
	}

	public static void createDatabaseFromFile(InputStream is)
			throws IOException {
		Runtime rt = Runtime.getRuntime();
		Process process =
				rt.exec(new String[] { "/usr/local/bin/mysql", "-u",
						dbConf.getUsername(),
						"--password=" + dbConf.getPassword(), "-h",
						dbConf.getHost(), "test" }, null, null);
		OutputStream os = process.getOutputStream();
		while (is.available() > 0) {
			int ch = is.read();
			os.write(ch);
			// System.out.print((char) ch);
			// os.flush();
		}
	}

	public static void deleteTestTable() throws IOException {
		Runtime rt = Runtime.getRuntime();
		Process process =
				rt.exec(new String[] { "/usr/local/bin/mysql", "-u",
						dbConf.getUsername(),
						"--password=" + dbConf.getPassword(), "-h",
						dbConf.getHost(), "test" }, null, null);
		OutputStream os = process.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		osw.write("DROP TABLE IF EXISTS `documents`;");
		osw.flush();
		osw.close();
	}

}
