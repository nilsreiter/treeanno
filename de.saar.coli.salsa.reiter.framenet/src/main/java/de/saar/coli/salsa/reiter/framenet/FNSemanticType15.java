package de.saar.coli.salsa.reiter.framenet;

import java.util.List;
import java.util.logging.Level;

import org.dom4j.Element;

/**
 * This class is for reading FrameNet 1.5 data.
 * 
 * @author reiter
 * 
 */
public class FNSemanticType15 extends FNSemanticType {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8602096378674558332L;

    /**
     * 
     * @param reader
     *            The database reader
     * @param node
     *            The XML node
     */
    protected FNSemanticType15(final FNDatabaseReader reader, final Element node) {
	super(reader, node);

    }

    @Override
    protected Boolean supplyNode(final FNDatabaseReader reader,
	    final Element node) {
	if (!nodeSupplied) {
	    id = node.attributeValue("ID");
	    name = node.attributeValue("name");
	    abbreviation = node.attributeValue("abbrev");
	    definition =
		    node.element("definition").getText().replace('\n', ' ');
	    List<?> list = node.elements("superType");

	    for (Object item : list) {
		Element st = (Element) item;
		superTypes.add(frameNet.getSemanticType(
			st.attributeValue("superTypeName"), true));
		try {
		    frameNet.getSemanticType(st.attributeValue("superTypeName"))
			    .registerSubType(this);
		} catch (SemanticTypeNotFoundException e) {
		    reader.getFrameNet()
			    .getLogger()
			    .log(Level.WARNING,
				    "Semantic type "
					    + name
					    + " uses "
					    + st.attributeValue("superTypeName")
					    + " as supertype, which does not exist.");
		}
	    }
	    nodeSupplied = true;
	    return true;
	}
	return false;
    }
}
