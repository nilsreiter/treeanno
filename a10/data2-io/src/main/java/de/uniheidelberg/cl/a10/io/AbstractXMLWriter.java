package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.OutputStream;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public abstract class AbstractXMLWriter<T> extends AbstractWriter<T> {

	public AbstractXMLWriter(final OutputStream os) {
		super(os);
	}

	@Override
	public void write(final T object) throws IOException {
		// OutputStreamWriter writer = new
		// OutputStreamWriter(this.outputStream);
		Serializer ser = new Serializer(this.outputStream, "UTF-8");
		// ser.setMaxLength(80);
		// ser.setIndent(3);
		ser.write(getDocument(object));
		ser.flush();
	};

	public abstract Element getElement(T object);

	public Document getDocument(final T object) {
		Document document = new Document(this.getElement(object));
		return document;
	};

}
