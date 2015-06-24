package de.ustu.ims.reiter.treeanno;

import java.util.Collection;

import org.apache.uima.jcas.JCas;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;

public interface DataLayer {
	Project getProject(int i);

	Collection<Project> getProjects();

	User getUser(int i);

	Document getDocument(int i);

	int getAccessLevel(Project proj, User user);

	Collection<Document> getDocuments(Project proj);

	JCas getJCas(Document document);

	boolean deleteDocument(Document document);

	int cloneDocument(Document document);

	boolean updateJCas(Document document, JCas jcas);
}
