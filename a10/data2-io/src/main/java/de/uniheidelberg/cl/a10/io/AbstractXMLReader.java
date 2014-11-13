package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public abstract class AbstractXMLReader<T> extends AbstractReader<T> {
	@Override
	public T read(final InputStream is) throws IOException {
		try {
			return this.read(new InputStreamReader(is));
		} catch (ValidityException e) {
			e.printStackTrace();
			throw new IOException(e);
		} catch (ParsingException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public T read(Reader r) throws IOException, ValidityException,
			ParsingException {
		Builder xBuilder = new Builder();

		Document doc;
		doc = xBuilder.build(r);
		return this.read(doc.getRootElement());
	}

	public T read(final Document doc) throws IOException {
		return this.read(doc.getRootElement());
	}

	protected abstract T read(final Element rootElement) throws IOException;

	protected String getNamespace() {
		return null;
	}

	/**
	 * This is for an easier transition from dom4j to xom. On the long run, this
	 * should be removed.
	 * 
	 * @param start
	 * @param names
	 * @return
	 */
	@Deprecated
	protected Iterable<Element> getElements(Element start, String... names) {
		Elements elements;
		if (getNamespace() == null)
			elements = start.getChildElements(names[0]);
		else
			elements = start.getChildElements(names[0], getNamespace());

		if (names.length > 1) {
			return getElements(elements.get(0),
					Arrays.copyOfRange(names, 1, names.length));
		} else {
			final Elements fElements = elements;
			return new Iterable<Element>() {

				@Override
				public Iterator<Element> iterator() {
					return new Iterator<Element>() {

						int current = 0;

						@Override
						public boolean hasNext() {
							return current < fElements.size();
						}

						@Override
						public Element next() {
							return fElements.get(current++);
						}

						@Override
						public void remove() {
							throw new UnsupportedOperationException();
						}
					};
				}
			};
		}
	}

}
