package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.InputStream;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public abstract class AbstractXMLReader<T> extends AbstractReader<T> {
	@Override
	public T read(final InputStream is) throws IOException {
		try {

			Builder xBuilder = new Builder();

			// DOMReader domReader = new DOMReader();

			Document doc = xBuilder.build(is);// domReader.read(dBuilder.parse(is));
			return this.read(doc.getRootElement());
		} catch (ValidityException e) {
			e.printStackTrace();
			throw new IOException(e);
		} catch (ParsingException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public T read(final Document doc) throws IOException {
		return this.read(doc.getRootElement());
	}

	protected abstract T read(final Element rootElement) throws IOException;

}
