package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.impl.DocumentSet_impl;
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
		String title = sE.getAttributeValue(XMLConstants.TITLE);
		DocumentSet ds = new DocumentSet_impl(id);
		ds.setTitle(title);
		for (int i = 0; i < sE.getChildElements("d").size(); i++) {
			this.getRitualDocument(sE.getChildElements("d").get(i).getValue());
		}
		return ds;

	}

}
