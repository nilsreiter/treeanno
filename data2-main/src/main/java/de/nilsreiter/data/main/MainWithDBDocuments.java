package de.nilsreiter.data.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.kohsuke.args4j.Argument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.util.db.DataSourceFactory;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.impl.DatabaseDataSource_impl;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DBDataReader;

public abstract class MainWithDBDocuments extends Main {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Argument(usage = "A list of document ids", required = true)
	List<String> arguments = new ArrayList<String>();

	protected DBDataReader dataReader;
	protected Database database;

	public List<Document> getDocuments() throws IOException {

		List<Document> docs = new ArrayList<Document>();

		try {

			database =
					new DatabaseDataSource_impl(
							DataSourceFactory.getDataSource(getConfiguration()));

			dataReader = new DBDataReader(database);
			for (String id : arguments) {
				try {
					docs.add(dataReader.read(id));
				} catch (ValidityException e) {
					logger.debug(e.getLocalizedMessage());
				} catch (ParsingException e) {
					logger.debug(e.getLocalizedMessage());
				}
			}
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
			throw new IOException(e);
		}
		return docs;
	}
}
