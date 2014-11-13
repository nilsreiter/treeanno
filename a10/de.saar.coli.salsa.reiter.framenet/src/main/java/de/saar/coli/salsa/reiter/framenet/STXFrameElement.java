package de.saar.coli.salsa.reiter.framenet;

import org.dom4j.Element;

/**
 * This class represents a frame element read from a SalsaTigerXML document. 
 * @author Nils Reiter
 * @since 0.4
 */
public class STXFrameElement extends FrameElement {
	static final private long serialVersionUID = 1l;
	
	protected STXFrameElement(Element element) {
		name = element.attributeValue("name");
		String opt = element.attributeValue("optional");
		if (opt.equals("true")) {
			coreType = CoreType.Core;
		} else {
			coreType = CoreType.Peripheral;
		}
	}
}
