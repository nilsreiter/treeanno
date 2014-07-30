package de.nilsreiter.web.beans;

import java.util.Map;

public interface State {
	Map<String, ? extends Object> getOpen();

	Object getCurrentObject();

	void setCurrent(String obj);

	void addOpen(String docId, Object document);

}
