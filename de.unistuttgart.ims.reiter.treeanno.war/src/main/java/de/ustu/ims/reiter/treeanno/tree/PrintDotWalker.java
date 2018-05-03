package de.ustu.ims.reiter.treeanno.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class PrintDotWalker implements Walker<TreeSegment, String> {

	StringBuilder b;
	@Deprecated
	String segmentStyle;
	@Deprecated
	String virtualSegmentStyle;
	Map<String, String> segmentStyles;
	Function<TreeSegment, String> labelFunction;

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
			if (ts.getNodeType() == null) {

				// this is for old documents who don't have a node type yet
				b.append("[");
				if (ts.getBegin() == ts.getEnd()) {
					b.append(segmentStyles.get("virtual"));
				} else {
					b.append(segmentStyles.get("text"));
				}
				if (labelFunction != null) {
					b.append(",label=\"").append(labelFunction.apply(ts)).append("\"");
				}
				b.append("]");

			} else {
				if (segmentStyles.containsKey(ts.getNodeType())) {
					b.append("[");
					b.append(segmentStyles.get(ts.getNodeType()));
					if (labelFunction != null) {
						b.append(",label=\"").append(labelFunction.apply(ts)).append("\"");
					}
					b.append("]");
				}
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

	@Override
	public String getResult() {
		return toString();
	}

	public Function<TreeSegment, String> getLabelFunction() {
		return labelFunction;
	}

	public void setLabelFunction(Function<TreeSegment, String> labelFunction) {
		this.labelFunction = labelFunction;
	}
}
