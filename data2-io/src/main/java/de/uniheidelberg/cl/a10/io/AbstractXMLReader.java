package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.InputStream;
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
		Elements elements = start.getChildElements(names[0]);
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
