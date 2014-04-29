package de.uniheidelberg.cl.a10.patterns.similarity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.HasGlobalId;

public class SimilarityDatabase extends Database {

	private static final String TABLE_NAME = "similarities";

	public static final String CREATE_TABLE = "CREATE TABLE";
	public static final String DROP_TABLE = "DROP TABLE";

	public static final String TABLE_STRUCTURE = "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, type CHAR(5), id1 VARCHAR(10), id2 VARCHAR(10), sim DOUBLE";

	PreparedStatement putStatement = null;
	PreparedStatement getStatement = null;

	public SimilarityDatabase(DatabaseConfiguration dbc) throws SQLException,
			ClassNotFoundException {
		super(dbc);
	}

	public void putSimilarity(Class<? extends SimilarityFunction<?>> simType,
			HasGlobalId e1, HasGlobalId e2, double similarity)
			throws SQLException {
		if (putStatement == null)
			putStatement = this.getConnection().prepareStatement(
					"INSERT INTO " + TABLE_NAME + " values (default,?,?,?,?)");
		putStatement.setString(1, simType.getSimpleName());
		putStatement.setString(2, e1.getGlobalId());
		putStatement.setString(3, e2.getGlobalId());
		putStatement.setDouble(4, similarity);

		putStatement.executeUpdate();

	}

	public double getSimilarity(Class<? extends SimilarityFunction<?>> simType,
			HasGlobalId e1, HasGlobalId e2) throws SQLException {
		if (getStatement == null)
			getStatement = this.getConnection().prepareStatement(
					"SELECT sim from " + TABLE_NAME
							+ " WHERE type = ? AND id1 = ? AND id2 = ?;");
		getStatement.setString(1, simType.getSimpleName());
		getStatement.setString(2, e1.getGlobalId());
		getStatement.setString(3, e2.getGlobalId());
		ResultSet rs = getStatement.executeQuery();
		return rs.getDouble(0);
	}

	public void initTable() throws SQLException {
		Statement stmt = getStatement();

		stmt.execute(CREATE_TABLE + " similarities (" + TABLE_STRUCTURE + ");");
		stmt.close();
	}

	public void dropTable() throws SQLException {
		Statement stmt = getStatement();

		stmt.execute(DROP_TABLE + " similarities;");
		stmt.close();
	}
}