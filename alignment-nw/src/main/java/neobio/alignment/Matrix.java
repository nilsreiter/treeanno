/*
 * Matrix.java
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
 * This interface defines a minimal set of operations that a matrix must implement. This
 * interface is used by the {@linkplain Smawk} class to provide a general services
 * regardless of how the matrix is actually stored.
 *
 * @author Sergio A. de Carvalho Jr.
 * @see Smawk
 */
public interface Matrix
{
	/**
	 * Returns the value at an specified row and column.
	 *
	 * @param row row number of element to be retrieved
	 * @param col column number of element to be retrieved
	 * @return value at row <CODE>row</CODE> column <CODE>col</CODE>
	 */
	public int valueAt (int row, int col);

	/**
	 * Returns the number of rows that this matrix has.
	 *
	 * @return number of rows
	 */
	public int numRows ();

	/**
	 * Returns the number of columns that this matrix has.
	 *
	 * @return number of columns
	 */
	public int numColumns ();
}
