package de.nilsreiter.web.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DocumentSetInfo extends ObjectInfo {
	List<String> documentIds = new LinkedList<String>();
	String id;

	public List<String> getDocumentIdList() {
		return documentIds;
	}

	public void setDocumentIdList(List<String> documentIds) {
		this.documentIds = documentIds;
		Collections.sort(documentIds);

	}

	public String getDocumentIds() {
		return this.getDocumentIdList().toString();
	}

	public boolean add(String e) {
		boolean b = documentIds.add(e);
		Collections.sort(documentIds);
		return b;
	}

	public String get(int index) {
		return documentIds.get(index);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
