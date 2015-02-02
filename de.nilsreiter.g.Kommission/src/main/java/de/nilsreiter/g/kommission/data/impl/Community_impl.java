package de.nilsreiter.g.kommission.data.impl;

import java.util.LinkedList;
import java.util.List;

import de.nilsreiter.g.kommission.data.Community;
import de.nilsreiter.g.kommission.data.Group;

public class Community_impl implements Community {

	List<Group> groups;

	List<Person_impl> persons = new LinkedList<Person_impl>();

	List<Property_impl> properties = new LinkedList<Property_impl>();

	@Override
	public List<Group> getGroups() {
		return groups;
	}

	@Override
	public List<Person_impl> getPersons() {
		return persons;
	}

	public void setPersons(List<Person_impl> persons) {
		this.persons = persons;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public List<Property_impl> getProperties() {
		return properties;
	}

	public void setProperties(List<Property_impl> properties) {
		this.properties = properties;
	}

}
