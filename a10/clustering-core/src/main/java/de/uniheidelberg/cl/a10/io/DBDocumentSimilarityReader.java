package de.uniheidelberg.cl.a10.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nu.xom.Element;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class DBDocumentSimilarityReader extends
		AbstractLinkedXMLReader<Matrix<Document, Document, Probability>> {
	public static final String databaseName = "documentSimilarities";

	Database database;

	public DBDocumentSimilarityReader(Database db) {
		super(new DatabaseDocumentStreamProvider(db));
		database = db;
	}

	public Matrix<Document, Document, Probability> read(DocumentSet ds)
			throws SQLException, FileNotFoundException,
			DocumentNotFoundException, IOException {
		MapMatrix<Document, Document, Probability> matrix =
				new MapMatrix<Document, Document, Probability>(Probability.NULL);
		SQLBuilder b = new SQLBuilder();
		b.select("documentId1", "documentId2", "sim")
				.from(database.getTableName(databaseName))
		.where("documentSetId = '" + ds.getId() + "'");
		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(b.toString());
		if (rs.first())
			do {
				matrix.put(getRitualDocument(rs.getString(1)),
						getRitualDocument(rs.getString(2)),
						Probability.fromLogProbability(rs.getDouble(3)));
			} while (rs.next());
		return matrix;

	}

	@Override
	protected Matrix<Document, Document, Probability> read(Element rootElement)
			throws IOException {
		// empty, because not used
		throw new UnsupportedOperationException();
	}

}
