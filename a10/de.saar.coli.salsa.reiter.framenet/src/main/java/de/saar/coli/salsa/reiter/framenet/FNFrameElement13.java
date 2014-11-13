package de.saar.coli.salsa.reiter.framenet;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;

/**
 * This class is for reading FrameNet 1.3 data.
 * 
 * @author reiter
 * 
 */
public class FNFrameElement13 extends FNFrameElement {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new FrameElement object. The XML node should provide the
     * following attributes: name, abbrev, cDate, coreType and ID. There should
     * be a definition element present as a sub node of the given node.
     * 
     * @param frame
     *            The frame to which this frame element belongs.
     * @param node
     *            The node in the XML document
     * @param reader
     *            The FNDatabaseReader
     */
    protected FNFrameElement13(final FNDatabaseReader reader,
	    final Frame frame, final Element node) {
	super();
	name = node.attributeValue("name");
	abbreviation = node.attributeValue("abbrev");
	try {
	    creationDate =
		    reader.getDateFormat().parse(node.attributeValue("cDate"));
	} catch (ParseException e) {
	    creationDate = new Date(0L);
	    e.printStackTrace();
	}
	coreType = CoreType.fromString(node.attributeValue("coreType"));
	id = node.attributeValue("ID");
	definition = node.element("definition").getText().getBytes();

	this.frame = frame;

	// May cause problems when there is no semTypes element.
	// In 1.3, there are semTypes-elements, even when there is no
	// semType-element.
	List<?> stNodelist = node.element("semTypes").elements("semType");
	semanticTypes = new SemanticType[stNodelist.size()];
	for (int i = 0; i < stNodelist.size(); i++) {
	    Element stnode = (Element) stNodelist.get(i);
	    SemanticType st =
		    frame.framenet.getSemanticType(
			    stnode.attributeValue("name"), true);
	    semanticTypes[i] = st;
	    st.registerFrameElement(this);
	}
    }
}
