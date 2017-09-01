package de.ustu.ims.reiter.treeanno.tree;

public interface Walker<T, R> {
	void init();

	void beginNode(Node<T> node, Node<T> parent);

	void endNode(Node<T> node, Node<T> parent);

	R getResult();
}
