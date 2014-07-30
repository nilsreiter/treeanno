package de.nilsreiter.data.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import de.nilsreiter.util.db.DatabaseConfiguration;
import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DBDataWriter;
import de.uniheidelberg.cl.a10.data2.io.DBDocument;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class StoreInDB extends Main {
	@Argument(usage = "A collection of files in data2-XML format",
			required = true)
	List<File> arguments = new ArrayList<File>();

	@Option(name = "--init", usage = "Initialize the database table")
	boolean initTable = false;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, SQLException {
		StoreInDB sid = new StoreInDB();
		sid.processArguments(args);
		sid.run();
	}

	private void run() throws IOException, ClassNotFoundException, SQLException {
		DataReader dr = new DataReader();
		DatabaseDBConfiguration_impl db =
				new DatabaseDBConfiguration_impl(
						DatabaseConfiguration
								.getDatabaseConfiguration(getConfiguration()));
		if (initTable) {
			DBDocument dbd = new DBDocument(db);
			dbd.initDatabase();
		}
		for (File file : arguments) {
			Document doc = dr.read(new FileInputStream(file));
			DBDataWriter dbdw = new DBDataWriter(db);
			dbdw.write(doc);
		}
	}
}
