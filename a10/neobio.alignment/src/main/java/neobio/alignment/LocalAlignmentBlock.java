/*
 * LocalAlignmentBlock.java
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
 * This class is used by the {@linkplain CrochemoreLandauZivUkelsonLocalAlignment}
 * algorithm to store the information of an alignment block. All fields are public (but
 * final) in order to simplify the access to the data.
 *
 * <P>For more information on how this class is used, please refer to the specification
 * of the <CODE>CrochemoreLandauZivUkelsonLocalAlignment</CODE> class.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 */
public class LocalAlignmentBlock extends AlignmentBlock
{
	/**
	 * The value of the highest scoring path which starts at the input border of this
	 * block and ends inside it, called E-path.
	 */
	public int[] E_path_score;

	/**
	 * An array of pointers to blocks that are source of E-paths.
	 */
	public LocalAlignmentBlock[] E_path_ancestor;

	/**
	 * Indexes of of the entry in the ancestor block that is the source of the E-path.
	 */
	public int[] E_path_ancestor_index;

	/**
	 * The value of the highest scoring path which starts inside the block and ends at its
	 * output border.
	 */
	public int[] S_path_score;

	/**
	 * The type of the highest scoring path ending at a given position of the output
	 * border of a block.
	 */
	public byte[] path_type;

	/**
	 * The direction to the source of the S-path of the new vertex of this block.
	 */
	public byte S_direction;

	/**
	 * The value of the highest scoring path contained in this block, called C-path.
	 */
	public int C;

	/**
	 * Creates a new root block.
	 *
	 * @param factor1 factor of the first sequence being aligned
	 * @param factor2 factor of the second sequence being aligned
	 */
	LocalAlignmentBlock (Factor factor1, Factor factor2)
	{
		super (factor1, factor2);

		E_path_score = S_path_score = new int[] {0};
		E_path_ancestor = new LocalAlignmentBlock [] {this};
		E_path_ancestor_index = new int [] {0};
	}

	/**
	 * Creates a new alignment block, with all arrays created with the specified size.
	 *
	 * @param factor1 factor of the first sequence being aligned
	 * @param factor2 factor of the second sequence being aligned
	 * @param size size of the arrays to be created
	 */
	LocalAlignmentBlock (Factor factor1, Factor factor2, int size)
	{
		super (factor1, factor2, size);

		E_path_score = new int [size];
		E_path_ancestor = new LocalAlignmentBlock [size];
		E_path_ancestor_index = new int [size];
		S_path_score = new int [size];
		path_type = new byte [size];
	}
}
