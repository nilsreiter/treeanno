package de.nilsreiter.web.beans;

import java.util.HashMap;
import java.util.Map;

import de.uniheidelberg.cl.a10.HasId;

public class AbstractState<T extends HasId> implements State {
	Map<String, T> openDocuments = new HashMap<String, T>();
	String currentDocument = null;

	public AbstractState(Class<?> cl) {

	}

	@Override
	public Map<String, T> getOpen() {
		return openDocuments;
	}

	public void setOpen(Map<String, T> openDocuments) {
		this.openDocuments = openDocuments;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addOpen(String di, Object e) {
		openDocuments.put(di, (T) e);
	}

	@Override
	public void setCurrent(String currentDocument) {
		this.currentDocument = currentDocument;
	}

	@Override
	public String getCurrentObject() {
		return this.currentDocument;
	}

}
