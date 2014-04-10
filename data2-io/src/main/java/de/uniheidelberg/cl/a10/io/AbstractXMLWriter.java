package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import nu.xom.Document;
import nu.xom.Element;

public abstract class AbstractXMLWriter<T> extends AbstractWriter<T> {

	public AbstractXMLWriter(final OutputStream os) {
		super(os);
	}

	@Override
	public void write(final T object) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(this.outputStream);
		writer.write(this.getDocument(object).toXML());
		writer.flush();
	};

	public abstract Element getElement(T object);

	public Document getDocument(final T object) {
		Document document = new Document(this.getElement(object));
		return document;
	};

}
