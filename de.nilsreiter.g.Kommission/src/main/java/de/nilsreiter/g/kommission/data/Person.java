package de.nilsreiter.g.kommission.data;

public interface Person {
	String getName();

	Double getPropertyValue(Property p);

	Group getGroup();
}
