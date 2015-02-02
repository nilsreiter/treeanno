package de.nilsreiter.g.kommission.data.impl;

import java.util.Map;

import de.nilsreiter.g.kommission.data.Group;
import de.nilsreiter.g.kommission.data.Person;
import de.nilsreiter.g.kommission.data.Property;

public class Person_impl implements Person {
	int id;
	String name;
	Map<Property_impl, Double> properties;
	Group group;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getPropertyValue(Property p) {
		return properties.get(p);
	}

	@Override
	public Group getGroup() {
		return group;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Property_impl, Double> getProperties() {
		return properties;
	}

	public void setProperties(Map<Property_impl, Double> properties) {
		this.properties = properties;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Person: ").append(name).append('\n');
		for (Map.Entry<Property_impl, Double> entry : properties.entrySet()) {
			b.append("  ").append(entry.getKey().toString());
			b.append(": ").append(entry.getValue()).append("\n");
		}
		return b.toString();
	}
}
