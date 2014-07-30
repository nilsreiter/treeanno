package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.io.Writer;

public class DBAlignmentWriter extends AlignmentWriter implements
Writer<Alignment<? extends AnnotationObjectInDocument>> {
	DBAlignment database;

	public DBAlignmentWriter(DatabaseDBConfiguration_impl database) {
		super(new ByteArrayOutputStream());
		this.database = new DBAlignment(database);

	}

	public DBAlignmentWriter(DBAlignment database) {
		super(new ByteArrayOutputStream());
		this.database = database;

	}

	@Override
	public void write(Alignment<? extends AnnotationObjectInDocument> doc)
			throws IOException {
		// create the XML representation
		super.write(doc);
		super.close();

		// Collect document ids
		String docIds = doc.getDocuments().toString();
		docIds = docIds.substring(1, docIds.length() - 1);
		docIds = docIds.replaceAll(" ", "");

		// Write to database
		try {
			PreparedStatement statement = database.getInsertStatement();
			int fC = 1;
			statement.setString(fC++, doc.getId());
			statement.setString(fC++, docIds);
			statement.setString(fC++, outputStream.toString());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new IOException(e);
		}

	}

}
