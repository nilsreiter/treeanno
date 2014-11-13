package de.nilsreiter.util.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import de.nilsreiter.util.db.DatabaseConfiguration;

public class DatabaseDBConfiguration_impl extends AbstractDatabase {
	DatabaseConfiguration config;
	private Connection connection = null;

	public DatabaseDBConfiguration_impl(DatabaseConfiguration dbc)
			throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		config = dbc;
		connection =
				DriverManager.getConnection(dbc.getURL(), dbc.getUsername(),
						dbc.getPassword());
	}

	public DatabaseDBConfiguration_impl(DataSource ds) {

	}

	@Override
	public Statement getStatement() throws SQLException {
		return connection.createStatement();
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	public void disconnect() throws SQLException {
		this.getConnection().close();
	}

	@Override
	public String getTableName(String s) {
		return config.getPrefix() + s;
	}

}