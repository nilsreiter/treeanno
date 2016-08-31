package de.ustu.ims.reiter.treeanno.tree;

import java.util.HashMap;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class PrintXmlWalker implements Walker<TreeSegment> {

	Document document;
	Element rootElement;
	Map<TreeSegment, Element> elementMap = new HashMap<TreeSegment, Element>();

	@Override
	public void init() {}

	@Override
	public void beginNode(Node<TreeSegment> node, Node<TreeSegment> parent) {
		Element xmlNode = new Element("node");
		if (node.getObject() != null) {
			TreeSegment ts = node.getObject();
			if (ts.getCoveredText() != null)
				xmlNode.addAttribute(new Attribute("surface", ts
						.getCoveredText()));
			if (ts.getCategory() != null)
				xmlNode.addAttribute(new Attribute("category", ts.getCategory()));

		}
		elementMap.put(node.getObject(), xmlNode);
		if (parent == null)
			rootElement = xmlNode;
		else
			elementMap.get(parent.getObject()).appendChild(xmlNode);

	}

	@Override
	public void endNode(Node<TreeSegment> node, Node<TreeSegment> parent) {}

	@Override
	public String toString() {
		Document document = new Document(rootElement);

		return document.toXML();
	}

}
