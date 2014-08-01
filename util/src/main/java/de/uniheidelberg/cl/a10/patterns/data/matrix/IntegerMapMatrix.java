package de.uniheidelberg.cl.a10.patterns.data.matrix;

public class IntegerMapMatrix<R, C> extends MapMatrix<R, C, Integer> implements
IntegerMatrix<R, C> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IntegerMapMatrix() {
		super();
		this.defaultValue = 0;
		this.hasDefault = true;
	}

	public IntegerMapMatrix(final IntegerMatrix<R, C> matrix) {
		super();
		this.defaultValue = 0;
		this.hasDefault = true;
		for (R r : matrix.getRows()) {
			for (C c : matrix.getColumns()) {
				this.put(r, c, matrix.get(r, c));
			}
		}
	}

}
