/*
 * Smawk.java
 *
 * Copyright 2003 Sergio Anibal de Carvalho Junior
 *
 * This file is part of NeoBio.
 *
 * NeoBio is free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * NeoBio is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with NeoBio;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * Proper attribution of the author as the source of the software would be appreciated.
 *
 * Sergio Anibal de Carvalho Junior		mailto:sergioanibaljr@users.sourceforge.net
 * Department of Computer Science		http://www.dcs.kcl.ac.uk
 * King's College London, UK			http://www.kcl.ac.uk
 *
 * Please visit http://neobio.sourceforge.net
 *
 * This project was supervised by Professor Maxime Crochemore.
 *
 */

package neobio.alignment;


/**
 * This class implement the SMAWK algorithm to compute column maxima on a totally monotone
 * matrix as described.
 *
 * <P>This implementation derives from the paper of A.Aggarwal, M.Klawe, S.Moran, P.Shor,
 * and R.Wilber, <I>Geometric Applications of a Matrix Searching Algorithm</I>,
 * Algorithmica, 2, 195-208 (1987).</P>
 *
 * <P>The matrix must be an object that implements the {@linkplain Matrix} interface. It
 * is also expected to be totally monotone, and the number of rows should be greater than
 * or equals to the number of columns. If these conditions are not met, the the result is
 * unpredictable and can lead to an ArrayIndexOutOfBoundsException.</P>
 *
 * <P>{@link #computeColumnMaxima computeColumnMaxima} is the main public method of this
 * class. It computes the column maxima of a given matrix, i.e. the rows that contain the
 * maximum value of each column in O(n) (linear) time, where n is the number of rows. This
 * method does not return the maximum values itself, but just the indexes of their
 * rows.</P>
 *
 * <P>Note that it is necessary to create an instance of this class to execute the
 * <CODE>computeColumnMaxima</CODE> because it stores temporary data is that instance. To
 * prevent problems with concurrent access, the <CODE>computeColumnMaxima</CODE> method is
 * declared <CODE>synchronized</CODE>.
 *
 * <CODE><BLOCKQUOTE><PRE>
 * // create an instance of Smawk
 * Smawk smawk = new Smawk();
 *
 * // create an array to store the result
 * int col_maxima = new int [some_matrix.numColumns()];
 *
 * // now compute column maxima
 * smawk.computeColumnMaxima (some_matrix, col_maxima)
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>Note that the array of column maxima indexes (the computation result) must be
 * created beforehand and its size must be equal to the number of columns of the
 * matrix.</P>
 *
 * <P>This implementation creates arrays of row and column indexes from the original array
 * and simulates all operations (reducing, deletion of odd columns, etc.) by manipulating
 * these arrays. The benefit is two-fold. First the matrix is not required to implement
 * any of these this operations but only a simple method to retrieve a value at a given
 * position. Moreover, it tends to be faster since it uses a manipulation of these small
 * vectors and no row or column is actually deleted. The downside is, of course, the use
 * of extra memory (in practice, however, this is negligible).</P>
 *
 * <P>Note that this class does not contain a <CODE>computeRowMaxima</CODE> method,
 * however, the <CODE>computeColumnMaxima</CODE> can easily be used to compute row maxima
 * by using a transposed matrix interface, i.e. one that inverts the indexes of the
 * <CODE>valueAt</CODE> method (returning [col,row] when [row,col] is requested) and swaps
 * the number of rows by the number of columns, and vice-versa.</P>
 *
 * <P>Another simpler method, {@link #naiveComputeColumnMaxima naiveComputeColumnMaxima},
 * does the same job without using the SMAWK algorithm. It takes advantage of the monotone
 * property of the matrix only (SMAWK explores the stronger constraint of total
 * monotonicity), and therefore has a worst case time complexity of O(n * m), where n is
 * the number of rows and m is the number of columns. However, this method tends to be
 * faster for small matrices because it avoids recursions and row and column
 * manipulations. There is also a
 * {@linkplain #naiveComputeRowMaxima naiveComputeRowMaxima} method to compute row maxima
 * with the naive approach.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see Matrix
 */
public class Smawk
{
	/**
	 * A pointer to the matrix that is being manipulated.
	 */
	protected Matrix matrix;

	/**
	 * The matrix's current number of rows. This reflects any deletion of rows already
	 * performed.
	 */
	protected int numrows;

	/**
	 * An array of row indexes reflecting the current state of the matrix. When rows are
	 * deleted, the corresponding indexes are simply moved to the end of the vector.
	 */
	protected int row[];

	/**
	 * This array is used to store for each row of the original matrix, its index in the
	 * current state of the matrix, i.e. its index in the <CODE>row</CODE> array.
	 */
	protected int row_position[];

