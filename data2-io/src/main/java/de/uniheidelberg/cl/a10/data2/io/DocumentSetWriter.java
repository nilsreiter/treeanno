package de.uniheidelberg.cl.a10.data2.io;

import java.io.OutputStream;

import nu.xom.Attribute;
import nu.xom.Element;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.io.AbstractXMLWriter;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class DocumentSetWriter extends AbstractXMLWriter<DocumentSet> {

	public DocumentSetWriter(OutputStream os) {
		super(os);
	}

	@Override
	public Element getElement(DocumentSet object) {
		Element ret = new Element("docset");
		ret.addAttribute(new Attribute(XMLConstants.ID, object.getId()));
		for (Document doc : object) {
			Element docElement = new Element("d");
			docElement.appendChild(doc.getId());
			ret.appendChild(docElement);
		}
		return ret;
	}

}
