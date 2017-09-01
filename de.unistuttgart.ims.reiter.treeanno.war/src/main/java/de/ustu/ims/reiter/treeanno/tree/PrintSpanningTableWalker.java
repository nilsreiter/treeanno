package de.ustu.ims.reiter.treeanno.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PrintSpanningTableWalker<T> implements Walker<T, Map<T, Map<T, T>>> {

	public PrintSpanningTableWalker(Function<T, String> idFunction) {
		super();
		this.idFunction = idFunction;
	}

	Map<T, Map<T, T>> matrix;
	Function<T, String> idFunction;

	@Override
	public void init() {
		matrix = new HashMap<T, Map<T, T>>();
	}

	@Override
	public void beginNode(Node<T> node, Node<T> parent) {
		if (!node.getChildren().isEmpty()) {
			T x = (findFirstLeaf(node).getObject());
			T y = (findLastLeaf(node).getObject());
			store(x, y, node.getObject());
		}
	}

	protected Node<T> findFirstLeaf(Node<T> node) {
		if (node.getChildren().first().getChildren().isEmpty())
			return node.getChildren().first();
		else
			return findFirstLeaf(node.getChildren().first());
	}

	protected Node<T> findLastLeaf(Node<T> node) {
		if (node.getChildren().last().getChildren().isEmpty())
			return node.getChildren().last();
		else
			return findLastLeaf(node.getChildren().last());
	}

	@Override
	public void endNode(Node<T> node, Node<T> parent) {
		// intentionally empty
	}

	protected void store(T x, T y, T ts) {
		if (!matrix.containsKey(x)) {
			matrix.put(x, new HashMap<T, T>());
		}
		matrix.get(x).put(y, ts);
	}

	@Override
	public String toString() {
		return matrix.toString();

	}


	@Override
	public Map<T, Map<T, T>> getResult() {
		return matrix;
	}

}
