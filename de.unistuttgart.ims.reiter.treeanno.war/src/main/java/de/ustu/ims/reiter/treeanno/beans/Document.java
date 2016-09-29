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

	@Deprecated
	@Column
	Date modificationDate;

	@DatabaseField(canBeNull = false, foreign = true, columnName = "project",
			foreignAutoRefresh = true)
	Project project;

	@Column
	String name;

	@Deprecated
	@Column
	boolean hidden;

	@Deprecated
	@DatabaseField(canBeNull = true, foreign = true, columnName = "cloneOf")
	Document cloneOf;

	@DatabaseField(columnName = FIELD_XMI, columnDefinition = "LONGTEXT")
	String xmi;

	@ForeignCollectionField(eager = false)
	ForeignCollection<UserDocument> userDocuments;

	public int getId() {
		return id;
	}

	@Deprecated
	public Date getModificationDate() {
		return modificationDate;
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

	@Deprecated
	public boolean isHidden() {
		return hidden;
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

	@Deprecated
	public Document getCloneOf() {
		return cloneOf;
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

}
