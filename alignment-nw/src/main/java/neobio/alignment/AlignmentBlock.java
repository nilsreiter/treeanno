/*
 * AlignmentBlock.java
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
 * This class is used by the {@linkplain CrochemoreLandauZivUkelson} algorithm to store
 * the information of an alignment block. All fields are public (but final) in order to
 * simplify the access to the data.
 *
 * <P>For more information on how this class is used, please refer to the specification
 * of the <CODE>CrochemoreLandauZivUkelson</CODE> class and it subclasses.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see CrochemoreLandauZivUkelson
 */
public class AlignmentBlock
{
	/**
	 * A pointer to the factor of the first sequence being aligned.
	 */
	public final Factor factor1;

	/**
	 * A pointer to the factor of the second sequence being aligned.
	 */
	public final Factor factor2;

	/**
	 * The DIST column of this block.
	 */
	public final int[] dist_column;

	/**
	 * An array of pointers to prefix blocks of this block.
	 */
	public final AlignmentBlock[] ancestor;

	/**
	 * This block's output border.
	 */
	public final int[] output_border;

	/**
	 * An array of indexes to the source of the highest scoring path for each entry in
	 * the output border.
	 */
	public final int[] source_path;

	/**
	 * An array of directions that must be followed to reach the source of the highest
	 * scoring path for each entry in the output border.
	 *
	 * @see CrochemoreLandauZivUkelson#STOP_DIRECTION
	 * @see CrochemoreLandauZivUkelson#LEFT_DIRECTION
	 * @see CrochemoreLandauZivUkelson#DIAGONAL_DIRECTION
	 * @see CrochemoreLandauZivUkelson#TOP_DIRECTION
	 */
	public final byte[]	direction;

	/**
	 * Creates a new root block. A root block does not have <CODE>source_path</CODE> and
	 * <CODE>ancestor</CODE> arrays. Moreover, its <CODE>dist_column</CODE> and
	 * <CODE>output_border</CODE> arrays are set to zero, and the <CODE>direction</CODE>
	 * array is set to contain an <CODE>STOP_DIRECTION</CODE>.
	 *
	 * @param factor1 factor of the first sequence being aligned
	 * @param factor2 factor of the second sequence being aligned
	 */
	public AlignmentBlock (Factor factor1, Factor factor2)
	{
		this.factor1 = factor1;
		this.factor2 = factor2;

		dist_column = output_border = new int[] {0};
		direction = new byte [] {0}; // STOP_DIRECTION
		source_path = null;
		ancestor = null;
	}

	/**
	 * Creates a new alignment block, with all arrays created with the specified size.
	 *
	 * @param factor1 factor of the first sequence being aligned
	 * @param factor2 factor of the second sequence being aligned
	 * @param size size of the arrays to be created
	 */
	public AlignmentBlock (Factor factor1, Factor factor2, int size)
	{
		this.factor1 = factor1;
		this.factor2 = factor2;

		dist_column = new int[size];
		output_border = new int[size];
		direction = new byte[size];
		source_path = new int[size];
		ancestor = new AlignmentBlock[size];
	}
}
