package de.nilsreiter.util.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import de.nilsreiter.util.db.Database;

public class DatabaseDataSource_impl extends AbstractDatabase implements
Database {

	DataSource dataSource;

	public DatabaseDataSource_impl(DataSource ds) {
		dataSource = ds;
	}

	@Override
	@Deprecated
	public Statement getStatement() throws SQLException {
		return this.getConnection().createStatement();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
