package de.nilsreiter.g.kommission.data;

import java.util.List;

public interface Community {
	List<? extends Group> getGroups();

	List<? extends Person> getPersons();

	List<? extends Property> getProperties();
}
