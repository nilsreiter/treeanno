package de.nilsreiter.event.similarity.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.event.similarity.EventSimilarityFunction;
import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;
import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class SimilarityDatabase_impl<T extends HasId & HasDocument> implements
		SimilarityDatabase<T> {

	private Logger logger = LoggerFactory.getLogger(SimilarityDatabase.class);
	public static final String BASE_TBL_SIMILARITIES = "similarities";

	private final String table_similarities;

	public static int documentIdMaxLength = 20;
	public static int eventIdMaxLength = 10;
	public static int typeNameMaxLength = 4;

	private static String TBL_STRUCT_SIMILARITIES =
			"id INT NOT NULL AUTO_INCREMENT KEY, " + "type CHAR("
					+ typeNameMaxLength + "), " + "document1 VARCHAR("
					+ documentIdMaxLength + "), " + "document2 VARCHAR("
					+ documentIdMaxLength + "), " + "id1 VARCHAR("
					+ eventIdMaxLength + ")," + " id2 VARCHAR("
					+ eventIdMaxLength + ")," + " sim DOUBLE";

	PreparedStatement putStatement = null;
	PreparedStatement getStatement = null;

	int putCounter = 0;
	int batchSize = 10000;

	Database database;

	public SimilarityDatabase_impl(Database db) throws SQLException,
	ClassNotFoundException {
		database = db;
		table_similarities = BASE_TBL_SIMILARITIES;
	}

	@Override
	public synchronized void putSimilarity(
			Class<? extends EventSimilarityFunction> simType, T e1, T e2,
			double similarity) throws SQLException {
		if (putStatement == null)
			putStatement =
					database.getConnection().prepareStatement(
							"INSERT INTO "
									+ database.getTableName(table_similarities)
									+ " values (default,?,?,?,?,?,?)");
		putStatement.setString(1,
				simType.getSimpleName().substring(0, typeNameMaxLength));
		putStatement.setString(2, e1.getRitualDocument().getId());
		putStatement.setString(3, e2.getRitualDocument().getId());
		putStatement.setString(4, e1.getId());
		putStatement.setString(5, e2.getId());
		putStatement.setDouble(6, similarity);

		putStatement.addBatch();

		if (++putCounter % batchSize == 0) {
			logger.trace("Executing batch");
			putStatement.executeBatch();
		}

	}

	/**
	 * Executes all batches, closes the connection
	 * 
	 * @throws SQLException
	 */
	@Override
	public synchronized void finish() throws SQLException {
		logger.trace("Executing batch");
		putStatement.executeBatch();
		putStatement.close();
	}

	@Override
	public double getSimilarity(Class<? extends SimilarityFunction<T>> simType,
			T e1, T e2) throws SQLException {
		if (getStatement == null) {
			SQLBuilder b = new SQLBuilder();
			b.select("sim")
					.from(database.getTableName(table_similarities))
					.where("type=? AND document1=? AND document2=? AND id1=? AND id2=?");

			getStatement =
					database.getConnection().prepareStatement(b.toString());
		}
		getStatement.setString(1,
				simType.getSimpleName().substring(0, typeNameMaxLength));
		getStatement.setString(2, e1.getRitualDocument().getId());
		getStatement.setString(3, e2.getRitualDocument().getId());
		getStatement.setString(4, e1.getId());
		getStatement.setString(5, e2.getId());
		ResultSet rs = getStatement.executeQuery();
		if (rs.first())
			return rs.getDouble(1);
		else {
			logger.error("No entry found for {}({} and {})", new Object[] {
					simType, e1.getId(), e2.getId() });
			return 0.0;
		}
	}

	@Override
	public void rebuild() throws SQLException {
		database.dropTable(table_similarities);
		this.initSimilaritiesTable();
	}

	public void initSimilaritiesTable() throws SQLException {
		SQLBuilder b = new SQLBuilder();
		b.create(database.getTableName(table_similarities)).struct(
				TBL_STRUCT_SIMILARITIES);
		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(b.toString());
		stmt.close();
		conn.close();

	}

	@Override
	public void dropType(String type) throws SQLException {
		StringBuilder b = new StringBuilder();
		b.append("DELETE FROM ").append(table_similarities);
		b.append(" WHERE type='").append(type).append("';");
		Statement stmt = database.getConnection().createStatement();
		stmt.execute(b.toString());
		stmt.close();

	}

	public Database getDatabase() {
		return database;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	@Override
	public Map<String, Matrix<T, T, Double>> getSimilarities(Document doc1,
			Document doc2) throws SQLException {
		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		SQLBuilder b = new SQLBuilder();
		b.select("*")
				.from(table_similarities)
				.where(" ( document1='" + doc1.getId() + "' AND document2='"
						+ doc2.getId() + "')" + " OR ( document1='"
						+ doc1.getId() + "' AND document2='" + doc1.getId()
						+ "' )" + " OR ( document1='" + doc2.getId()
						+ "' AND document2='" + doc1.getId() + "' )"
						+ " OR ( document1='" + doc2.getId()
						+ "' AND document2='" + doc2.getId() + "' )");

		ResultSet rs = stmt.executeQuery(b.toString());

		Map<String, Matrix<T, T, Double>> matrixMap =
				new HashMap<String, Matrix<T, T, Double>>();

		while (rs.next()) {
			String id1 = rs.getString("id1");
			String id2 = rs.getString("id2");
			String type = rs.getString(2);
			double sim = rs.getDouble(7);

			@SuppressWarnings("unchecked")
			T aoi1 = (T) doc1.getById(id1);
			@SuppressWarnings("unchecked")
			T aoi2 = (T) doc2.getById(id2);
			if (!matrixMap.containsKey(type))
				matrixMap.put(type, new MapMatrix<T, T, Double>(0.0));
			if (aoi1 != null && aoi2 != null)
				matrixMap.get(type).put(aoi1, aoi2, sim);
		}
		stmt.close();
		conn.close();
		return matrixMap;
	}
}