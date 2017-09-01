package de.ustu.ims.reiter.treeanno.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PrintSpanningTableWalker<T> implements Walker<T, Map<String, Map<String, Integer>>> {

	public PrintSpanningTableWalker(Function<T, String> idFunction) {
		super();
		this.idFunction = idFunction;
	}

	Map<String, Map<String, T>> matrix;
	Function<T, String> idFunction;

	@Override
	public void init() {
		matrix = new HashMap<String, Map<String, T>>();
	}

	@Override
	public void beginNode(Node<T> node, Node<T> parent) {
		if (!node.getChildren().isEmpty()) {
			String x = idFunction.apply(node.getChildren().first().getObject());
			String y = idFunction.apply(node.getChildren().last().getObject());
			store(x, y, node.getObject());
		}
	}

	@Override
	public void endNode(Node<T> node, Node<T> parent) {
		// intentionally empty
	}

	protected void store(String x, String y, T ts) {
		if (!matrix.containsKey(x)) {
			matrix.put(x, new HashMap<String, T>());
		}
		matrix.get(x).put(y, ts);
	}

	@Override
	public String toString() {
		return matrix.toString();

	}

	public Map<String, Map<String, T>> getMatrix() {
		return matrix;
	}

	@Override
	public Map<String, Map<String, Integer>> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
