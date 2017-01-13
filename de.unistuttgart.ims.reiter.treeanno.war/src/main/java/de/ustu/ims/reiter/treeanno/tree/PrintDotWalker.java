package de.ustu.ims.reiter.treeanno.tree;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class PrintDotWalker implements Walker<TreeSegment> {

	StringBuilder b;
	String segmentStyle;
	String virtualSegmentStyle;

	public PrintDotWalker(String segmentStyle, String virtualSegmentStyle) {
		this.segmentStyle = segmentStyle;
		this.virtualSegmentStyle = virtualSegmentStyle;
	}

	@Override
	public void beginNode(Node<TreeSegment> node, Node<TreeSegment> parent) {
		if (node.getObject() != null) {

			TreeSegment ts = node.getObject();
			b.append(ts.getId());
			if (ts.getBegin() == ts.getEnd() && virtualSegmentStyle != null) {
				b.append("[").append(virtualSegmentStyle);
				// b.append(",label=\"").append(ts.getCategory().replace("\"",
				// "\\\""));
				b.append("]");
			} else if (segmentStyle != null) {
				b.append("[").append(segmentStyle).append("]");
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
