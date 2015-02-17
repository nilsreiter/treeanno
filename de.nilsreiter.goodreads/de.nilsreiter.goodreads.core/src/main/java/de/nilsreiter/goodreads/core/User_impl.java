package de.nilsreiter.goodreads.core;

import de.nilsreiter.goodreads.api.User;

public class User_impl implements User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	int goodReadsId;
	String name;
	String location;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGoodReadsId() {
		return goodReadsId;
	}

	public void setGoodReadsId(int goodReadsId) {
		this.goodReadsId = goodReadsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
