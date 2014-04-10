package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.xerces.impl.xs.opti.DefaultDocument;
import org.apache.xerces.impl.xs.opti.DefaultElement;
import org.apache.xml.serialize.OutputFormat;

import de.uniheidelberg.cl.a10.data2.Chunk;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.Mantra;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Section;
import de.uniheidelberg.cl.a10.data2.Sense;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class DataWriter {

	OutputStream outputStream;

	public DataWriter(final OutputStream os) {
		this.outputStream = os;
	}

	public void close() throws IOException {
		this.outputStream.flush();
	}

	public void write(final Document text) throws IOException {
		XMLWriter xmlWriter = new XMLWriter(this.outputStream,
				OutputFormat.createPrettyPrint());

		xmlWriter.write(this.getDocument(text));
		xmlWriter.flush();
	}

	protected Element getElement(final Token token) {
		Element tokenElement = new DefaultElement("token");
		tokenElement.addAttribute(XMLConstants.ID, token.getId());
		tokenElement.addAttribute(XMLConstants.WORD, token.getSurface());
		tokenElement.addAttribute(XMLConstants.LEMMA, token.getLemma());
		tokenElement.addAttribute(XMLConstants.DEPENDENCYRELATION,
				token.getDependencyRelation());
		if (token.getSense() != null) {
			tokenElement.addAttribute(XMLConstants.SENSE, token.getSense()
					.getId());
		}
		tokenElement.addAttribute(XMLConstants.BEGIN,
				String.valueOf(token.getBegin()));
		tokenElement.addAttribute(XMLConstants.END,
				String.valueOf(token.getEnd()));
		if (token.getGovernor() != null)
			tokenElement.addAttribute(XMLConstants.GOVERNOR, token
					.getGovernor().getId());
		tokenElement.addAttribute(XMLConstants.PARTOFSPEECH,
				token.getPartOfSpeech());
		tokenElement.addAttribute(XMLConstants.DEPENDENCYRELATION,
				token.getDependencyRelation());
		for (Frame frame : token.getFrames()) {
			tokenElement.add(this.getRefElement("frame", frame));
		}

		if (token.getOldId() != null) {
			tokenElement.addAttribute(XMLConstants.OLDID, token.getOldId());
		}
		return tokenElement;
	}

	protected Element getElement(final Sentence sentence) {
		Element sentenceElement = new DefaultElement("sentence");
		sentenceElement.addAttribute("id", sentence.getId());
		for (Token token : sentence) {
			sentenceElement.add(this.getElement(token));
		}
		return sentenceElement;
	}

	protected Element getRefElement(final String name, final DataObject dObj) {
		Element element = new DefaultElement(name);
		element.addAttribute(XMLConstants.IDREF, dObj.getId());
		return element;
	}

	protected Element getElement(final Mention mention) {
		Element mentionElement = new DefaultElement("mention");
		mentionElement.addAttribute(XMLConstants.ID, mention.getId());

		// target tokens
		for (Token token : mention.getTokens()) {
			mentionElement.add(this.getRefElement("token", token));
		}

		// corresponding frame elements
		for (FrameElm fe : mention.getFrameElms()) {
			mentionElement.add(this.getRefElement("fe", fe));
		}

		return mentionElement;
	}

	protected Element getElement(final Entity entity) {
		Element entityElement = new DefaultElement("entity");

		if (entity.getSense() != null) {
			Element senseRef = new DefaultElement("sense");
			senseRef.addAttribute(XMLConstants.IDREF, entity.getSense().getId());
			entityElement.add(senseRef);
		}
		entityElement.addAttribute(XMLConstants.ID, entity.getId());

		for (Mention mention : entity) {
			entityElement.add(this.getElement(mention));
		}
		return entityElement;
	}

	protected Element getElement(final Mantra mantra) {
		Element element = new DefaultElement("mantra");
		element.addAttribute(XMLConstants.ID, mantra.getId());
		if (mantra.firstToken() != null)
			element.add(this.getRefElement("token", mantra.firstToken()));
		return element;
	}

	protected Element getElement(final FrameElm fe) {
		Element element = new DefaultElement("frame_element");
		element.addAttribute(XMLConstants.ID, fe.getId());
		element.addAttribute("name", fe.getName());
		if (fe.getHead() != null)
			element.add(this.getRefElement("head", fe.getHead()));
		for (Token token : fe) {
			if (token == null || token.getId() == null) {
				System.err.println(".");
			}
			element.add(this.getRefElement("token", token));
		}

		for (Mention mention : fe.getMentions()) {
			element.add(this.getRefElement("mention", mention));
		}

		return element;
	}

	protected Element getElement(final Frame frame) {
		Element element = new DefaultElement("frame");
		element.addAttribute(XMLConstants.ID, frame.getId());
		element.addAttribute(XMLConstants.OLDID, frame.getOldId());
		element.addAttribute("name", frame.getFrameName());
		element.add(this.getRefElement("token", frame.getTokens().get(0)));

		for (FrameElm fe : frame.getFrameElms()) {
			element.add(this.getElement(fe));
		}

		return element;
	}

	protected Element getElement(final Section section) {
		Element element = new DefaultElement("section");
		element.addAttribute(XMLConstants.ID, section.getId());

		for (Sentence sentence : section) {
			element.add(this.getRefElement("sentence", sentence));
		}

		return element;
	}

	protected Document getDocument(final RitualDocument text) {
		Document textDoc = new DefaultDocument();

		Element rootElement = new DefaultElement("root");
		// rootElement.add(Namespace
		// .get("http://www.cl.uni-heidelberg.de/~reiter/RitualDocument"));

		Element textElement = new DefaultElement("document");

		Element origElement = new DefaultElement("originaltext");
		origElement.addCDATA(text.getOriginalText());

		textElement.addAttribute("id", text.getId());
		textElement.add(origElement);

		if (text.getTitle() != null) {
			textElement.addAttribute(XMLConstants.TITLE, text.getTitle());
		}

		textDoc.setRootElement(rootElement);
		rootElement.add(textElement);

		// Sentences
		Element sentencesElement = new DefaultElement("sentences");
		for (Sentence sentence : text.getSentences()) {
			sentencesElement.add(this.getElement(sentence));
		}

		// Coreference
		Element corefElement = new DefaultElement("coreference");
		for (Entity entity : text.getEntities()) {
			corefElement.add(this.getElement(entity));
		}
		Element singletonElements = new DefaultElement("singletons");
		for (Mention mention : text.getMentions()) {
			if (mention.getEntity() == null) {
				singletonElements.add(this.getElement(mention));
			}
		}
		corefElement.add(singletonElements);

		// Frames + Semantic Roles
		Element framesElement = new DefaultElement("frames");
		for (Frame frame : text.getFrames()) {
			framesElement.add(this.getElement(frame));
		}
		Element frameOrderingTemporal = new DefaultElement("order");
		frameOrderingTemporal.addAttribute("type", "temporal");
		for (Frame frame : text.getFramesInTemporalOrdering()) {
			frameOrderingTemporal.add(this.getRefElement("frame", frame));
		}

		/*
		 * Element frameOrderingTextual = new DefaultElement("order");
		 * frameOrderingTextual.addAttribute("type", "textual");
		 * 
		 * framesElement.add(frameOrderingTextual);
		 */
		framesElement.add(frameOrderingTemporal);

		// Chunks
		Element chunksElement = new DefaultElement("chunks");
		for (Sentence sentence : text.getSentences()) {
			for (Chunk chunk : sentence.getChunks()) {
				chunksElement.add(this.getElement(chunk));
			}
		}

		// Sections
		Element sectionsElement = new DefaultElement("sections");
		for (Section section : text.getSections()) {
			sectionsElement.add(this.getElement(section));
		}

		// Senses
		Element sensesElement = new DefaultElement("senses");
		for (Sense sense : text.getSenses()) {
			sensesElement.add(this.getElement(sense));
		}

		// Mantras
		Element mantrasElement = new DefaultElement("mantras");
		for (Mantra mantra : text.getMantras()) {
			mantrasElement.add(this.getElement(mantra));
		}

		textElement.add(sentencesElement);
		textElement.add(corefElement);
		textElement.add(framesElement);
		textElement.add(chunksElement);
		textElement.add(sectionsElement);
		textElement.add(sensesElement);
		textElement.add(mantrasElement);

		// System.err.println(textDoc.asXML());

		return textDoc;
	}

	protected Element getElement(final Sense sense) {
		Element element = new DefaultElement("sense");
		element.addAttribute(XMLConstants.ID, sense.getId());
		element.addAttribute(XMLConstants.WORDNETID, sense.getWordNetId());
		return element;
	}

	protected Element getElement(final Chunk chunk) {
		Element element = new DefaultElement("chunk");
		element.addAttribute(XMLConstants.ID, chunk.getId());
		element.addAttribute(XMLConstants.CATEGORY, chunk.getCategory());
		element.addAttribute("sentence", chunk.getSentence().getId());
		for (Token token : chunk) {
			element.add(this.getRefElement("token", token));
		}

		return element;
	}

	/*
	 * protected Element getElement(final PairwiseAlignment<FrameElm> feAlign) {
	 * Element feAlignElement = new DefaultElement("fealignment"); if
	 * (feAlign.getElement1() != null) { feAlignElement.addAttribute(FE1,
	 * feAlign.getElement1().getId()); } if (feAlign.getElement2() != null) {
	 * feAlignElement.addAttribute(FE2, feAlign.getElement2().getId()); }
	 * feAlignElement.addAttribute(SCORE, Double.toString(feAlign.getScore()));
	 * return feAlignElement; }
	 */

	protected Element getElement(
			final PairwiseLink_impl<? extends Alignable> frameAlign) {
		Element frameAlignElement = new DefaultElement("framealignment");
		if (frameAlign.getElement1() != null) {
			frameAlignElement.addAttribute(XMLConstants.FRAME1, frameAlign
					.getElement1().getId());
		}
		if (frameAlign.getElement2() != null) {
			frameAlignElement.addAttribute(XMLConstants.FRAME2, frameAlign
					.getElement2().getId());
		}
		frameAlignElement.addAttribute(XMLConstants.SCORE,
				Double.toString(frameAlign.getScore()));
		if (frameAlign.getAlignmentType() != null)
			frameAlignElement.addAttribute(XMLConstants.TYPE, frameAlign
					.getAlignmentType().toString());
		if (frameAlign.getStatus() != null)
			frameAlignElement.addAttribute("status", frameAlign.getStatus()
					.toString());
		if (frameAlign.getDescription() != null)
			frameAlignElement.addAttribute(XMLConstants.DESCRIPTION,
					frameAlign.getDescription());
		// add fealignments
		/*
		 * for (PairwiseAlignment<FrameElm> feAlign : frameAlign
		 * .getFrameElmAlignment()) {
		 * frameAlignElement.add(this.getElement(feAlign)); }
		 */

		// individual similarity scores
		/*
		 * for (Class<? extends SimilarityFunction<Frame>> fun : frameAlign
		 * .keySetSimilarity()) { Element e = new
		 * DefaultElement("similarityscore"); e.addAttribute("name",
		 * fun.getCanonicalName()); e.addAttribute("score",
		 * frameAlign.getSimilarity(fun).toString()); frameAlignElement.add(e);
		 * }
		 */

		return frameAlignElement;
	}

}
