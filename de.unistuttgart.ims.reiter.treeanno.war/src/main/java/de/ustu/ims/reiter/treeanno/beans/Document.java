package de.ustu.ims.reiter.treeanno.beans;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@Entity(name = "treeanno_documents")
public class Document {
	private static final String FIELD_XMI = "xmi";

	@DatabaseField(generatedId = true)
	int id;

	@DatabaseField(version = true)
	Date modificationDate;

	@DatabaseField(canBeNull = false, foreign = true, columnName = "project",
			foreignAutoRefresh = true)
	Project project;

	@Column
	String name;

	@DatabaseField(columnName = FIELD_XMI, columnDefinition = "LONGTEXT")
	String xmi;

	@ForeignCollectionField(eager = false)
	ForeignCollection<UserDocument> userDocuments;

	@DatabaseField
	DocumentType type;

	public int getId() {
		return id;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDatabaseId() {
		return id;
	}

	public void setDatabaseId(int databaseId) {
		this.id = databaseId;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Document)) return false;
		return this.hashCode() == obj.hashCode();
	}

	public String getXmi() {
		return xmi;
	}

	public void setXmi(String xmi) {
		this.xmi = xmi;
	}

	public Collection<UserDocument> getUserDocuments() {
		return userDocuments;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

}
