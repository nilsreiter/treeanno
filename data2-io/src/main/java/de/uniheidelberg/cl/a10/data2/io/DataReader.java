package de.uniheidelberg.cl.a10.data2.io;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import nu.xom.Element;
import nu.xom.Elements;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.impl.Chunk_impl;
import de.uniheidelberg.cl.a10.data2.impl.Document_impl;
import de.uniheidelberg.cl.a10.data2.impl.Entity_impl;
import de.uniheidelberg.cl.a10.data2.impl.FrameElm_impl;
import de.uniheidelberg.cl.a10.data2.impl.Frame_impl;
import de.uniheidelberg.cl.a10.data2.impl.Mantra_impl;
import de.uniheidelberg.cl.a10.data2.impl.Mention_impl;
import de.uniheidelberg.cl.a10.data2.impl.Section_impl;
import de.uniheidelberg.cl.a10.data2.impl.Sense_impl;
import de.uniheidelberg.cl.a10.data2.impl.Sentence_impl;
import de.uniheidelberg.cl.a10.data2.impl.Token_impl;
import de.uniheidelberg.cl.a10.io.AbstractXMLReader;
import de.uniheidelberg.cl.a10.io.XMLConstants;

/**
 * This class implements methods for reading ritual annotations from an xml file
 * into a RitualDocument object.
 * 
 * @author hartmann
 * 
 */
public class DataReader extends AbstractXMLReader<Document_impl> {

	private Document_impl ritDoc = null;

	public DataReader() {

	}

	private void extractMantras(final Element doc_elm) {
		if (doc_elm.getChildElements("mantras") != null) {
			Elements mantraElements = doc_elm.getChildElements("mantras")
					.get(0).getChildElements("mantra");
			for (int i = 0; i < mantraElements.size(); i++) {
				Element mantra_element = mantraElements.get(i);
				if (mantra_element.getChildElements("token") != null) {
					Mantra_impl mantra = new Mantra_impl(
							mantra_element.getAttributeValue("id"),
							this.ritDoc.getTokenById(mantra_element
									.getChildElements("token").get(0)
									.getAttributeValue("idref")));
					this.ritDoc.addMantra(mantra);
				}
			}
		}
	}

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

	private void extractSingletonMentions(final Element doc_elm) {
		for (Element men_element : getElements(doc_elm, "coreference",
				"singletons", "mention")) {
			this.extractMention(men_element, null);
		}
	}

	/**
	 * Extracts the governor for each Token (if it exists)
	 * 
	 * @param doc_elm
	 */
	private void setGovernors(final Element doc_elm) {
		for (Element sent_elm : getElements(doc_elm, "sentences", "sentence")) {
			for (Element tok_elm : getElements(sent_elm, "token")) {
				if (tok_elm.getAttributeValue("governor") == null)
					continue;
				Token_impl governor = this.ritDoc.getTokenById(tok_elm
						.getAttribute("governor").getValue());
				this.ritDoc.getTokenById(tok_elm.getAttributeValue("id"))
						.setGovernor(governor);
			}
		}
	}

	/**
	 * Extracts all Senses from a dom4j tree
	 */
	private void extractSenses(final Element doc_elm) {
		for (Element sense_elm : getElements(doc_elm, "senses", "sense")) {
			Sense_impl sense = new Sense_impl(sense_elm.getAttributeValue("id"));
			this.ritDoc.addSense(sense);
			if (sense_elm.getAttribute("wordnet") != null) {
				sense.setWordNetId(sense_elm.getAttributeValue("wordnet"));
			}
		}

	}

	/**
	 * Extracts all FrameElements from a dom4j tree
	 */
	private void extractFrameElements(final Element doc_elm) {
		for (Element frame_elm : doc_elm.element("frames").elements("frame")) {
			for (Element fe_elm : frame_elm.elements("frame_element")) {
				FrameElm_impl fe = new FrameElm_impl(fe_elm.attribute("id")
						.getValue());
				this.ritDoc.addFrameElm(fe);
				fe.setRitualDocument(ritDoc);
				// add tokens to frame element
				TreeSet<Token> toks = new TreeSet<Token>();
				for (Element tok_elm : fe_elm.elements("token")) {
					toks.add(this.ritDoc.getTokenById(tok_elm
							.attribute("idref").getValue()));
					this.ritDoc.getTokenById(
							tok_elm.attribute("idref").getValue()).addFrameElm(
							fe);
				}
				fe.setTokens(toks);
				if (fe_elm.element("head") != null) {
					fe.setHead(this.ritDoc.getTokenById(fe_elm.element("head")
							.attributeValue("idref")));
				}
			}
		}
	}

