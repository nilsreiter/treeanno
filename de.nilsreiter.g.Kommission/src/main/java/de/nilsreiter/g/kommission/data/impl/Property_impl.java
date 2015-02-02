package de.nilsreiter.g.kommission.data.impl;

import de.nilsreiter.g.kommission.data.Property;

public class Property_impl implements Property {
	String name;

	public Property_impl(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
