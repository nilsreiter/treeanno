package de.nilsreiter.web.beans;

import java.util.LinkedList;
import java.util.List;

public class DocumentSetInfo extends DocumentInfo {
	List<String> documentIds = new LinkedList<String>();

	public List<String> getDocumentIdList() {
		return documentIds;
	}

	public void setDocumentIdList(List<String> documentIds) {
		this.documentIds = documentIds;
	}

	public String getDocumentIds() {
		return this.getDocumentIdList().toString();
	}

	public boolean add(String e) {
		return documentIds.add(e);
	}

	public String get(int index) {
		return documentIds.get(index);
	}
}
