package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.util.HashSet;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.io.AbstractLinkedXMLReader;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class DocumentSetReader extends AbstractLinkedXMLReader<DocumentSet> {

	public DocumentSetReader(DataStreamProvider dsProvider) {
		super(dsProvider);
	}

	@Override
	protected DocumentSet read(Element rootElement) throws IOException {
		Element sE = rootElement;
		String id = sE.getAttributeValue(XMLConstants.ID);
		DocumentSet ds = new PrivateDocumentSet(id);
		for (int i = 0; i < sE.getChildElements("d").size(); i++) {
			this.getRitualDocument(sE.getChildElements("d").get(i).getValue());
		}
		return ds;

	}

	public static class PrivateDocumentSet extends HashSet<Document> implements
			DocumentSet {
		private static final long serialVersionUID = 1L;
		String id;

		public PrivateDocumentSet(String s) {
			this.id = s;
		}

		@Override
		public String getId() {
			return id;
		}

	}
}
