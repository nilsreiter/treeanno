package de.ustu.ims.reiter.treeanno.beans;

public class Project {
	int databaseId;
	String name;

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return databaseId;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Project)) return false;
		return this.hashCode() == obj.hashCode();
	}
}
