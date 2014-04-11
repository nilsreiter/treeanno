/*
 * OutMatrix.java
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
 * Implements an interface to the OUT matrix of a block. This class is used by the
 * {@linkplain CrochemoreLandauZivUkelson} and subclasses to enconde the OUT matrix
 * from the input border and DIST matrix of an {@linkplain AlignmentBlock}.
 *
 * <P>The OUT matrix defined as <CODE>OUT[i,j] = I[i] + DIST[i,j]</CODE> where I is the
 * input border array and DIST is the DIST matrix.</P>
 *
 * <P>The output border of a block is computed from the OUT matrix by taking the maximum
 * value of each column. Note that this class <B>does not compute the OUT matrix</B>, it
 * just stores the necessary information to retrieve a value at any position of the
 * matrix.</P>
 *
 * <P>It implements the Matrix interface so that the SMAWK algorithm can be used to
 * compute its column maxima.</P>
 *
 * <P>For more information on how this class is used, please refer to the specification
 * of the <CODE>CrochemoreLandauZivUkelson</CODE> and its subclasses.
 *
 * @author Sergio A. de Carvalho Jr.
 * @see CrochemoreLandauZivUkelson
 * @see CrochemoreLandauZivUkelsonGlobalAlignment
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @see AlignmentBlock
 * @see Smawk
 */
public class OutMatrix implements Matrix
{
	/**
	 * The length of the longest sequence (number of characters) being aligned. It needs
	 * to be set only once per alignment.
	 */
	protected int max_length;

	/**
	 * The maximum absolute score that the current scoring scheme can return. It needs
	 * to be set only once per alignment.
	 */
	protected int max_score;

	/**
	 * The DIST matrix of a block.
	 */
	protected int[][] dist;

	/**
	 * The input border of a block.
	 */
	protected int[] input_border;

	/**
	 * The dimension of the OUT matrix.
	 */
	protected int dim;

	/**
	 * The number of columns of the block.
	 */
	protected int lc;

	/**
	 * Initialised this OUT matrix interface. This method needs to be executed only once
	 * per alignment.
	 *
	 * @param max_length the length of the longest sequence (number of characters) being
	 * aligned
	 * @param max_score the maximum absolute score that the current scoring scheme can
	 * return
	 */
	public void init (int max_length, int max_score)
	{
		this.max_length = max_length;
		this.max_score = max_score;
	}

	/**
	 * Sets this interface's data to represent an OUT matrix for a block. This method
	 * is typically executed once for each block being aligned.
	 *
	 * @param dist the DIST matrix
	 * @param input_border the input border
	 * @param dim the dimension of the OUT matrix
	 * @param lc the number of columns of the block
	 */
	public void setData (int[][] dist, int[] input_border, int dim, int lc)
	{
		this.dist = dist;
		this.input_border = input_border;
		this.dim = dim;
		this.lc = lc;
	}

	/**
	 * Returns the value at a given position of the matrix. In general it returns the
	 * value of <CODE>DIST[col][row] + input_border[row]</CODE>. However, special cases
	 * occur for its upper right and lower left triangular parts.
	 *
	 * @param row row index
	 * @param col column index
	 * @return the value at row <CODE>row</CODE>, column <CODE>col</CODE> of this OUT
	 * matrix
	 */
	public int valueAt (int row, int col)
	{
		// The DIST matrix is indexed by [column][row]

		if (col < lc)
		{
			if (row < dim - (lc - col))
				return dist[col][row] + input_border[row];
			else
				// lower left triangle entries
				return - (max_length + row + 1) * max_score;
		}
		else if (col == lc)
		{
			return dist[col][row] + input_border[row];
		}
		else
		{
			if (row < (col - lc))
				// upper right triangle entries
				return Integer.MIN_VALUE + row;
			else
				return dist[col][row - (col - lc)] + input_border[row];
		}
	}

	/**
	 * Returns the number of rows of this OUT matrix.
	 *
	 * @return the number of rows of this OUT matrix
	 */
	public int numRows ()
	{
		return dim;
	}

	/**
	 * Returns the number of columns of this OUT matrix.
	 *
	 * @return the number of columns of this OUT matrix
	 */
	public int numColumns ()
	{
		return dim;
	}
}