	/**
	 * The matrix's current number of columns. This reflects any deletion of columns
	 * already performed.
	 */
	protected int numcols;

	/**
	 * An array of column indexes reflecting the current state of the matrix. When columns
	 * are deleted, the corresponding indexes are simply moved to the end of the vector.
	 */
	protected int col[];

	/**
	 * Computes the column maxima of a given matrix. It first sets up arrays of row and
	 * column indexes to simulate a copy of the matrix (where all operations will be
	 * performed). It then calls the recursive protected <CODE>computeColumnMaxima</CODE>
	 * method.
	 *
	 * <P>The matrix is required to be an object that implements the <CODE>Matrix</CODE>
	 * interface. It is also expected to be totally monotone, and the number of rows
	 * should be greater than or equals to the number of columns. If it is not, the the
	 * result is unpredictable and can lead to an ArrayIndexOutOfBoundsException.</P>
	 *
	 * <P>This method does not return the maximum values itself, but just the indexes of
	 * their rows. Note that the array of column maxima (the computation result) must be
	 * created beforehand and its size must be equal to the number of columns of the
	 * matrix.</P>
	 *
	 * <P>To prevent problems with concurrent access, this method is declared
	 * <CODE>synchronized</CODE>.</P>
	 *
	 * @param matrix the matrix that will have its column maxima computed
	 * @param col_maxima the array of column maxima (indexes of the rows containing
	 * maximum values of each column); this is the computation result
	 * @see #computeColumnMaxima(int[])
	 */
	public synchronized void computeColumnMaxima (Matrix matrix, int[] col_maxima)
	{
		int i;

		this.matrix = matrix;

		// create an array of column indexes
		numcols = matrix.numColumns();
		col = new int [numcols];
		for (i = 0; i < numcols; i++)
			col[i] = i;

		// create an array of row indexes
		numrows = matrix.numRows();
		row = new int [numrows];
		for (i = 0; i < numrows; i++)
			row[i] = i;

		// instantiate an helper array for
		// backward reference of rows
		row_position = new int [numrows];

		computeColumnMaxima (col_maxima);
	}

	/**
	 * This method implements the SMAWK algorithm to compute the column maxima of a given
	 * matrix. It uses the arrays of row and column indexes to performs all operations on
	 * this 'fake' copy of the original matrix.
	 *
	 * <P>The first step is to reduce the matrix to a quadratic size (if necessary). It
	 * then delete all odd columns and recursively computes column maxima for this matrix.
	 * Finally, using the information computed for the odd columns, it searches for
	 * column maxima of the even columns. The column maxima are progressively stored in
	 * the <CODE>col_maxima</CODE> array (each recursive call will compute a set of
	 * column maxima).</P>
	 *
	 * @param col_maxima the array of column maxima (the computation result)
	 */
	protected void computeColumnMaxima (int[] col_maxima)
	{
		int original_numrows, original_numcols, c, r, max, end;

		original_numrows = numrows;

		if (numrows > numcols)
		{
			// reduce to a quadratic size by deleting
			// rows that contain no maximum of any column
			reduce ();
		}

		// base case: matrix has only one row (and one column)
		if (numrows == 1)
		{
			// so the first column's maximum is the only row left
			col_maxima[col[0]] = row[0];

			if (original_numrows > numrows)
			{
				// restore rows of original matrix (deleted on reduction)
				restoreRows (original_numrows);
			}

			return;
		}

		// save the number of columns before deleting the odd ones
		original_numcols = numcols;

		deleteOddColumns ();

		// recursively computes max rows for the remaining even columns
		computeColumnMaxima (col_maxima);

		restoreOddColumns (original_numcols);

		// set up pointers to the original index for all rows
		for (r = 0; r < numrows; r++)
			row_position[row[r]] = r;

		// compute max rows for odd columns based on the result of even columns
		for (c = 1; c < numcols; c = c + 2)
		{
			if (c < numcols - 1)
				// if not last column, search ends
				// at next columns' max row
				end = row_position[col_maxima[col[c + 1]]];
			else
				// if last columnm, search ends
				// at last row
				end = numrows - 1;

			// search starts at previous columns' max row
			max = row_position[col_maxima[col[c - 1]]];

			// check all values until the end
			for (r = max + 1; r <= end; r++)
			{
				if (valueAt(r, c) > valueAt(max, c))
					max = r;
			}

			col_maxima[col[c]] = row[max];
		}

		if (original_numrows > numrows)
		{
			// restore rows of original matrix (deleted on reduction)
			restoreRows (original_numrows);
		}
	}

