package de.nilsreiter.web.beans;

public class RandomWalkConfig {
	String alignmentDocumentId;

	int n = 100;

	int k = 5;

	public String getAlignmentDocumentId() {
		return alignmentDocumentId;
	}

	public void setAlignmentDocumentId(String alignmentDocumentId) {
		this.alignmentDocumentId = alignmentDocumentId;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
}
