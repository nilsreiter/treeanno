package de.ustu.ims.reiter.treeanno.tree;

import java.util.HashMap;
import java.util.Map;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class PrintDotWalker implements Walker<TreeSegment> {

	StringBuilder b;
	@Deprecated
	String segmentStyle;
	@Deprecated
	String virtualSegmentStyle;
	Map<String, String> segmentStyles;

	public PrintDotWalker(Map<String, String> segmentStyles) {
		this.segmentStyles = segmentStyles;
	}

	public PrintDotWalker() {
		segmentStyles = new HashMap<String, String>();
		segmentStyles.put("text", "shape=oval");
		segmentStyles.put("virtual", "shape=oval");
	}


	public PrintDotWalker(String segmentStyle, String virtualSegmentStyle) {
		segmentStyles = new HashMap<String, String>();
		segmentStyles.put("text", segmentStyle);
		segmentStyles.put("virtual", virtualSegmentStyle);
	}

	@Override
	public void beginNode(Node<TreeSegment> node, Node<TreeSegment> parent) {
		if (node.getObject() != null) {

			TreeSegment ts = node.getObject();
			b.append(ts.getId());
			if (segmentStyles.containsKey(ts.getNodeType())) {
				b.append("[");
				b.append(segmentStyles.get(ts.getNodeType()));
				b.append("]");
			}

			b.append(";\n");
			if (parent.getObject() != null) {
				b.append(parent.getObject().getId()).append(" -> ").append(ts.getId()).append(";\n");
			}
		}
	}

	@Override
	public void endNode(Node<TreeSegment> node, Node<TreeSegment> parent) {
	}

	@Override
	public String toString() {
		b.append("}\n");
		return b.toString();
	}

	@Override
	public void init() {
		b = new StringBuilder();
		b.append("digraph G {\n");
	}
}
