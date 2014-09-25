package de.nilsreiter.web.beans;

public abstract class ObjectInfo {
	String databaseId;
	long creationDate;

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	@Override
	public int hashCode() {
		return databaseId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass().isAssignableFrom(obj.getClass())) {
			return obj.hashCode() == this.hashCode();
		}
		return false;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
}
