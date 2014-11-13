package de.nilsreiter.docsim.main;

import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.a10.io.DocumentSimilarityReader;

public class DocumentSimilarity2DB extends MainWithIO {
	public static void main(String[] args) {
		DocumentSimilarity2DB dsdb = new DocumentSimilarity2DB();
		dsdb.processArguments(args);
		dsdb.run();
	}

	private void run() {
		DocumentSimilarityReader dsr = new DocumentSimilarityReader(this.input);
	}
}