	/**
	 * This is a helper method to simplify the call to the <CODE>valueAt</CODE> method
	 * of the matrix. It returns the value at row <CODE>r</CODE>, column <CODE>c</CODE>.
	 *
	 * @param r the row number of the value being retrieved
	 * @param c the column number of the value being retrieved
	 * @return the value at row <CODE>r</CODE>, column <CODE>c</CODE>
	 * @see Matrix#valueAt
	 */
	protected final int valueAt (int r, int c)
	{
		return matrix.valueAt (row[r], col[c]);
	}

	/**
	 * This method simulates a deletion of odd rows by manipulating the <CODE>col</CODE>
	 * array of indexes. In fact, nothing is deleted, but the indexes are moved to the end
	 * of the array in a way that they can be easily restored by the
	 * <CODE>restoreOddColumns</CODE> method using a reverse approach.
	 *
	 * @see #restoreOddColumns
	 */
	protected void deleteOddColumns ()
	{
		int tmp;

		for (int c = 2; c < numcols; c = c + 2)
		{
			// swap column c with c/2
			tmp = col[c / 2];
			col[c / 2] = col[c];
			col[c] = tmp;
		}

		numcols = ((numcols - 1) / 2 + 1);
	}

	/**
	 * Restores the <CODE>col</CODE> array of indexes to the state it was before the
	 * <CODE>deleteOddColumns</CODE> method was called. It only needs to know how many
	 * columns there was originally. The indexes that were moved to the end of the array
	 * are restored to their original position.
	 *
	 * @param original_numcols the number of columns before the odd ones were deleted
	 * @see #deleteOddColumns
	 */
	protected void restoreOddColumns (int original_numcols)
	{
		int tmp;

		for (int c = 2 * ((original_numcols - 1) / 2); c > 0; c = c - 2)
		{
			// swap back column c with c/2
			tmp = col[c / 2];
			col[c / 2] = col[c];
			col[c] = tmp;
		}

		numcols = original_numcols;
	}

	/**
	 * This method is the key component of the SMAWK algorithm. It reduces an n x m matrix
	 * (n rows and m columns), where n >= m, to an n x n matrix by deleting m - n rows
	 * that are guaranteed to have no maximum value for any column. The result is an
	 * squared submatrix matrix that contains, for each column c, the row that has the
	 * maximum value of c in the original matrix. The rows are deleted with the
	 * <CODE>deleteRow</CODE>method.
	 *
	 * <P>It uses the total monotonicity property of the matrix to identify which rows can
	 * safely be deleted.
	 *
	 * @see #deleteRow
	 */
	protected void reduce ()
	{
		int k = 0, reduced_numrows = numrows;

		// until there is more rows than columns
		while (reduced_numrows > numcols)
		{
			if (valueAt(k, k) < valueAt(k + 1, k))
			{
				// delete row k
				deleteRow (reduced_numrows, k);
				reduced_numrows --;
				k --;
			}
			else
			{
				if (k < numcols - 1)
				{
					k++;
				}
				else
				{
					// delete row k+1
					deleteRow (reduced_numrows, k+1);
					reduced_numrows --;
				}
			}
		}

		numrows = reduced_numrows;
	}

	/**
	 * This method simulates a deletion of a row in the matrix during the
	 * <CODE>reduce</CODE> operation. It just moves the index to the end of the array in a
	 * way that it can be restored afterwards by the <CODE>restoreRows</CODE> method
	 * (nothing is actually deleted). Deleted indexes are kept in ascending order.
	 *
	 * @param reduced_rows the current number of rows in the reducing matrix
	 * @param k the index of the row to be deleted
	 * @see #restoreRows
	 */
	protected void deleteRow (int reduced_rows, int k)
	{
		int r, saved_row = row[k];

		for (r = k + 1; r < reduced_rows; r++)
			row[r - 1] = row[r];

		for (r = reduced_rows - 1; r < (numrows - 1) && row[r+1] < saved_row; r++)
			row[r] = row[r+1];

		row[r] = saved_row;
	}

	/**
	 * Restores the <CODE>row</CODE> array of indexes to the state it was before the
	 * <CODE>reduce</CODE> method was called. It only needs to know how many rows there
	 * was originally. The indexes that were moved to the end of the array are restored to
	 * their original position.
	 *
	 * @param original_numrows the number of rows before the reduction was performed
	 * @see #deleteRow
	 * @see #reduce
	 */
	protected void restoreRows (int original_numrows)
	{
		int r, r2, s, d = numrows;

		for (r = 0; r < d; r++)
		{
			if (row[r] > row[d])
			{
				s = row[d];
				for (r2 = d; r2 > r; r2--)
					row[r2] = row[r2-1];
				row[r] = s;
				d++;
				if (d > original_numrows - 1) break;
			}
		}

		numrows = original_numrows;
	}

