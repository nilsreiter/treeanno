package de.uniheidelberg.cl.a10.io;

import java.io.OutputStream;

import nu.xom.Attribute;
import nu.xom.Comment;
import nu.xom.Element;
import nu.xom.Text;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.patterns.TrainingConfiguration;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class DocumentSimilarityWriter extends
		AbstractXMLWriter<Matrix<Document, Document, Probability>> {

	TrainingConfiguration configuration = null;

	public DocumentSimilarityWriter(final OutputStream os) {
		super(os);
	}

	@Override
	public Element getElement(
			final Matrix<Document, Document, Probability> object) {
		Element rootElement = new Element("matrix");
		rootElement.appendChild(new Comment(
				"This representation contains negative log probabilities!"));
		if (this.configuration != null) {
			rootElement.appendChild(configuration.getXML());
		}
		for (Document r : object.getRows()) {
			for (Document c : object.getColumns()) {
				if (object.get(r, c) != null) {
					Element e = new Element("v");
					e.appendChild(new Attribute("r", r.getId()));
					e.appendChild(new Attribute("c", c.getId()));
					e.appendChild(new Text(String.valueOf(object.get(r, c)
							.getLogProbability())));
					rootElement.appendChild(e);
				}
			}
		}
		return rootElement;
	}

	/**
	 * @return the configuration
	 */
	public TrainingConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(final TrainingConfiguration configuration) {
		this.configuration = configuration;
	}
}
