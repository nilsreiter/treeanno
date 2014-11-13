package de.saar.coli.salsa.reiter.framenet;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

/**
 * This class is for reading FrameNet 1.5 data.
 * 
 * @author reiter
 * 
 */
public class FNFrameRelation15 extends FNFrameRelation {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * The constructor. Should not be invoked manually, but rather through a
     * {@link FrameNetRelation} object.
     * 
     * 
     * @param relation
     *            The relation
     * @param xmlnode
     *            The XML node
     * @throws FileNotFoundException
     *             If the FN data file can not be found
     * @throws FrameNotFoundException
     *             If the frame is not defined in the FN database
     * @throws FrameElementNotFoundException
     *             The frame element is not found or does not belong to the
     *             frame
     * @param reader
     *            The database reader
     */
    public FNFrameRelation15(final FNDatabaseReader15 reader,
	    final FrameNetRelation relation, final Element xmlnode)
	    throws FileNotFoundException, FrameNotFoundException,
	    FrameElementNotFoundException {
	frameNetRelation = relation;
	frameElementRelations = new LinkedList<FrameElementRelation>();
	id = xmlnode.attributeValue("ID");
	superFrame =
		frameNetRelation.frameNet.getFrame(xmlnode
			.attributeValue("superFrameName"));
	subFrame =
		frameNetRelation.frameNet.getFrame(xmlnode
			.attributeValue("subFrameName"));
	List<?> list = xmlnode.elements("FERelation"); // xpath.selectNodes(xmlnode);
	for (Object item : list) {
	    FrameElementRelation ferel =
		    new FrameElementRelation(this, (Element) item);
	    frameElementRelations.add(ferel);
	}
    }
}