	/**
	 * This is a simpler method for calculating column maxima. It does the same job as
	 * <CODE>computeColumnMaxima</CODE>, but without complexity of the SMAWK algorithm.
	 *
	 * <P>The matrix is required to be an object that implements the <CODE>Matrix</CODE>
	 * interface. It is also expected to be monotone. If it is not, the result is
	 * unpredictable but, unlike <CODE>computeColumnMaxima</CODE>, it cannot lead to an
	 * ArrayIndexOutOfBoundsException.</P>
	 *
	 * <P>This method does not return the maximum values itself, but just the indexes of
	 * their rows. Note that the array of column maxima (the computation result) must be
	 * created beforehand and its size must be equal to the number of columns of the
	 * matrix.</P>
	 *
	 * <P>It takes advantage of the monotone property of the matrix only (SMAWK explores
	 * the stronger constraint of total monotonicity), and therefore has a worst case time
	 * complexity of O(n * m), where n is the number of rows and m is the number of
	 * columns. However, this method tends to be faster for small matrices because it
	 * avoids recursions and row and column manipulations.</P>
	 *
	 * @param matrix the matrix that will have its column maxima computed
	 * @param col_maxima the array of column maxima (indexes of the rows containing
	 * maximum values of each column); this is the computation result
	 * @see #naiveComputeRowMaxima
	 */
	public static void naiveComputeColumnMaxima (Matrix matrix, int col_maxima[])
	{
		int max_row = 0;
		//int last_max = 0;

		for (int c = 0; c < matrix.numColumns(); c ++)
		{
			for (int r = max_row; r < matrix.numRows(); r++)
				if (matrix.valueAt(r,c) > matrix.valueAt(max_row,c))
					max_row = r;

			col_maxima[c] = max_row;

			// uncomment the following code to raise an exception when
			// the matrix is not monotone
			/*
			if (max_row < last_max)
				throw new IllegalArgumentException ("Non totally monotone matrix.");
			last_max = max_row;
			max_row = 0;
			*/
		}
	}

	/**
	 * This is a simpler method for calculating row maxima. It does not use the SMAWK
	 * algorithm.
	 *
	 * <P>The matrix is required to be an object that implements the <CODE>Matrix</CODE>
	 * interface. It is also expected to be monotone. If it is not, the result is
	 * unpredictable but, unlike <CODE>computeColumnMaxima</CODE>, it cannot lead to an
	 * ArrayIndexOutOfBoundsException.</P>
	 *
	 * <P>This method does not return the maximum values itself, but just the indexes of
	 * their columns. Note that the array of row maxima (the computation result) must be
	 * created beforehand and its size must be equal to the number of columns of the
	 * matrix.</P>
	 *
	 * <P>It takes advantage of the monotone property of the matrix only (SMAWK explores
	 * the stronger constraint of total monotonicity), and therefore has a worst case time
	 * complexity of O(n * m), where n is the number of rows and m is the number of
	 * columns. However, this method tends to be faster for small matrices because it
	 * avoids recursions and row and column manipulations.</P>
	 *
	 * @param matrix the matrix that will have its row maxima computed
	 * @param row_maxima the array of row maxima (indexes of the columns containing
	 * maximum values of each row); this is the computation result
	 * @see #naiveComputeColumnMaxima
	 */
	public static void naiveComputeRowMaxima (Matrix matrix, int row_maxima[])
	{
		int max_col = 0;
		//int last_max = 0;

		for (int r = 0; r < matrix.numRows(); r++)
		{
			for (int c = max_col; c < matrix.numColumns(); c ++)
				if (matrix.valueAt(r,c) > matrix.valueAt(r,max_col))
					max_col = c;

			row_maxima[r] = max_col;

			// uncomment the following code to raise an exception when
			// the matrix is not monotone
			/*
			if (max_col < last_max)
				throw new IllegalArgumentException ("Non-monotone matrix.");
			last_max = max_col;
			max_col = 0;
			*/
		}
	}

	/**
	 * Prints the current state of the matrix (reflecting deleted rows and columns) in the
	 * standard output. It can be used internally for debugging.
	 */
	protected void printMatrix ()
	{
		int r, c;

		System.out.print("row\\col\t| ");
		for (c = 0; c < numcols; c++)
			System.out.print(col[c] + "\t");

		for (r = 0; r < numrows; r++)
		{
			System.out.print(row[r] + "\n\t| ");
			for (c = 0; c < numcols; c++)
				System.out.print(matrix.valueAt(r,c) + "\t");
		}

	}

	/**
	 * Prints the contents of an object implementing the matrix interface in the standard
	 * output. It can be used for debugging.
	 *
	 * @param matrix a matrix
	 */
	public static void printMatrix (Matrix matrix)
	{
		for (int r = 0; r < matrix.numRows(); r++)
		{
			for (int c = 0; c < matrix.numColumns(); c++)
				System.out.print(matrix.valueAt(r,c) + "\t");
			System.out.print("\n");
		}
	}
}
