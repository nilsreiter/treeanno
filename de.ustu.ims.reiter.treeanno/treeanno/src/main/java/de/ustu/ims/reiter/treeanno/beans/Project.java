package de.ustu.ims.reiter.treeanno.beans;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

@Entity(name = "treeanno_projects")
public class Project {

	@DatabaseField(generatedId = true)
	int id;

	@Column
	String name;

	@ForeignCollectionField(eager = false)
	ForeignCollection<Document> documents;

	@Column
	int type = 1;

	@Deprecated
	public int getDatabaseId() {
		return id;
	}

	@Deprecated
	public void setDatabaseId(int databaseId) {
		this.id = databaseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Project)) return false;
		return this.hashCode() == obj.hashCode();
	}

	public int getId() {
		return id;
	}

	public ForeignCollection<Document> getDocuments() {
		return documents;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
