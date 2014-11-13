package de.uniheidelberg.cl.a10.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nu.xom.Element;
import nu.xom.Elements;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.patterns.TrainingConfiguration;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class DocumentSimilarityReader extends
		AbstractLinkedXMLReader<Matrix<Document, Document, Probability>> {

	TrainingConfiguration lastTrainingConfiguration = null;

	Probability defaultValue = null;

	public DocumentSimilarityReader(final File dDirectory) {
		super(dDirectory);
	}

	@Override
	protected Matrix<Document, Document, Probability> read(
			final Element rootElement) throws IOException {
		Matrix<Document, Document, Probability> matrix = new MapMatrix<Document, Document, Probability>(
				this.getDefaultValue());
		Elements chElem = rootElement.getChildElements("v");
		for (int i = 0; i < chElem.size(); i++) {
			Element e = chElem.get(i);
			try {
				Document r = this.getRitualDocument(e.getAttributeValue("r"));
				Document c = this.getRitualDocument(e.getAttributeValue("c"));
				Probability p = Probability.fromLogProbability(Double
						.parseDouble(e.getValue()));
				matrix.put(r, c, p);
				matrix.put(c, r, p);
			} catch (FileNotFoundException ex) {
				// nothing happens
			}
		}
		if (rootElement.getFirstChildElement("trainingconfiguration") != null) {
			TrainingConfigurationReader tcr = new TrainingConfigurationReader();
			this.lastTrainingConfiguration = tcr.read(rootElement
					.getFirstChildElement("trainingconfiguration"));
		}
		return matrix;
	}

	public TrainingConfiguration getLastTrainingConfiguration() {
		return lastTrainingConfiguration;
	}

	public Probability getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final Probability defaultValue) {
		this.defaultValue = defaultValue;
	}
}
