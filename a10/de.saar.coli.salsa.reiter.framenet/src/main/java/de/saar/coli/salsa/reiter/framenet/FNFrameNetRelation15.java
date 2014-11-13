package de.saar.coli.salsa.reiter.framenet;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.dom4j.Element;

/**
 * This class is for reading FrameNet 1.5 data.
 * 
 * @author reiter
 * 
 */
public class FNFrameNetRelation15 extends FNFrameNetRelation {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a FrameNetRelation object.
     * 
     * @param reader
     *            The database reader
     * @param node
     *            The XML node representing the relation in the XML file.
     */

    protected FNFrameNetRelation15(final FNDatabaseReader15 reader,
	    final Element node) {
	frameNet = reader.getFrameNet();
	name = node.attributeValue("name");
	id = node.attributeValue("ID");
	superName = node.attributeValue("superFrameName");
	subName = node.attributeValue("subFrameName");
	frameRelations = new HashSet<FrameRelation>();

	frameNet.getLogger().log(Level.INFO,
		"FrameNetRelation.FrameNetRelation(): Node loaded. " + name);

	List<?> list = node.elements("frameRelation"); // xpath.selectNodes(node);

	for (Object item : list) {
	    try {
		FrameRelation frel =
			new FNFrameRelation15(reader, this, (Element) (item));
		frameRelations.add(frel);
	    } catch (FrameNotFoundException e) {
		frameNet.getLogger()
			.log(Level.WARNING,
				e.getFrameName()
					+ " not found in the FrameNet database, but used in frame relation "
					+ id + ". Files not consistent");
	    } catch (FrameElementNotFoundException e) {
		frameNet.getLogger().log(
			Level.WARNING,
			e.getFrameElement() + " is not in frame "
				+ e.getFrame().getName()
				+ ", but specified in frame relation " + id
				+ ". Files not consistent.");
	    } catch (Exception e) {
		frameNet.getLogger().log(Level.SEVERE, e.toString());
	    }
	}

    }
}
