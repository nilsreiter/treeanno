package de.nilsreiter.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.nilsreiter.event.similarity.ArgumentText;
import de.nilsreiter.event.similarity.EventSimilarityFunction;
import de.nilsreiter.event.similarity.FrameNet;
import de.nilsreiter.event.similarity.GaussianDistanceSimilarity;
import de.nilsreiter.event.similarity.VerbNet;
import de.nilsreiter.event.similarity.WordNet;
import de.nilsreiter.util.StringMap;
import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;
import de.nilsreiter.web.beans.AlignmentInfo;
import de.nilsreiter.web.beans.DocumentInfo;
import de.nilsreiter.web.beans.DocumentSetInfo;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;
import de.uniheidelberg.cl.a10.data2.io.DBDataReader;
import de.uniheidelberg.cl.a10.data2.io.DBDocumentSetReader;

public class ServletDocumentManager {

	List<DocumentInfo> documentInfo = null;
	List<DocumentSetInfo> documentSetInfo = null;
	public List<AlignmentInfo> alignmentInfo = null;
	Database database;

	public DBDataReader getDataReader() {
		return new DBDataReader(this.getDatabase());
	}

	public DBAlignmentReader<Event> getAlignmentReader() throws SQLException {
		return new DBAlignmentReader<Event>(this.getDatabase());
	}

	public DBDocumentSetReader getDocumentSetReader() throws SQLException {
		return new DBDocumentSetReader(this.getDatabase());
	}

	public Map<Token, String> getClassesForTokens(Alignment<Event> alignment) {
		Map<Token, String> classesForTokens =
				this.getClassesForTokens(alignment.getDocuments());
		for (Link<Event> link : alignment.getAlignments()) {
			for (Event event : link.getElements()) {
				classesForTokens.put(event.firstToken(),
						classesForTokens.get(event.firstToken())
								+ " alignment " + link.getId());
			}
		}
		return classesForTokens;
	}

	public Map<Token, String>
			getClassesForTokens(Collection<Document> documents) {

		StringMap<Token> classesForTokens = new StringMap<Token>();
		for (Document document : documents) {
			for (Frame frame : document.getFrames()) {
				if (!classesForTokens.containsKey(frame.firstToken())) {
					classesForTokens.put(frame.firstToken(), "");
				}
				classesForTokens.append(frame.firstToken(),
						" frame " + frame.getId());
			}
			for (Event frame : document.getEvents()) {
				if (!classesForTokens.containsKey((frame).firstToken())) {
					classesForTokens.append(((HasTokens) frame).firstToken(),
							"");
				}
				classesForTokens.put(((HasTokens) frame).firstToken(),
						" event " + frame.getId());

			}
			for (Mention mention : document.getMentions()) {
				for (Token token : mention) {
					classesForTokens.append(token,
							" mention " + mention.getGlobalId());
				}
			}
		}
		return classesForTokens;
	}

	public List<DocumentInfo> getDocumentInfo() {
		if (documentInfo == null) {
			SQLBuilder b = new SQLBuilder();
			b.select("*").from(database.getTableName("documents"));
			documentInfo = new ArrayList<DocumentInfo>();
			ResultSet rs = null;
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = database.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(b.toString());
				if (rs.first()) {
					do {
						DocumentInfo di = new DocumentInfo();
						di.setDatabaseId(rs.getString(1));
						di.setId(rs.getString(2));
						di.setCorpus(rs.getString(3));
						di.setTextBegin(rs.getString(4));
						documentInfo.add(di);
					} while (rs.next());
					return documentInfo;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtils.close(rs);
				DBUtils.close(stmt);
				DBUtils.close(conn);
			}
		}
		return documentInfo;
	}

	public List<DocumentSetInfo> getDocumentSetInfo() {
		if (this.documentSetInfo == null) {
			SQLBuilder b = new SQLBuilder();
			b.select("*").from(database.getTableName("documentSets"));
			documentSetInfo = new LinkedList<DocumentSetInfo>();
			ResultSet rs = null;
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = database.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(b.toString());
				if (rs.first()) {
					do {
						DocumentSetInfo dsi = new DocumentSetInfo();
						dsi.setId(rs.getString(3));
						dsi.setDatabaseId(rs.getString(1));
						for (String s : rs.getString(2).split(",")) {
							dsi.add(s);
						}
						documentSetInfo.add(dsi);
					} while (rs.next());
					return documentSetInfo;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtils.close(rs);
				DBUtils.close(stmt);
				DBUtils.close(conn);
			}
		}
		return documentSetInfo;
	}

	public List<AlignmentInfo> getAlignmentDocuments() {
		if (alignmentInfo == null) {
			SQLBuilder b = new SQLBuilder();
			b.select("*").from(database.getTableName("alignments"));
			alignmentInfo = new LinkedList<AlignmentInfo>();
			ResultSet rs = null;
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = database.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(b.toString());
				if (rs.first()) {
					do {
						AlignmentInfo dsi = new AlignmentInfo();
						dsi.setId(rs.getString(2));
						dsi.setDatabaseId(rs.getString(1));
						for (String s : rs.getString(3).split(",")) {
							dsi.add(s);
						}
						alignmentInfo.add(dsi);
					} while (rs.next());
					return alignmentInfo;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtils.close(rs);
				DBUtils.close(stmt);
				DBUtils.close(conn);
			}
		}
		return alignmentInfo;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	@SuppressWarnings("unchecked")
	public List<Class<? extends EventSimilarityFunction>>
			getSupportedFunctions() {
		List<Class<? extends EventSimilarityFunction>> similarityTypes =
				Arrays.asList(WordNet.class, FrameNet.class, VerbNet.class,
						ArgumentText.class, GaussianDistanceSimilarity.class);

		return similarityTypes;
	}

}
