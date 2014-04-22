package de.uniheidelberg.cl.a10.patterns.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class ModelWriter<T extends HasDocument & HasId> {
	OutputStream outputStream;

	public ModelWriter(final OutputStream os) {
		this.outputStream = os;
	}

	public void close() throws IOException {
		try {
		} finally {
			this.outputStream.close();
		}
	}

	public void write(final SEHiddenMarkovModel_impl<T> hmm) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(this.outputStream);
		writer.write(this.getDocument(hmm).toXML());
		writer.flush();

	}

	public void write(final MarkovModel_impl<Integer> mm) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(this.outputStream);
		writer.write(this.getDocument(mm).toXML());
		writer.flush();

	}

	private Document getDocument(final MarkovModel_impl<Integer> bmm) {
		Document doc = new Document(this.getElement(bmm, new IdMap()));
		return doc;
	}

	private Document getDocument(final SEHiddenMarkovModel_impl<T> hmm) {
		Document doc = new Document(this.getElement(hmm, new IdMap()));
		return doc;
	}

	private Element getElement(final SEHiddenMarkovModel_impl<T> hmm,
			final IdMap idMap) {
		Element hmmElement = new Element("sehmm");

		// properties
		hmmElement.appendChild(this.getElement(hmm.getProperties()));

		// training configuration
		if (hmm.getTrainingConfiguration() != null) {
			hmmElement.appendChild(hmm.getTrainingConfiguration().getXML());
		}

		// events
		for (T event : hmm.getEvents()) {
			if (event != null) {
				idMap.newId("event", event);
				Element eventElement = new Element("event");

				eventElement.appendChild(new Attribute("frame", event.getId()));
				eventElement.appendChild(new Attribute("id", idMap.get(event
						.hashCode())));
				if (event.getRitualDocument() != null)
					eventElement.appendChild(new Attribute("source", event
							.getRitualDocument().getId()));
				if (event == hmm.getStartSymbol()) {
					eventElement.appendChild(new Attribute("type", "start"));
				} else if (event == hmm.getEndSymbol()) {
					eventElement.appendChild(new Attribute("type", "end"));
				}
				hmmElement.appendChild(eventElement);
			}
		}

		// markov model
		hmmElement.appendChild(this.getElement(hmm.getMM(), idMap));

		// probabilities
		for (Integer state : hmm.getStates()) {
			for (T event : hmm.getEventsForState(state)) {
				Element probElement = new Element("prob");
				probElement.appendChild(new Attribute("from", idMap.get(state
						.hashCode())));
				probElement.appendChild(new Attribute("to", idMap.get(event
						.hashCode())));
				probElement.appendChild(new Attribute("p", String.valueOf(hmm
						.getEmissionProbabilities().get(state, event)
						.getProbability())));

				hmmElement.appendChild(probElement);

			}
		}

		return hmmElement;
	}

	private Element getElement(final Properties prop) {
		Element propElement = new Element("properties");
		for (Object key : prop.keySet()) {
			Element pElement = new Element("property");
			pElement.appendChild(new Attribute("key", key.toString()));
			pElement.appendChild(new Attribute("value", prop.getProperty(key
					.toString())));
			propElement.appendChild(pElement);
		}
		return propElement;
	}

	private Element getElement(final MarkovModel_impl<Integer> mm,
			final IdMap idMap) {
		Element mmElement = new Element("mm");

		// states
		for (Integer state : mm.getStates()) {
			idMap.newId("state", state);
			Element stateElement = new Element("state");
			stateElement.appendChild(new Attribute("id", idMap.get(state
					.hashCode())));
			stateElement
					.appendChild(new Attribute("integer", state.toString()));
			if (mm.getFinalStates().contains(state)) {
				stateElement.appendChild(new Attribute("final", "final"));
			}
			mmElement.appendChild(stateElement);
		}

		// probabilities
		for (Integer s1 : mm.getStates()) {
			for (Integer s2 : mm.getStates()) {
				if (mm.getTransitionProbabilities().get(s1, s2).isPositive()) {
					Element probElement = new Element("prob");
					probElement.appendChild(new Attribute("from", idMap.get(s1
							.hashCode())));
					probElement.appendChild(new Attribute("to", idMap.get(s2
							.hashCode())));
					probElement.appendChild(new Attribute("p", String
							.valueOf(mm.getTransitionProbabilities()
									.get(s1, s2).getProbability())));

					mmElement.appendChild(probElement);
				}
			}
			if (mm.getStartingProbabilities().get(s1).isPositive()) {
				Element probElement = new Element("prob");
				probElement.appendChild(new Attribute("to", idMap.get(s1
						.hashCode())));
				probElement.appendChild(new Attribute("p", String.valueOf(mm
						.getStartingProbabilities().get(s1).getProbability())));
				mmElement.appendChild(probElement);
			}
		}

		return mmElement;
	}
}
