package de.ustu.ims.reiter.treeanno;

import java.util.Collection;
import java.util.List;

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
}
