package de.ustu.ims.reiter.treeanno.beans;

import java.util.Date;

public class Document {
	int databaseId;
	Date modificationDate;
	Project project;
	String name;
	boolean hidden;

	@Deprecated
	public int getId() {
		return databaseId;
	}

	@Deprecated
	public void setId(int id) {
		this.databaseId = id;
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
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
