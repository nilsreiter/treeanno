package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocument;

public abstract class AbstractXMLWriter<T> extends AbstractWriter<T> {

	public AbstractXMLWriter(final OutputStream os) {
		super(os);
	}

	@Override
	public void write(final T object) throws IOException {
		XMLWriter xmlWriter = new XMLWriter(this.outputStream,
				OutputFormat.createPrettyPrint());
		xmlWriter.write(this.getDocument(object));
		xmlWriter.flush();
	};

	public abstract Element getElement(T object);

	public Document getDocument(final T object) {
		Document document = new DefaultDocument();
		document.setRootElement(this.getElement(object));
		return document;
	};

}
