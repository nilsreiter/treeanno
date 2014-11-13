package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.uniheidelberg.cl.reiter.pos.BNC;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;
import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;

/**
 * 
 * @author reiter
 * 
 */
public class Sentence15 extends Sentence {

    /**
     * The sub corpus to which this sentence belongs.
     */
    SubCorpus15 subCorpus = null;

    /**
     * An identification number for the sentence.
     */
    Integer sentenceNumber = 0;

    /**
     * This field stores, whether this sentence is sound and consistent. Some
     * annotations in the FrameNet database are inconsistent.
     */
    boolean sane = true;

    /**
     * 
     * @param alu
     *            The annotated lexical unit this sentence belongs to
     * @param cr
     *            The CorpusReader object
     * @param frame
     *            The frame appearing in this sentence
     * @param element
     *            The XML element
     * @param theSubCorpus
     *            The sub corpus in which this sentence occurs
     */
    public Sentence15(final AnnotatedLexicalUnit alu, final CorpusReader cr,
	    final Frame frame, final SubCorpus15 theSubCorpus,
	    final Element element) {
	super(element.attributeValue("ID"), element.elementText("text"));
	corpusReader = cr;
	this.subCorpus = theSubCorpus;
	element.addNamespace("fn", "http://framenet.icsi.berkeley.edu");

	Attribute posStyle =
		(Attribute) element
			.selectSingleNode("fn:annotationSet[@status='UNANN']/fn:layer/@name");
	List<?> allTokens = null;

	Class<? extends IPartOfSpeech> posStyleClass = null;
	if (posStyle.getStringValue().equals("BNC")) {
	    posStyleClass = BNC.class;
	    allTokens =
		    element.selectNodes("fn:annotationSet/fn:layer[@name='BNC']/fn:label[@start and @end]");

	} else {
	    posStyleClass = PTB.class;
	    allTokens =
		    element.selectNodes("fn:annotationSet/fn:layer[@name='PENN']/fn:label[@start and @end]");

	}

	for (Object tokenObj : allTokens) {
	    Element token = (Element) tokenObj;
	    CharacterRange range =
		    new CharacterRange(new Integer(
			    token.attributeValue("start")), new Integer(
			    token.attributeValue("end")) + 1);
	    if (token.attributeValue("name") != null
		    && token.attributeValue("name").length() > 0) {

		IPartOfSpeech ipos =
			this.getPartOfSpeech(token, posStyleClass, true);
		addToken(range).setPartOfSpeech(ipos);

	    }
	}

	// sanity check for the token list
	sane = !Range.isOverlapping(this.tokenList.keySet());

	Iterator<?> annotationSetIterator =
		element.elementIterator("annotationSet");
	while (annotationSetIterator.hasNext()) {
	    Element annotationSetElement =
		    (Element) annotationSetIterator.next();
	    try {
		AnnotationSet15 annotationSet =
			new AnnotationSet15(this, annotationSetElement,
				alu.lexicalUnit, alu.frame);
		this.annotationSets.add(annotationSet);
	    } catch (FrameNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (FrameElementNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (ParsingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    /**
     * 
     * @param subCorpus
     *            The sub corpus of this sentence
     * @param text
     *            The tokenized text
     */
    public Sentence15(final SubCorpus15 subCorpus, final String text) {
	super("", text);
	this.subCorpus = subCorpus;
    }

    /**
     * This constructor takes a FrameNetCorpus object and the XML element node
     * representing a single sentence in the data file.
     * 
     * TODO: This method is known to be broken, the token handling is all wrong.
     * 
     * @param reader
     *            The FrameNetCorps
     * @param node
     *            The sentence node
     * @throws FrameElementNotFoundException
     *             If a frame element is not defined
     * @throws FrameNotFoundException
     *             If a frame is not defined
     * @throws ParsingException
     *             If an XML exception occurs while parsing
     * 
     */
    protected Sentence15(final FrameNetCorpus reader, final Element node)
	    throws FrameElementNotFoundException, FrameNotFoundException,
	    ParsingException {
	super(node.attributeValue("ID"), node.element("text").getText());
	this.corpusReader = reader;

	// Search for all tokens, add them to the list
	node.addNamespace("fn", "http://framenet.icsi.berkeley.edu");
	List<?> allTokens =
		node.selectNodes("fn:annotationSet/fn:layer/fn:label[@start and @end]");
	for (Object tokenObj : allTokens) {
	    Element token = (Element) tokenObj;
	    CharacterRange range =
		    new CharacterRange(new Integer(
			    token.attributeValue("start")), new Integer(
			    token.attributeValue("end")) + 1);
	    addToken(range);
	}

	List<?> asets = node.selectNodes("fn:annotationSet[@frameName]");

	// NodeList asets = node.getElementsByTagName("annotationSet");
	// if (asets != null) {
	for (Object aseto : asets) {
	    Element aset = (Element) aseto;
	    if (aset.attributeValue("frameName") != "") {

		try {
		    Frame frame =
			    reader.getFrameNet().getFrame(
				    aset.attributeValue("frameName"));
		    annotationSets.add(new AnnotationSet15(this, aset, reader
			    .getFrameNet().getLexicalUnit(frame,
				    aset.attributeValue("luName")), frame));
		} catch (ParsingException e) {
		    getCorpus().getLogger().severe(e.getMessage());
		    if (reader.isAbortOnError()) {
			throw new ParsingException(e.getMessage());
		    }
		} catch (FrameNotFoundException e) {
		    getCorpus().getLogger().warning(e.getMessage());
		    if (reader.isAbortOnError()) {
			throw new FrameNotFoundException(e.getFrameName());
		    }
		}
	    }
	}

    }

    /**
     * @return the subCorpus
     */
    public SubCorpus15 getSubCorpus() {
	return subCorpus;
    }

    /**
     * @param theSubCorpus
     *            the subCorpus to set
     */
    protected void setSubCorpus(final SubCorpus15 theSubCorpus) {
	this.subCorpus = theSubCorpus;
    }

    /**
     * @return the sentenceNumber
     */
    public Integer getSentenceNumber() {
	return sentenceNumber;
    }

    /**
     * @param sentenceNumber
     *            the sentenceNumber to set
     */
    public void setSentenceNumber(final Integer sentenceNumber) {
	this.sentenceNumber = sentenceNumber;
    }

    /**
     * @return A boolean value showing the soundness of the sentence and its
     *         annotations.
     */
    public boolean isSane() {
	return sane;
    }

    /**
     * 
     * @param token
     * @param posClass
     * @param tryOther
     * @return
     */
    public IPartOfSpeech getPartOfSpeech(final Element token,
	    final Class<? extends IPartOfSpeech> posClass,
	    final boolean tryOther) {
	try {
	    IPartOfSpeech posTag =
		    (IPartOfSpeech) posClass.getMethod("fromString",
			    String.class).invoke(null,
			    token.attributeValue("name").toUpperCase());
	    return posTag;
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (SecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    if (tryOther) {
		if (posClass == BNC.class) {
		    return this.getPartOfSpeech(token, PTB.class, false);
		} else if (posClass == PTB.class) {
		    return this.getPartOfSpeech(token, BNC.class, false);
		}
	    } else {
		System.err.println(e.getMessage());
		System.err.println(token.asXML());
		System.err.println(token.attributeValue("name"));
	    }
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }
}
