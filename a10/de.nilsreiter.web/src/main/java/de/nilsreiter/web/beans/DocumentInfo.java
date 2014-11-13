package de.nilsreiter.web.beans;

import de.uniheidelberg.cl.a10.HasId;

public class DocumentInfo extends ObjectInfo implements HasId {
	String id;

	String corpus;

	String textBegin;

	boolean open;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCorpus() {
		return corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public String getTextBegin() {
		return textBegin;
	}

	public void setTextBegin(String textBegin) {
		this.textBegin = textBegin.substring(0, 50);
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
