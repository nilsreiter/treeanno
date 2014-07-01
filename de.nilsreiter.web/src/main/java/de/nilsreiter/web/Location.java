package de.nilsreiter.web;

public class Location {

	public Location() {
	}

	public Location(String corpus, Area area) {
		super();
		this.corpus = corpus;
		this.area = area;
	}

	public static enum Area {
		Corpus, Document, Alignment
	}

	String corpus;

	Area area;

	String documentOrAlignment;

	public String getCorpus() {
		return corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area state) {
		this.area = state;
	}

	public String getDocumentOrAlignment() {
		return documentOrAlignment;
	}

	public void setDocumentOrAlignment(String documentOrAlignment) {
		this.documentOrAlignment = documentOrAlignment;
	}

}
