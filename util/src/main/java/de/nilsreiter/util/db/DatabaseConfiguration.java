package de.nilsreiter.util.db;

public class DatabaseConfiguration {
	public DatabaseConfiguration(String host, String username, String password,
			String database, String port, String protocol) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		this.port = port;
		this.protocol = protocol;
	}

	String host;
	String username;
	String password;
	String database;
	String port = "3306";
	String protocol = "mysql";

	public DatabaseConfiguration(String host, String database, String username,
			String password) {
		super();
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public String getURL() {
		StringBuilder b = new StringBuilder();
		b.append("jdbc:").append(protocol).append("://");
		b.append(host).append(':').append(port);
		b.append('/');
		b.append(database);

		return b.toString();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public static DatabaseConfiguration getDefaultConfiguration() {
		return new DatabaseConfiguration("waitahapinguin", "reiterns",
				"bybNoaKni", "reiter", "3306", "mysql");
	}
}