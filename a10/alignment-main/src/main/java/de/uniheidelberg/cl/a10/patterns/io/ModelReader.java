package de.uniheidelberg.cl.a10.patterns.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.impl.FrameTokenEvent_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.TrainingConfiguration;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class ModelReader {
	Map<String, Document> ritualDocumentMap = new HashMap<String, Document>();

	File ritualDocumentsDirectory = null;

	DataReader dataReader = null;

	boolean searchSubDirectories = true;

	public ModelReader(final DataReader cr, final Document... documents) {
		dataReader = cr;
		for (Document rdoc : documents) {
			this.ritualDocumentMap.put(rdoc.getId(), rdoc);
		}
	}

	public ModelReader(final DataReader cr, final Collection<Document> documents) {
		dataReader = cr;
		for (Document rdoc : documents) {
			this.ritualDocumentMap.put(rdoc.getId(), rdoc);
		}
	}

	public ModelReader(final DataReader containerReader,
			final File documentsDirectory) {
		dataReader = containerReader;
		ritualDocumentsDirectory = documentsDirectory;
	}

	public MarkovModel_impl<Integer> readMarkovModel(final InputStream is)
			throws ValidityException, ParsingException, IOException {
		Builder xBuilder = new Builder();

		nu.xom.Document doc = xBuilder.build(is);

		return this.readMarkovModel(doc.getRootElement(),
				new HashMap<String, Object>());
	}

	public SEHiddenMarkovModel_impl<FrameTokenEvent> readHiddenMarkovModel(
			final InputStream is) throws IOException, ValidityException,
			ParsingException {
		Builder xBuilder = new Builder();

		nu.xom.Document doc = xBuilder.build(is);
		return this.readHiddenMarkovModel(doc);
	}

	public SEHiddenMarkovModel_impl<FrameTokenEvent> readHiddenMarkovModel(
			final nu.xom.Document doc) throws IOException {
		return this.readHiddenMarkovModel(doc.getRootElement(),
				new HashMap<String, Object>());
	}

	protected Properties readProperties(final Element propElement) {
		Properties prop = new Properties();
		Elements chElem = propElement.getChildElements("property");
		for (int i = 0; i < chElem.size(); i++) {
			Element pElement = chElem.get(i);
			prop.setProperty(pElement.getAttributeValue("key"),
					pElement.getAttributeValue("value"));
		}

		return prop;
	}

	protected SEHiddenMarkovModel_impl<FrameTokenEvent> readHiddenMarkovModel(
			final Element hmmElement, final Map<String, Object> idMap)
			throws FileNotFoundException, IOException {
		SEHiddenMarkovModel_impl<FrameTokenEvent> hmm = new SEHiddenMarkovModel_impl<FrameTokenEvent>();

		// reading of properties
		if (hmmElement.getFirstChildElement("properties") != null) {
			hmm.setProperties(this.readProperties(hmmElement
					.getFirstChildElement("properties")));
		}

		// reading of training configuration (if any)
		if (hmmElement.getChildElements("trainingconfiguration").size() > 0) {
			Element confElement = hmmElement
					.getFirstChildElement("trainingconfiguration");
			String confType = confElement.getAttributeValue("type");
			try {
				TrainingConfiguration tc = (TrainingConfiguration) Class
						.forName(confType).newInstance();
				if (tc.fromXML(confElement)) {
					hmm.setTrainingConfiguration(tc);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// processing properties
		for (Object s : hmm.getProperties().keySet()) {
			String key = (String) s;

			if (key.startsWith("source")) {
				String value = hmm.getProperty(key);
				String rName = value.replaceAll(".xml", "");
				if (!this.ritualDocumentMap.containsKey(rName))
					this.ritualDocumentMap.put(rName, this.dataReader.read(this
							.searchFileForRitualDocument(value)));
			}
		}

		// reading of events
		Elements chElem = hmmElement.getChildElements("event");
		for (int i = 0; i < chElem.size(); i++) {
			Element eventElement = chElem.get(i);

			String id = eventElement.getAttributeValue("id");
			String frameId = eventElement.getAttributeValue("frame");
			String source = eventElement.getAttributeValue("source");
			FrameTokenEvent frame = null;
			if (source != null) {
				// TODO: This breaks if we want to model other events than
				// frames, but for now it's ok.
				frame = FrameTokenEvent_impl.getEvent(this.ritualDocumentMap.get(source)
						.getById(frameId));
				hmm.getEvents().add(frame);
			}
			idMap.put(id, frame);
			if (eventElement.getAttribute("type") != null) {
				if (eventElement.getAttributeValue("type").equalsIgnoreCase(
						"start"))
					hmm.setStartSymbol(frame);
				else if (eventElement.getAttributeValue("type")
						.equalsIgnoreCase("end"))
					hmm.setEndSymbol(frame);
			}
		}

		// reading of the embedded markov model
		MarkovModel_impl<Integer> mm = this.readMarkovModel(
				hmmElement.getFirstChildElement("mm"), idMap);
		hmm.setTransitionProbabilities(mm.getTransitionProbabilities());
		hmm.setFinalStates(mm.getFinalStates());
		hmm.setStartingProbabilities(mm.getStartingProbabilities());
		hmm.setStates(mm.getStates());

		// reading of emission probabilities
		chElem = hmmElement.getChildElements("prob");
		for (int i = 0; i < chElem.size(); i++) {
			Element probElement = chElem.get(i);

			Integer state = (Integer) idMap.get(probElement
					.getAttributeValue("from"));
			FrameTokenEvent_impl frame = (FrameTokenEvent_impl) idMap.get(probElement
					.getAttributeValue("to"));
			Probability p = Probability.fromProbability(Double
					.parseDouble(probElement.getAttributeValue("p")));
			hmm.getEmissionProbabilities().put(state, frame, p);
		}

		return hmm;
	}

	protected MarkovModel_impl<Integer> readMarkovModel(final Element element,
			final Map<String, Object> idMap) {
		MarkovModel_impl<Integer> mm = new MarkovModel_impl<Integer>();
		Elements chElem = element.getChildElements("state");
		for (int i = 0; i < chElem.size(); i++) {
			Element stateElement = chElem.get(i);

			String id = stateElement.getAttributeValue("id");
			String num = stateElement.getAttributeValue("integer");
			Integer state = Integer.valueOf(num);
			idMap.put(id, state);
			mm.getStates().add(state);
			if (stateElement.getAttribute("final") != null) {
				mm.getFinalStates().add(state);
			}
		}
		chElem = element.getChildElements("prob");
		for (int i = 0; i < chElem.size(); i++) {

			Element probElement = chElem.get(i);
			if (probElement.getAttribute("from") != null) {
				Integer from = (Integer) idMap.get(probElement
						.getAttributeValue("from"));
				Integer to = (Integer) idMap.get(probElement
						.getAttributeValue("to"));
				Probability p = Probability.fromProbability(Double
						.parseDouble(probElement.getAttributeValue("p")));
				mm.getTransitionProbabilities().put(from, to, p);
			} else {
				Integer to = (Integer) idMap.get(probElement
						.getAttributeValue("to"));
				Probability p = Probability.fromProbability(Double
						.parseDouble(probElement.getAttributeValue("p")));
				mm.getStartingProbabilities().put(to, p);
			}
		}
		return mm;
	}

	protected File searchFileForRitualDocument(final String name)
			throws FileNotFoundException {
		if (this.searchSubDirectories) {
			return this.searchFileForRitualDocument(ritualDocumentsDirectory,
					name);
		} else {
			return new File(this.ritualDocumentsDirectory, name);
		}
	}

	protected File searchFileForRitualDocument(final File directory,
			final String name) throws FileNotFoundException {
		if (new File(directory, name).exists())
			return new File(directory, name);
		else
			for (File f : directory.listFiles()) {
				if (f.isDirectory())
					try {
						return this.searchFileForRitualDocument(f, name);
					} catch (FileNotFoundException e) {
						// silently catching
					}
			}
		throw new FileNotFoundException(name);
	}
}
