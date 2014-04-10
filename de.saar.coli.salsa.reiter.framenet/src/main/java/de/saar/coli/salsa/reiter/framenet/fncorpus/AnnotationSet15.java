package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.List;

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IToken;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.uniheidelberg.cl.reiter.util.CharacterRange;

public class AnnotationSet15 extends AnnotationSet {
    public AnnotationSet15(final Sentence15 sentence, final Element setElement,
	    final LexicalUnit lexicalUnit, final Frame frame)
	    throws FrameNotFoundException, FrameElementNotFoundException,
	    ParsingException {
	status = setElement.attributeValue("status");
	id = setElement.attributeValue("ID");
	init(setElement);

	this.frame = frame;
	this.lexicalUnit = lexicalUnit;
	this.sentence = sentence;

	Element targetlabel =
		(Element) setElement
			.selectSingleNode("fn:layer[@name='Target']/fn:label");
	if (targetlabel != null && targetlabel.attributeValue("start") != null) {
	    int start = Integer.parseInt(targetlabel.attributeValue("start"));
	    int end = Integer.parseInt(targetlabel.attributeValue("end")) + 1;
	    realizedFrame =
		    new RealizedFrame(frame,
			    sentence.getTokens(new CharacterRange(start, end)),
			    "");
	    realizedFrame.setSentence(sentence);
	}

	if (setElement.attributeValue("status").equals("MANUAL")) {
	    processFrameElements(setElement);
	}
    }

    /**
     * TODO: The exception needs to be refined.
     * 
     * @param sentence
     * @param rf
     * @throws Exception
     */
    public AnnotationSet15(final Sentence15 sentence, final RealizedFrame rf)
	    throws Exception {
	if (sentence.getSubCorpus().getAnnotatedLexicalUnit().getFrame() == null) {
	    sentence.getSubCorpus().getAnnotatedLexicalUnit()
		    .setFrame(rf.getFrame());
	}
	if (rf.getFrame() != sentence.getSubCorpus().getAnnotatedLexicalUnit()
		.getFrame()) {
	    throw new Exception("Frame "
		    + rf.getFrame().toString()
		    + " does not match to frame of lexical unit ("
		    + sentence.getSubCorpus().getAnnotatedLexicalUnit()
			    .getFrame().toString() + ").");
	}
	this.sentence = sentence;
	this.frame = rf.getFrame();
	this.realizedFrame = rf;
	this.realizedFrame.setSentence(sentence);
	// TODO: We should do some sort of checking, whether the realized frame
	// matches the sentences etc.
    }

    // TODO: This method needs to be modularized
    protected void processFrameElements(final Element element)
	    throws FrameElementNotFoundException {
	List<?> felabels = element.selectNodes("fn:layer[@name='FE']/fn:label");

	for (Object lab : felabels) {
	    Element label = (Element) lab;
	    if (label != null) {
		if (realizedFrame != null) {
		    try {
			if (label.attributeValue("start") != null
				&& label.attributeValue("end") != null) {
			    int start, end;
			    start =
				    Integer.parseInt(label
					    .attributeValue("start"));
			    end = Integer.parseInt(label.attributeValue("end"));

			    String fename = label.attributeValue("name");
			    CharacterRange range =
				    new CharacterRange(start, end + 1);
			    if (frame != null && start != end) {
				List<IToken> tokenList =
					sentence.getTokenList(range);
				RealizedFrameElement rfe = null;
				if (tokenList.isEmpty()) {
				    rfe =
					    realizedFrame
						    .addRealizedFrameElement(
							    fename,
							    sentence.addToken(range));
				} else {
				    rfe =
					    realizedFrame
						    .addRealizedFrameElement(
							    fename, tokenList);
				}
				DefaultAttribute gfAttribute =
					(DefaultAttribute) label
						.selectSingleNode("../../fn:layer[@name='GF']/fn:label[@start='"
							+ start
							+ "' and @end='"
							+ end
							+ "']/@name");
				if (gfAttribute != null) {
				    rfe.setProperty("GF",
					    gfAttribute.getStringValue());
				}

				DefaultAttribute ptAttribute =
					(DefaultAttribute) label
						.selectSingleNode("../../fn:layer[@name='PT']/fn:label[@start='"
							+ start
							+ "' and @end='"
							+ end
							+ "']/@name");
				if (gfAttribute != null) {
				    rfe.setProperty("PT",
					    ptAttribute.getStringValue());
				}
			    }

			} else if (label.attributeValue("itype") != null) {
			    String iType = "";
			    iType = label.attributeValue("itype");
			    RealizedFrameElement rfe =
				    new RealizedFrameElement(
					    realizedFrame,
					    realizedFrame
						    .getFrame()
						    .getFrameElement(
							    label.attributeValue("name")));
			    rfe.setNullInstantiated(true);
			    rfe.setIType(iType);
			    realizedFrame.addRealizedFrameElement(rfe);
			}
		    } catch (FrameElementNotFoundException e) {
			sentence.getCorpus().getLogger().severe(e.getMessage());
			if (sentence.getCorpus().isAbortOnError()) {
			    throw new FrameElementNotFoundException(
				    e.getFrame(), e.getFrameElement());
			}
		    }
		}
	    }
	}
    };
}
