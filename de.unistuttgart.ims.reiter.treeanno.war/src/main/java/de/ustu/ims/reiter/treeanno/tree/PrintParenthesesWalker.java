package de.ustu.ims.reiter.treeanno.tree;

import java.util.function.Function;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class PrintParenthesesWalker<T> implements Walker<T> {

	public static Function<TreeSegment, String> treeSegmentId = (TreeSegment ts) -> {
		return (ts == null ? "root" : String.valueOf(ts.getId()));
	};

	public PrintParenthesesWalker() {
	}

	public PrintParenthesesWalker(Function<T, String> idFunction) {
		super();
		this.idFunction = idFunction;
	}

	StringBuilder b;
	Function<T, String> idFunction = null;
	boolean insertSpace = false;

	@Override
	public void beginNode(Node<T> node, Node<T> parent) {
		b.append('(');
		if (idFunction != null) {
			if (insertSpace)
				b.append(' ');
			b.append(idFunction.apply(node.getObject()));
			if (insertSpace)
				b.append(' ');
		}
	}

	@Override
	public void endNode(Node<T> node, Node<T> parent) {
		b.append(')');
	}

	@Override
	public String toString() {
		return b.toString();
	}

	@Override
	public void init() {
		b = new StringBuilder();
	}

	public Function<T, String> getIdFunction() {
		return idFunction;
	}

	public void setIdFunction(Function<T, String> idFunction) {
		this.idFunction = idFunction;
	}
}