	/**
	 * Extracts a Sentence from a dom4j tree and adds it to the Text object
	 * 
	 * @param sent_elm
	 *            A sentence element
	 */
	private void extractSentence(final Element sent_elm) {
		Sentence_impl sent = new Sentence_impl(sent_elm.attribute("id")
				.getValue());
		this.ritDoc.addSentence(sent);
		sent.setRitualDocument(ritDoc);
		for (Element tok_elm : sent_elm.elements("token")) {
			Token_impl tok = new Token_impl(tok_elm.attribute("id").getValue(),
					tok_elm.attribute("word").getValue());
			this.ritDoc.addToken(tok);
			sent.add(tok);
			// add attributes
			tok.setBegin(Integer.parseInt(tok_elm.attribute(
					"characterOffsetBegin").getValue()));
			tok.setEnd(Integer.parseInt(tok_elm.attribute("characterOffsetEnd")
					.getValue()));
			tok.setPartOfSpeech(tok_elm.attribute("pos").getValue());
			if (tok_elm.attribute("OldId") != null)
				tok.setOldId(tok_elm.attributeValue("OldId"));
			if (tok_elm.attribute("deprel") != null) {
				tok.setDependencyRelation(tok_elm.attribute("deprel")
						.getValue());
			}
			tok.setLemma(tok_elm.attribute("lemma").getValue());
			// tok.setTokenPosition(Integer.parseInt(tok_elm.attribute("token_pos").getValue()));
			if (tok_elm.attribute("sense") != null) {
				tok.setSense(this.ritDoc.getSenseById(tok_elm
						.attribute("sense").getValue()));
			}
			tok.setSentence(sent);
			tok.setRitualDocument(ritDoc);

		}
	}

	/**
	 * Extracts all Sentences in a dom4j tree and adds them to the Text object
	 */
	private void extractSentences(final Element doc_elm) {
		for (Element sent_element : doc_elm.element("sentences").elements(
				"sentence")) {
			this.extractSentence(sent_element);
		}
	}

	/**
	 * Extracts an Entity from a dom4j tree and adds it to the Text object
	 * 
	 * @param ent_elm
	 *            An entity element
	 */
	private void extractEntity(final Element ent_elm) {
		Entity_impl ent = new Entity_impl(ent_elm.attribute("id").getValue());
		if (ent_elm.element("sense") != null) {
			ent.setSense(this.ritDoc.getSenseById(ent_elm.element("sense")
					.attribute("idref").getValue()));
		}
		for (Element men_elm : ent_elm.elements("mention")) {
			this.extractMention(men_elm, ent);
		}
		this.ritDoc.addEntity(ent);
	}

	private void extractMention(final Element men_elm, final Entity_impl ent) {
		Mention_impl men = new Mention_impl(men_elm.attribute("id").getValue());
		this.ritDoc.addMention(men);
		if (ent != null) {
			ent.addMention(men);
			men.setEntity(ent);
		}
		men.setRitualDocument(ritDoc);
		TreeSet<Token> toks = new TreeSet<Token>();
		// add tokens to mention
		for (Element tok_elm : men_elm.elements("token")) {
			Token_impl token = this.ritDoc.getTokenById(tok_elm.attribute(
					"idref").getValue());
			toks.add(token);
			token.addMention(men);
		}
		men.setTokens(toks);
		// add frame elements
		for (Element fe_elm : men_elm.elements("fe")) {
			men.add(this.ritDoc.getFrameElmById(fe_elm.attribute("idref")
					.getValue()));
		}
	}

	/**
	 * Extracts all Entities from a dom4j tree and adds them to the Text object
	 * 
	 * @param doc_elm
	 */
	private void extractEntities(final Element doc_elm) {
		for (Element ent_element : doc_elm.element("coreference").elements(
				"entity")) {
			this.extractEntity(ent_element);
		}
	}

