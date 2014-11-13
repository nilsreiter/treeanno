package de.nilsreiter.util.db;

import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceFactory {
	public static DataSource getDataSource(DatabaseConfiguration dbConfig) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(dbConfig.getUsername());
		ds.setPassword(dbConfig.getPassword());
		ds.setUrl(dbConfig.getURL());
		return ds;
	}

	public static DataSource getDataSource(Configuration config) {
		return getDataSource(DatabaseConfiguration
				.getDatabaseConfiguration(config));
	}
}
