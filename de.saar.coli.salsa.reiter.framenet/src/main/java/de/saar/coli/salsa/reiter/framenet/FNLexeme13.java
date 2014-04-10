package de.saar.coli.salsa.reiter.framenet;

import org.dom4j.Element;

import de.uniheidelberg.cl.reiter.pos.FN;

/**
 * This class is for reading FrameNet 1.3 data.
 * 
 * @author reiter
 * 
 */
public class FNLexeme13 extends FNLexeme {
    /**
	 * 
	 */
    private static final long serialVersionUID = 7101254458073253129L;

    /**
     * The constructor builds a new Lexeme object based on an XML node.
     * 
     * @param node
     *            The XML node
     */
    protected FNLexeme13(final Element node) {
	id = node.attributeValue("ID");
	partOfSpeech = FN.fromShortString(node.attributeValue("pos"));
	breakBefore =
		node.attributeValue("breakBefore").equalsIgnoreCase("true");
	headword = node.attributeValue("headword").equalsIgnoreCase("true");
	value = node.getStringValue();
    }
}
