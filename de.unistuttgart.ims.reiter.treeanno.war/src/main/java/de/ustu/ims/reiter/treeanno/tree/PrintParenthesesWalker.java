package de.ustu.ims.reiter.treeanno.tree;

public class PrintParenthesesWalker<T> implements Walker<T> {

	StringBuilder b = new StringBuilder();

	@Override
	public void beginNode(Node<T> node) {
		b.append('(');
		// b.append(node.getObject().toString());
	}

	@Override
	public void endNode(Node<T> node) {
		b.append(')');
	}

	@Override
	public String toString() {
		return b.toString();
	}
}
