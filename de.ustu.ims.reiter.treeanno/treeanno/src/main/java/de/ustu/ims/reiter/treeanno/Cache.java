package de.ustu.ims.reiter.treeanno;

import java.util.Collection;
import java.util.List;

import org.apache.uima.jcas.JCas;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

public class Cache implements DataLayer {
	DatabaseIO dbio;

	@Override
	public int getAccessLevel(Project project, User user) {
		return dbio.getAccessLevel(project, user);
	}

	@Override
	public List<Project> getProjects() {
		return dbio.getProjects();
	}

	@Override
	public Collection<Document> getDocuments(Project proj) {
		return dbio.getDocuments(proj);
	}

	@Override
	public User getUser(int i) {
		return dbio.getUser(i);
	}

	@Override
	public Document getDocument(int documentId) {
		return dbio.getDocument(documentId);
	}

	@Override
	public Project getProject(int i) {
		return dbio.getProject(i);
	}

	@Override
	public Collection<Document> getDocuments(int projectId) {
		return dbio.getDocuments(projectId);
	}

	@Override
	public JCas getJCas(Document document) {
		return dbio.getJCas(document);
	}

	@Override
	public boolean deleteDocument(Document document) {
		return dbio.deleteDocument(document);
	}

	@Override
	public int cloneDocument(Document document) {
		return dbio.cloneDocument(document);
	}

	@Override
	public boolean updateJCas(Document document, JCas jcas) {
		return dbio.updateJCas(document, jcas);
	}
}
