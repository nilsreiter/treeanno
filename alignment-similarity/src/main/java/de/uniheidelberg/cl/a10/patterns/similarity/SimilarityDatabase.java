package de.uniheidelberg.cl.a10.patterns.similarity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;

public class SimilarityDatabase<T extends HasId & HasDocument> extends Database {

	public static String TBL_SIMILARITIES = "similarities";
	public static String TBL_DOCUMENTS = "documents";

	private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS";
	private static String DROP_TABLE = "DROP TABLE IF EXISTS";

	public static int documentIdMaxLength = 20;
	public static int eventIdMaxLength = 10;
	public static int typeNameMaxLength = 4;

	private static String TBL_STRUCT_SIMILARITIES = "id INT NOT NULL AUTO_INCREMENT KEY, "
			+ "type CHAR("
			+ typeNameMaxLength
			+ "), "
			+ "document1 INT, "
			+ "document2 INT, "
			+ "id1 VARCHAR("
			+ eventIdMaxLength
			+ "),"
			+ " id2 VARCHAR(" + eventIdMaxLength + ")," + " sim DOUBLE";
	private static String TBL_STRUCT_DOCUMENTS = "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "document VARCHAR(" + documentIdMaxLength + ")";
	private static String CREATE_INDEX = "CREATE INDEX";
	private static String INDEX_APP = "_index";

	PreparedStatement putStatement = null;
	PreparedStatement getStatement = null;

	public SimilarityDatabase(DatabaseConfiguration dbc) throws SQLException,
			ClassNotFoundException {
		super(dbc);
	}

	protected int getDocument(Document doc) throws SQLException {
		StringBuilder b = new StringBuilder();
		b.append("SELECT id FROM ").append(TBL_DOCUMENTS)
				.append(" WHERE document = ?;");
		PreparedStatement stmt = this.getConnection().prepareStatement(
				b.toString());
		stmt.setString(1, doc.getId());
		ResultSet rs = stmt.executeQuery();
		if (rs.first()) {
			return rs.getInt(1);
		}
		registerDocument(doc);
		return getDocument(doc);

	}

	private void registerDocument(Document doc) throws SQLException {
		StringBuilder b = new StringBuilder();
		b.append("INSERT INTO ").append(TBL_DOCUMENTS)
				.append(" VALUES (default,?);");
		PreparedStatement statement = this.getConnection().prepareStatement(
				b.toString());
		statement.setString(1, doc.getId());
		statement.executeUpdate();
	}

	public void putSimilarity(Class<? extends SimilarityFunction> simType,
			T e1, T e2, double similarity) throws SQLException {
		if (putStatement == null)
			putStatement = this.getConnection().prepareStatement(
					"INSERT INTO " + TBL_SIMILARITIES
							+ " values (default,?,?,?,?,?,?)");
		putStatement.setString(1,
				simType.getSimpleName().substring(0, typeNameMaxLength));
		putStatement.setInt(2, getDocument(e1.getRitualDocument()));
		putStatement.setInt(3, getDocument(e2.getRitualDocument()));
		putStatement.setString(4, e1.getId());
		putStatement.setString(5, e2.getId());
		putStatement.setDouble(6, similarity);

		putStatement.executeUpdate();

	}

	public double getSimilarity(Class<? extends SimilarityFunction> simType,
			T e1, T e2) throws SQLException {
		if (getStatement == null) {
			StringBuilder b = new StringBuilder();

			b.append("SELECT sim FROM ")
					.append(TBL_SIMILARITIES)
					.append(" INNER JOIN ")
					.append(TBL_DOCUMENTS)
					.append(" AS doc1 ON ")
					.append(TBL_SIMILARITIES)
					.append(".document1=doc1.id INNER JOIN ")
					.append(TBL_DOCUMENTS)
					.append(" AS doc2 ON ")
					.append(TBL_SIMILARITIES)
					.append(".document2=doc2.id WHERE type=? AND doc1.document=? AND doc2.document=?")
					.append(" AND id1=? AND id2=?;");
			getStatement = this.getConnection().prepareStatement(b.toString());
		}
		getStatement.setString(1,
				simType.getSimpleName().substring(0, typeNameMaxLength));
		getStatement.setString(2, e1.getRitualDocument().getId());
		getStatement.setString(3, e2.getRitualDocument().getId());
		getStatement.setString(4, e1.getId());
		getStatement.setString(5, e2.getId());
		ResultSet rs = getStatement.executeQuery();
		rs.first();
		return rs.getDouble(1);
	}

	public void rebuild() throws SQLException {
		this.dropTable(TBL_DOCUMENTS);
		this.dropTable(TBL_SIMILARITIES);
		this.initDocumentsTable();
		this.initSimilaritiesTable();
	}

	public void initSimilaritiesTable() throws SQLException {
		StringBuilder b = new StringBuilder();

		b.append(CREATE_TABLE).append(' ').append(TBL_SIMILARITIES)
				.append(" (").append(TBL_STRUCT_SIMILARITIES).append(")");

		Statement stmt = getStatement();
		stmt.execute(b.toString());
		stmt.close();

		b = new StringBuilder();
		b.append(CREATE_INDEX).append(' ').append(TBL_SIMILARITIES)
				.append(INDEX_APP).append(" ON ").append(TBL_SIMILARITIES)
				.append(" (type, document1, document2, id1, id2);");

		stmt = getStatement();
		stmt.execute(b.toString());
		stmt.close();

	}

	public void initDocumentsTable() throws SQLException {
		StringBuilder b = new StringBuilder();
		b.append(CREATE_TABLE).append(' ');
		b.append(TBL_DOCUMENTS).append(" (");
		b.append(TBL_STRUCT_DOCUMENTS).append(");");

		Statement stmt = getStatement();
		stmt.execute(b.toString());
		stmt.close();

	}

	public void dropTable(String tbl) throws SQLException {
		StringBuilder b = new StringBuilder();
		b.append(DROP_TABLE).append(" ");
		b.append(tbl).append(';');

		Statement stmt = getStatement();
		stmt.execute(b.toString());
		stmt.close();
	}
}