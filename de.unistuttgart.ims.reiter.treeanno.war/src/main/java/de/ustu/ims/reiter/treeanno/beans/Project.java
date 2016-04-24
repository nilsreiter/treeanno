package de.ustu.ims.reiter.treeanno.beans;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "treeanno_projects")
public class Project {

	@DatabaseField(generatedId = true)
	int id;

	@DatabaseField
	String name;

	@ForeignCollectionField(eager = false)
	ForeignCollection<Document> documents;

	@DatabaseField
	int type = 1;

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
