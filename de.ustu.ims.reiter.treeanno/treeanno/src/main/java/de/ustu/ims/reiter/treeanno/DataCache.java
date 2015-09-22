package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

@Deprecated
public class DataCache implements DataLayer {
	DatabaseIO dbio;
	Map<Integer, User> userMap;
	Map<Integer, Project> projectMap;
	Map<Integer, Document> documentMap;
	Collection<Project> projectCollection;
	Map<Document, JCas> jcasCache;
	Map<Pair<Project, User>, Integer> permissions;
	Map<Project, Collection<Document>> documentCollections;

	public DataCache(DatabaseIO dataLayer) {
		dbio = dataLayer;
		init();
	}

	public void init() {
		userMap = new HashMap<Integer, User>();
		projectMap = new HashMap<Integer, Project>();
		documentMap = new HashMap<Integer, Document>();
		projectCollection = null;
		jcasCache = new HashMap<Document, JCas>();
		permissions = new HashMap<Pair<Project, User>, Integer>();
		documentCollections = new HashMap<Project, Collection<Document>>();
	}

	@Override
	public Project getProject(int i) throws SQLException {
		if (!projectMap.containsKey(i)) projectMap.put(i, dbio.getProject(i));
		return projectMap.get(i);
	}

	@Override
	public Collection<Project> getProjects() throws SQLException {
		if (projectCollection == null) projectCollection = dbio.getProjects();
		return projectCollection;
	}

	@Override
	public User getUser(int i) throws SQLException {
		if (!userMap.containsKey(i)) userMap.put(i, dbio.getUser(i));
		return userMap.get(i);
	}

	@Override
	public Document getDocument(int i) throws SQLException {
		if (!documentMap.containsKey(i))
			documentMap.put(i, dbio.getDocument(i));
		return documentMap.get(i);
	}

	@Override
	public int getAccessLevel(Project proj, User user) throws SQLException {
		Pair<Project, User> p = new Pair<Project, User>(proj, user);
		if (!permissions.containsKey(p))
			permissions.put(p, dbio.getAccessLevel(proj, user));
		return permissions.get(p);
	}

	@Override
	public Collection<Document> getDocuments(Project proj) throws SQLException {
		if (!documentCollections.containsKey(proj))
			documentCollections.put(proj, dbio.getDocuments(proj));
		return documentCollections.get(proj);
	}

	@Override
	public JCas getJCas(Document document) throws UIMAException, SQLException,
			SAXException, IOException {
		if (!jcasCache.containsKey(document))
			jcasCache.put(document, dbio.getJCas(document));
		return jcasCache.get(document);
	}

	@Override
	public boolean deleteDocument(Document document) throws SQLException {
		if (dbio.deleteDocument(document)) {
			jcasCache.remove(document);
			documentMap.remove(document.getDatabaseId());
			documentCollections.remove(document.getProject());
			return true;
		}
		return false;
	}

	@Override
	public int cloneDocument(Document document) throws SQLException {
		int r = dbio.cloneDocument(document);
		if (r >= 0) {
			documentCollections.remove(document.getProject());
		}
		return r;
	}

	@Override
	public boolean setJCas(Document document, JCas jcas) throws SQLException,
	SAXException {
		if (dbio.setJCas(document, jcas)) {
			jcasCache.remove(document);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateDocument(Document document) throws SQLException {
		this.documentMap.remove(document);
		this.jcasCache.remove(document);
		this.documentCollections.remove(document.getProject());
		return dbio.updateDocument(document);
	}

	@Override
	public Document getNewDocument(Project p) throws SQLException {
		return dbio.getNewDocument(p);
	}

	@Override
	public UserDocument getUserDocument(User user, Document document)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public UserDocument getUserDocument(int u, int d) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUserDocument(UserDocument document)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Document createNewDocument(Document d) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDocument getUserDocument(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
