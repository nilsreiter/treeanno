package de.saar.coli.salsa.reiter.framenet;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * This class represents a frame read from a SalsaTigerXML file.
 * 
 * @author Nils Reiter
 * @since 0.4
 */
public class STXFrame extends Frame {

    static final private long serialVersionUID = 1l;

    protected STXFrame(final FrameNet fn, final Element element,
	    final Map<String, String> namespaces) {
	name = element.attributeValue("name");
	creationDate = new Date(0L);
	definition = "".getBytes();
	id = "";
	List<?> feNodelist = element.elements("element"); // xpath.selectNodes(element);
	for (int i = 0; i < feNodelist.size(); i++) {
	    Element feX = (Element) feNodelist.get(i);
	    FrameElement fe = new STXFrameElement(feX);
	    frameElements.put(fe.getName(), fe);
	}

    }

}
