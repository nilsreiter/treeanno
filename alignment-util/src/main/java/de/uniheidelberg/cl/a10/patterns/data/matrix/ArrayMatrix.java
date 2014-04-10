package de.uniheidelberg.cl.a10.patterns.data.matrix;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.Set;

public class ArrayMatrix implements DoubleMatrix<Integer, Integer> {
	double[][] data = null;

	public ArrayMatrix(final int r, final int c) {
		this.initData(r, c);
	}

	/**
	 * Initializes the arrays by setting all values to zero.
	 * 
	 * @param r
	 *            The number of rows
	 * @param c
	 *            The number of columns
	 */
	protected void initData(final int r, final int c) {
		data = new double[r][];
		for (int i = 0; i < r; i++) {
			data[i] = new double[c];
			Arrays.fill(data[i], 0.0);
		}
	}

	/**
	 * This method adds a row to the matrix. All values in the new row are set
	 * to zero. The row is added at the bottom.
	 */
	public void addRow() {
		double[][] oldData = data;
		data = new double[data.length + 1][];
		int i = 0;
		for (i = 0; i < oldData.length; i++) {
			data[i] = oldData[i];
		}
		if (oldData.length > 0) {
			data[i] = new double[oldData[0].length];
			Arrays.fill(data[i], 0.0);
		}
	}

	/**
	 * This method adds a column to the matrix. All values are set to zero. The
	 * column is added at the right end of the matrix.
	 */
	public void addColumn() {
		for (int i = 0; i < data.length; i++) {
			if (data[i] == null) {
				data[i] = new double[1];
			} else {
				data[i] = Arrays.copyOf(data[i], data[i].length + 1);
			}
		}
	}

	@Override
	public int getRowNumber() {
		return data.length;
	}

	@Override
	public int getColumnNumber() {
		return data[0].length;
	}

	@Override
	public void put(final Integer r, final Integer c, final Double v) {
		data[r][c] = v;
	}

	@Override
	public Double get(final Integer r, final Integer c) {
		return data[r][c];
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		Formatter formatter = new Formatter(b, Locale.US);
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < data[r].length; c++) {
				if (data[r][c] == 0.0) {
					b.append("     0.0");
				} else {
					formatter.format("%1$ 7.5f", data[r][c]);
				}
			}
			b.append('\n');
		}
		formatter.close();
		return b.toString();
	}

	public Double getRowSum(final Integer row) {
		double d = 0.0;
		for (int col = 0; col < data[row].length; col++) {
			d += this.get(row, col);
		}
		return d;
	}

	public Double getColumnSum(final Integer col) {
		double d = 0.0;
		for (int row = 0; row < data.length; row++) {
			d += this.get(row, col);
		}
		return d;
	}

	@Override
	public Set<Integer> getRows() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Integer> getColumns() {
		throw new UnsupportedOperationException();
	}
}
