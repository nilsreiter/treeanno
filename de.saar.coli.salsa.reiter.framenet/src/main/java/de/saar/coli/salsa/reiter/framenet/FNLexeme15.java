package de.saar.coli.salsa.reiter.framenet;

import org.dom4j.Element;

import de.uniheidelberg.cl.reiter.pos.FN;

/**
 * This class is for reading FrameNet 1.5 data.
 * 
 * @author reiter
 * 
 */
public class FNLexeme15 extends FNLexeme {
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
    protected FNLexeme15(final Element node) {
	id = node.attributeValue("ID");
	partOfSpeech = FN.fromShortString(node.attributeValue("POS"));
	breakBefore =
		node.attributeValue("breakBefore").equalsIgnoreCase("true");
	headword = node.attributeValue("headword").equalsIgnoreCase("true");
	value = node.attributeValue("name"); // node.getStringValue();
    }
}
