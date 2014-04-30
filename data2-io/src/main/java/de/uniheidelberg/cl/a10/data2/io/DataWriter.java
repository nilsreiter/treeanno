package de.uniheidelberg.cl.a10.data2.io;

import java.io.OutputStream;

import nu.xom.Attribute;
import nu.xom.Element;
import de.uniheidelberg.cl.a10.data2.AnnotationObject;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Chunk;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mantra;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Section;
import de.uniheidelberg.cl.a10.data2.Sense;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.io.AbstractXMLWriter;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class DataWriter extends
		AbstractXMLWriter<de.uniheidelberg.cl.a10.data2.Document> {

	public DataWriter(OutputStream os) {
		super(os);
	}

	protected Element getElement(final Token token) {
		Element tokenElement = new Element("token");
		tokenElement
				.addAttribute(new Attribute(XMLConstants.ID, token.getId()));
		tokenElement.addAttribute(new Attribute(XMLConstants.WORD, token
				.getSurface()));
		tokenElement.addAttribute(new Attribute(XMLConstants.LEMMA, token
				.getLemma()));
		if (token.getDependencyRelation() != null)
			tokenElement.addAttribute(new Attribute(
					XMLConstants.DEPENDENCYRELATION, token
							.getDependencyRelation()));
		if (token.getSense() != null) {
			tokenElement.addAttribute(new Attribute(XMLConstants.SENSE, token
					.getSense().getId()));
		}
		tokenElement.addAttribute(new Attribute(XMLConstants.BEGIN, String
				.valueOf(token.getBegin())));
		tokenElement.addAttribute(new Attribute(XMLConstants.END, String
				.valueOf(token.getEnd())));
		if (token.getGovernor() != null)
			tokenElement.addAttribute(new Attribute(XMLConstants.GOVERNOR,
					token.getGovernor().getId()));
		tokenElement.addAttribute(new Attribute(XMLConstants.PARTOFSPEECH,
				token.getPartOfSpeech()));
		if (token.getDependencyRelation() != null)
			tokenElement.addAttribute(new Attribute(
					XMLConstants.DEPENDENCYRELATION, token
							.getDependencyRelation()));
		for (Frame frame : token.getFrames()) {
			tokenElement.appendChild(this.getRefElement("frame", frame));
		}

		if (token.getOldId() != null) {
			tokenElement.addAttribute(new Attribute(XMLConstants.OLDID, token
					.getOldId()));
		}
		return tokenElement;
	}

	protected Element getElement(final Sentence sentence) {
		Element sentenceElement = new Element("sentence");
		sentenceElement.addAttribute(new Attribute("id", sentence.getId()));
		for (Token token : sentence) {
			sentenceElement.appendChild(this.getElement(token));
		}
		return sentenceElement;
	}

	protected Element getRefElement(final String name,
			final AnnotationObject dObj) {
		Element element = new Element(name);
		element.addAttribute(new Attribute(XMLConstants.IDREF, dObj.getId()));
		return element;
	}

	protected Element getElement(final Mention mention) {
		Element mentionElement = new Element("mention");
		mentionElement.addAttribute(new Attribute(XMLConstants.ID, mention
				.getId()));

		// target tokens
		for (Token token : mention.getTokens()) {
			mentionElement.appendChild(this.getRefElement("token", token));
		}

		// corresponding frame elements
		for (FrameElement fe : mention.getFrameElms()) {
			mentionElement.appendChild(this.getRefElement("fe", fe));
		}

		return mentionElement;
	}

	protected Element getElement(final Entity entity) {
		Element entityElement = new Element("entity");

		if (entity.getSense() != null) {
			Element senseRef = new Element("sense");
			senseRef.addAttribute(new Attribute(XMLConstants.IDREF, entity
					.getSense().getId()));
			entityElement.appendChild(senseRef);
		}
		entityElement.addAttribute(new Attribute(XMLConstants.ID, entity
				.getId()));

		for (Mention mention : entity) {
			entityElement.appendChild(this.getElement(mention));
		}
		return entityElement;
	}

	protected Element getElement(final Mantra mantra) {
		Element element = new Element("mantra");
		element.addAttribute(new Attribute(XMLConstants.ID, mantra.getId()));
		if (mantra.firstToken() != null)
			element.appendChild(this.getRefElement("token", mantra.firstToken()));
		return element;
	}

	protected Element getElement(final FrameElement fe) {
		Element element = new Element("frame_element");
		element.addAttribute(new Attribute(XMLConstants.ID, fe.getId()));
		element.addAttribute(new Attribute("name", fe.getName()));
		if (fe.getHead() != null)
			element.appendChild(this.getRefElement("head", fe.getHead()));
		for (Token token : fe) {
			if (token == null || token.getId() == null) {
				System.err.println(".");
			}
			element.appendChild(this.getRefElement("token", token));
		}

		for (Mention mention : fe.getMentions()) {
			element.appendChild(this.getRefElement("mention", mention));
		}

		return element;
	}

	protected Element getElement(final Frame frame) {
		Element element = new Element("frame");
		element.addAttribute(new Attribute(XMLConstants.ID, frame.getId()));
		if (frame.getOldId() != null)
			element.addAttribute(new Attribute(XMLConstants.OLDID, frame
					.getOldId()));
		element.addAttribute(new Attribute("name", frame.getFrameName()));
		element.appendChild(this.getRefElement("token", frame.getTokens()
				.get(0)));

		for (FrameElement fe : frame.getFrameElms()) {
			element.appendChild(this.getElement(fe));
		}

		return element;
	}

	protected Element getElement(final Section section) {
		Element element = new Element("section");
		element.addAttribute(new Attribute(XMLConstants.ID, section.getId()));

		for (Sentence sentence : section) {
			element.appendChild(this.getRefElement("sentence", sentence));
		}

		return element;
	}

	@Override
	public Element getElement(final de.uniheidelberg.cl.a10.data2.Document text) {

		Element rootElement = new Element("root");
		// rootElement.add(Namespace
		// .get("http://www.cl.uni-heidelberg.de/~reiter/RitualDocument"));

		Element textElement = new Element("document");

		Element origElement = new Element("originaltext");
		origElement.appendChild(text.getOriginalText());

		textElement.addAttribute(new Attribute(XMLConstants.ID, text.getId()));
		textElement.appendChild(origElement);

		if (text.getTitle() != null) {
			textElement.addAttribute(new Attribute(XMLConstants.TITLE, text
					.getTitle()));
		}

		rootElement.appendChild(textElement);

		// Sentences
		Element sentencesElement = new Element("sentences");
		for (Sentence sentence : text.getSentences()) {
			sentencesElement.appendChild(this.getElement(sentence));
		}

		// Coreference
		Element corefElement = new Element("coreference");
		for (Entity entity : text.getEntities()) {
			corefElement.appendChild(this.getElement(entity));
		}
		Element singletonElements = new Element("singletons");
		for (Mention mention : text.getMentions()) {
			if (mention.getEntity() == null) {
				singletonElements.appendChild(this.getElement(mention));
			}
		}
		corefElement.appendChild(singletonElements);

		// Frames + Semantic Roles
		Element framesElement = new Element("frames");
		for (Frame frame : text.getFrames()) {
			framesElement.appendChild(this.getElement(frame));
		}
		Element frameOrderingTemporal = new Element("order");
		frameOrderingTemporal.addAttribute(new Attribute("type", "temporal"));
		for (Frame frame : text.getFramesInTemporalOrdering()) {
			frameOrderingTemporal.appendChild(this
					.getRefElement("frame", frame));
		}

		/*
		 * Element frameOrderingTextual = new DefaultElement("order");
		 * frameOrderingTextual.addAttribute("type", "textual");
		 * 
		 * framesElement.add(frameOrderingTextual);
		 */
		framesElement.appendChild(frameOrderingTemporal);

		// Chunks
		Element chunksElement = new Element("chunks");
		for (Sentence sentence : text.getSentences()) {
			for (Chunk chunk : sentence.getChunks()) {
				chunksElement.appendChild(this.getElement(chunk));
			}
		}

		// Sections
		Element sectionsElement = new Element("sections");
		for (Section section : text.getSections()) {
			sectionsElement.appendChild(this.getElement(section));
		}

		// Senses
		Element sensesElement = new Element("senses");
		for (Sense sense : text.getSenses()) {
			sensesElement.appendChild(this.getElement(sense));
		}

		// Mantras
		Element mantrasElement = new Element("mantras");
		for (Mantra mantra : text.getMantras()) {
			mantrasElement.appendChild(this.getElement(mantra));
		}

		// Events
		Element eventsElement = new Element("events");
		for (Event event : text.getEvents()) {
			eventsElement.appendChild(this.getElement(event));

		}

		textElement.appendChild(sentencesElement);
		textElement.appendChild(corefElement);
		textElement.appendChild(framesElement);
		textElement.appendChild(chunksElement);
		textElement.appendChild(sectionsElement);
		textElement.appendChild(sensesElement);
		textElement.appendChild(mantrasElement);
		textElement.appendChild(eventsElement);

		return rootElement;
	}

	protected Element getElement(Event event) {
		Element element = new Element(XMLConstants.EVENT);
		element.addAttribute(new Attribute(XMLConstants.ID, event.getId()));
		element.addAttribute(new Attribute(XMLConstants.CLASS, event
				.getEventClass()));
		element.appendChild(getRefElement(XMLConstants.ANCHOR,
				event.getAnchor()));
		for (String argKey : event.getArguments().keySet()) {
			Element argElement = new Element(XMLConstants.ARGUMENT);
			argElement.addAttribute(new Attribute(XMLConstants.ROLE, argKey));
			for (AnnotationObjectInDocument aoi : event.getArguments().get(
					argKey)) {
				argElement.appendChild(getRefElement(XMLConstants.TARGET, aoi));
			}
			element.appendChild(argElement);
		}
		return element;
	}

	protected Element getElement(final Sense sense) {
		Element element = new Element("sense");
		element.addAttribute(new Attribute(XMLConstants.ID, sense.getId()));
		element.addAttribute(new Attribute(XMLConstants.WORDNETID, sense
				.getWordNetId()));
		return element;
	}

	protected Element getElement(final Chunk chunk) {
		Element element = new Element("chunk");
		element.addAttribute(new Attribute(XMLConstants.ID, chunk.getId()));
		element.addAttribute(new Attribute(XMLConstants.CATEGORY, chunk
				.getCategory()));
		element.addAttribute(new Attribute("sentence", chunk.getSentence()
				.getId()));
		for (Token token : chunk) {
			element.appendChild(this.getRefElement("token", token));
		}

		return element;
	}

}