	/**
	 * Extracts a Frame object from a dom4j tree and adds it to the Text Object
	 * 
	 * @param frame_elm
	 *            A frame element
	 * 
	 */
	private void extractFrame(final Element frame_elm) {
		Frame_impl frame = new Frame_impl(frame_elm.attribute("id").getValue());
		frame.setFrameName(frame_elm.attribute("name").getValue());
		if (frame_elm.attribute("OldId") != null)
			frame.setOldId(frame_elm.attributeValue("OldId"));
		this.ritDoc.addFrame(frame);
		Token_impl targetToken = this.ritDoc.getTokenById(frame_elm
				.element("token").attribute("idref").getValue());
		targetToken.addFrame(frame);
		frame.add(targetToken);
		frame.setRitualDocument(ritDoc);
		for (Element fe_elm : frame_elm.elements("frame_element")) {
			FrameElm_impl fe = this.ritDoc.getFrameElmById(fe_elm.attribute(
					"id").getValue());
			frame.addFrameElm(fe);
			fe.setName(fe_elm.attribute("name").getValue());
			fe.setFrame(frame);
			// add corresponding mentions to frame element
			for (Element men_elm : fe_elm.elements("mention")) {
				fe.add(this.ritDoc.getMentionById(men_elm.attribute("idref")
						.getValue()));
			}
		}
		// frame.setRitualDocument(ritDoc);
	}

	/**
	 * Extracts all Frames from a dom4j tree and adds them to the Text object
	 * 
	 * @param doc_elm
	 */
	private void extractFrames(final Element doc_elm) {
		for (Element frame_element : doc_elm.element("frames")
				.elements("frame")) {
			this.extractFrame(frame_element);
		}
		// Temporal ordering
		for (Element orderElement : doc_elm.element("frames").elements("order")) {
			if (orderElement.attributeValue("type")
					.equalsIgnoreCase("temporal")) {
				for (Element frameRef : orderElement.elements("frame")) {
					this.ritDoc.addFrameToTemporalOrdering(ritDoc
							.getFrameById(frameRef.attributeValue("idref")));

				}
			}
		}
	}

	/**
	 * Extracts all Chunks from a dom4j tree and adds them to the corresponding
	 * Sentence object
	 * 
	 * @param doc_elm
	 */
	private void extractChunks(final Element doc_elm) {
		for (Element ch_elm : doc_elm.element("chunks").elements("chunk")) {
			Chunk_impl chunk = new Chunk_impl(ch_elm.attribute("id").getValue());
			chunk.setCategory(ch_elm.attributeValue("category"));
			chunk.setRitualDocument(ritDoc);
			TreeSet<Token> toks = new TreeSet<Token>();
			for (Element tok_elm : ch_elm.elements("token")) {
				toks.add(this.ritDoc.getTokenById(tok_elm.attribute("idref")
						.getValue()));
			}
			chunk.setTokens(toks);
			this.ritDoc.getSentenceById(
					chunk.getTokens().get(0).getSentence().getId()).addChunk(
					chunk);
			// set sentence (sentence of the chunks first token)
			chunk.setSentence(chunk.getTokens().get(0).getSentence());
			this.ritDoc.addChunk(chunk);
		}

	}

	private void extractSections(final Element doc_elm) {
		for (Element sec_elm : doc_elm.element("sections").elements("section")) {
			Section_impl sec = new Section_impl(sec_elm.attribute("id")
					.getValue());
			for (Element sent_elm : sec_elm.elements("sentence")) {
				sec.addSentence(this.ritDoc.getSentenceById(sent_elm.attribute(
						"idref").getValue()));
				this.ritDoc.getSentenceById(
						sent_elm.attribute("idref").getValue()).setSection(sec);
			}
			this.ritDoc.addSection(sec);
		}
	}

	private void setOriginalText(final Element doc_elm) {
		this.ritDoc.setOriginalText(doc_elm.element("originaltext").getText());
	}

	@Override
	protected Document_impl read(final Element root_elm) {
		Element doc_elm = root_elm.element("document");
		this.ritDoc = new Document_impl(doc_elm.attribute("id").getValue());
		if (doc_elm.attribute(XMLConstants.TITLE) != null)
			this.ritDoc.setTitle(doc_elm.attributeValue(XMLConstants.TITLE));
		this.extractSenses(doc_elm);
		this.extractSentences(doc_elm);
		this.setGovernors(doc_elm);
		this.extractFrameElements(doc_elm);
		this.extractEntities(doc_elm);
		this.extractSingletonMentions(doc_elm);
		this.extractFrames(doc_elm);
		this.extractChunks(doc_elm);
		this.extractSections(doc_elm);
		this.extractMantras(doc_elm);
		this.setOriginalText(doc_elm);
		return this.ritDoc;

	}

}
