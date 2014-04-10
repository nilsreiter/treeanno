package de.saar.coli.salsa.reiter.framenet;

import org.dom4j.Element;

/**
 * This class provides the Semantic Type data from the original FrameNet XML
 * files.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public abstract class FNSemanticType extends SemanticType {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * This constructor loads the semantic type directly from the XML node in
     * the file "semtypes.xml". It takes the node and the FrameNet object as
     * arguments.
     * 
     * 
     * @param reader
     *            The database reader
     * @param node
     *            The XML node representing this semantic type
     */
    protected FNSemanticType(final FNDatabaseReader reader, final Element node) {
	super();
	frameNet = reader.getFrameNet();
	supplyNode(reader, node);

    }

    /**
     * Used to complete a semantic type by providing only the XML node. This
     * method overwrittes all data that have been preliminarily stored within
     * this semantic type.
     * 
     * @param node
     *            The XML node
     * @param reader
     *            The database reader
     * @return true if the node has been successfully loaded, false if it was
     *         already loaded
     */
    protected abstract Boolean supplyNode(FNDatabaseReader reader, Element node);

}
