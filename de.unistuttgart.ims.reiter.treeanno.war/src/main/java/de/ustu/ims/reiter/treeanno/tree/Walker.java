package de.ustu.ims.reiter.treeanno.tree;

/**
 * A Walker walks over a {@link Tree}. Walker methods are called at certain
 * points.
 * 
 * @author reiterns
 *
 * @param <T>
 *            The Payload type of the tree
 * @param <R>
 *            The return type of the walker
 */
public interface Walker<T, R> {
	void init();

	void beginNode(Node<T> node, Node<T> parent);

	void endNode(Node<T> node, Node<T> parent);

	R getResult();
}
