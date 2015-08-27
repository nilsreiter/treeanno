package de.ustu.ims.reiter.treeanno.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.j256.ormlite.field.DatabaseField;

@Entity(name = "treeanno_documents")
public class Document {
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

	@Column
	boolean hidden;

	@Column
	private String xmi = "";

	@Deprecated
	public int getId() {
		return id;
	}

	@Deprecated
	public void setId(int id) {
		this.id = id;
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

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
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

}
