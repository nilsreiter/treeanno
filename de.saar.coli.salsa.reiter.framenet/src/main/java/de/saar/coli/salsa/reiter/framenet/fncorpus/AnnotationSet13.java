package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.List;

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.jaxen.JaxenException;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.uniheidelberg.cl.reiter.util.CharacterRange;

public class AnnotationSet13 extends AnnotationSet {

    /**
     * The constructor used for constructing the AnnotationSet object out of an
     * XML element node.
     * 
     * @param sentence
     *            The sentence which is annotated
     * @param element
     *            The XML element representing the annotation set
     * @throws FrameNotFoundException
     * @throws FrameElementNotFoundException
     * @throws ParsingException
     */
    public AnnotationSet13(final Sentence sentence, final Element element)
	    throws FrameNotFoundException, FrameElementNotFoundException,
	    ParsingException {
	this.sentence = sentence;
	init(element);
	frameRef = element.attributeValue("frameRef");
	luRef = element.attributeValue("lexUnitRef");
	String frameName = element.attributeValue("frameName");
	String luName = element.attributeValue("luName");
	if (frameName != null) {
	    frame = sentence.getCorpus().getFrameNet().getFrame(frameName);
	}
	if (luName != null) {
	    lexicalUnit = frame.getLexicalUnit(luName);
	}

	Element targetlabel =
		(Element) element
			.selectSingleNode("layers/layer[@name='Target']/labels/label");
	if (targetlabel != null && targetlabel.attributeValue("start") != null) {
	    int start = Integer.parseInt(targetlabel.attributeValue("start"));
	    int end = Integer.parseInt(targetlabel.attributeValue("end")) + 1;
	    realizedFrame =
		    new RealizedFrame(frame,
			    sentence.getToken(new CharacterRange(start, end)),
			    "");
	}

	processFrameElements(element);
    }

    /**
     * This constructor is to be used with the AnnotationCorpus class.
     * 
     * @param sentence
     *            The sentence
     * @param element
     *            The XML element
     * @param frame
     *            The frame
     * @param lexicalUnit
     *            The lexical unit
     * @throws JaxenException
     * @throws FrameElementNotFoundException
     */
    public AnnotationSet13(final Sentence sentence, final Element element,
	    final Frame frame, final LexicalUnit lexicalUnit)
	    throws FrameElementNotFoundException {
	init(element);
	this.frame = frame;
	this.lexicalUnit = lexicalUnit;
	this.sentence = sentence;

	Element targetlabel =
		(Element) element
			.selectSingleNode("layers/layer[@name='Target']/labels/label");
	if (targetlabel != null && targetlabel.attributeValue("start") != null) {
	    int start = Integer.parseInt(targetlabel.attributeValue("start"));
	    int end = Integer.parseInt(targetlabel.attributeValue("end")) + 1;
	    realizedFrame =
		    new RealizedFrame(frame,
			    sentence.getToken(new CharacterRange(start, end)),
			    "");
	}

	processFrameElements(element);
    }

    protected void processFrameElements(final Element element)
	    throws FrameElementNotFoundException {
	List<?> felabels =
		element.selectNodes("layers/layer[@name='FE']/labels/label");

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
			    if (frame != null) {
				RealizedFrameElement rfe =
					realizedFrame.addRealizedFrameElement(
						fename,
						sentence.getToken(range));

				DefaultAttribute gfAttribute =
					(DefaultAttribute) label
						.selectSingleNode("../../../layer[@name='GF']/labels/label[@start='"
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
						.selectSingleNode("../../../layer[@name='PT']/labels/label[@start='"
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
    }

}
