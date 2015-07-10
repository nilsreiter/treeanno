package de.ustu.ims.reiter.treeanno.beans;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.j256.ormlite.field.DatabaseField;

@Entity(name = "treeanno_projects")
public class Project {
	@DatabaseField(id = true, generatedId = true)
	int id;

	@Column
	String name;

	public int getDatabaseId() {
		return id;
	}

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
}
