package de.nilsreiter.alignment.main;

import java.io.IOException;
import java.util.List;

import de.nilsreiter.data.main.MainWithDBDocuments;
import de.uniheidelberg.cl.a10.data2.Document;

public class AlignmentDummy extends MainWithDBDocuments {

	boolean pairwise = true;

	public static void main(String[] args) throws IOException {
		AlignmentDummy ad = new AlignmentDummy();
		ad.processArguments(args);
		ad.run();
	}

	private void run() throws IOException {
		List<Document> docs = getDocuments();
		for (int i = 0; i < docs.size(); i++) {
			for (int j = i + 1; j < docs.size(); j++) {
				System.err.println(docs.get(i).getId() + " - "
						+ docs.get(j).getId());
			}
		}
	}
}
