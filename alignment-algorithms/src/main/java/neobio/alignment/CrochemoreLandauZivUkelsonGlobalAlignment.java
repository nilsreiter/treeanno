/*
 * CrochemoreLandauZivUkelsonGlobalAlignment.java
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
 * This class implements the <B>global</B> pairwise sequence alignment algorithm (with
 * linear gap penalty function) due to Maxime Crochemore, Gad Landau and Michal
 * Ziv-Ukelson (2002).
 *
 * <P>This implementation derives from the paper of M.Crochemore, G.Landau and
 * M.Ziv-Ukelson, <I>A Sub-quadratic Sequence Alignment Algorithm for Unrestricted Scoring
 * Matrices</I> (available here as
 * <A HREF="doc-files/Crochemore_Landau_Ziv-Ukelson_algorithm.pdf">PDF</A> or
 * <A HREF="doc-files/Crochemore_Landau_Ziv-Ukelson_algorithm.pdf">Postscript</A>).</P>
 *
 * <P>For a general description of the algorithm, please refer to the specification of the
 * abstract {@linkplain CrochemoreLandauZivUkelson} superclass.</P>
 *
 * <P>This class consist mainly of methods that:</P>
 *
 * <LU>
 * <LI>create and compute all information of a block (see {@link #createBlock createBlock}
 * and its variants);
 * <LI>compute the output border of a block (see {@link #computeOutputBorder
 * computeOutputBorder};
 * <LI>locate the score of a high scoring global alignment in the block table (see {@link
 * #locateScore locateScore};
 * <LI>build an optimal global alignment from the information stored in the block table
 * (see {@link #buildOptimalAlignment buildOptimalAlignment}.
 * </LU>
 *
 * @see CrochemoreLandauZivUkelson
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @author Sergio A. de Carvalho Jr.
 */
public class CrochemoreLandauZivUkelsonGlobalAlignment extends CrochemoreLandauZivUkelson
{
	/**
	 * Creates and computes all information of an alignment block. Its main job is to
	 * compute the DIST column for the block. It then request the
	 * <CODE>computeOutputBorder</CODE> method to compute the block's output border.
	 *
	 * @param factor1 factor of the first sequence
	 * @param factor2 factor of the second sequence
	 * @param row row index of the block in the block table
	 * @param col column index of the block in the block table
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException if the scoring scheme is not compatible
	 * with the sequences being aligned
	 */
	protected AlignmentBlock createBlock (Factor factor1, Factor factor2, int row,
		int col) throws IncompatibleScoringSchemeException
	{
		AlignmentBlock	block, left_prefix, diag_prefix, top_prefix;
		int				size, lr, lc, score_ins, score_sub, score_del, ins, del, sub, max;

		lr = factor1.length();
		lc = factor2.length();
		size = lr + lc + 1;

		block = new AlignmentBlock (factor1, factor2, size);

		// set up pointers to prefixes
		left_prefix = getLeftPrefix (block);
		diag_prefix = getDiagonalPrefix (block);
		top_prefix  = getTopPrefix (block);

		// compute scores
		score_ins = scoreInsertion (factor2.getNewChar());
		score_sub = scoreSubstitution (factor1.getNewChar(), factor2.getNewChar());
		score_del = scoreDeletion (factor1.getNewChar());

		// compute dist column and direction
		for (int i = 0; i < size; i++)
		{
			// compute optimal path to
			// input border's ith position

			ins = sub = del = Integer.MIN_VALUE;

			if (i < size - 1)
				ins = left_prefix.dist_column[i] + score_ins;

			if ((i > 0) && (i < size - 1))
				sub = diag_prefix.dist_column[i - 1] + score_sub;

			if (i > 0)
				del = top_prefix.dist_column[i - 1] + score_del;

			block.dist_column[i] = max = max (ins, sub, del);

			// record the direction to of the optimal
			// path to input border's ith position
			if (max == ins)
				block.direction[i] = LEFT_DIRECTION;
			else if (max == sub)
				block.direction[i] = DIAGONAL_DIRECTION;
			else
				block.direction[i] = TOP_DIRECTION;
		}

		computeOutputBorder (block, row, col, size, lc, lr);

		return block;
	}

	/**
	 * Creates the root block. This is a special case of the <CODE>createBlock</CODE>
	 * method. No information is actually computed.
	 *
	 * @param factor1 factor of the first sequence
	 * @param factor2 factor of the second sequence
	 * @return the root block
	 */
	protected AlignmentBlock createRootBlock (Factor factor1, Factor factor2)
	{
		return new AlignmentBlock (factor1, factor2);
	}

	/**
	 * Creates and computes all information of an alignment block of the first row of the
	 * block table. This is a special case of the <CODE>createBlock</CODE> method.
	 *
	 * @param factor1 factor of the first sequence
	 * @param factor2 factor of the second sequence
	 * @param col column index of the block in the block table
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException if the scoring scheme is not compatible
	 * with the sequences being aligned
	 * @see #createBlock createBlock
	 */
	protected AlignmentBlock createFirstRowBlock (Factor factor1, Factor factor2, int col)
		throws IncompatibleScoringSchemeException
	{
		AlignmentBlock	block, left_prefix;
		int				size, lr, lc, score_ins;

		lr = 0; // factor1.length();
		lc = factor2.length();
		size = lr + lc + 1;

		block = new AlignmentBlock (factor1, factor2, size);

		// set up pointer to left prefix
		left_prefix = getLeftPrefix (block);

		// compute insertion's score
		score_ins = scoreInsertion (factor2.getNewChar());

		// compute dist column and direction
		for (int i = 0; i < lc; i++)
		{
			block.dist_column[i] = left_prefix.dist_column[i] + score_ins;
			block.direction[i] = LEFT_DIRECTION;
		}

		// last position
		block.dist_column[lc] = 0;
		block.direction[lc] = STOP_DIRECTION;

		computeOutputBorder (block, 0, col, size, lc, lr);

		return block;
	}

	/**
	 * Creates and computes all information of an alignment block of the first column of
	 * the block table. This is a special case of the <CODE>createBlock</CODE> method.
	 *
	 * @param factor1 factor of the first sequence
	 * @param factor2 factor of the second sequence
	 * @param row row index of the block in the block table
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException if the scoring scheme is not compatible
	 * with the sequences being aligned
	 * @see #createBlock createBlock
	 */
	protected AlignmentBlock createFirstColumnBlock (Factor factor1, Factor factor2,
		int row) throws IncompatibleScoringSchemeException
	{
		AlignmentBlock	block, top_prefix;
		int				size, lr, lc, score_del;

		lr = factor1.length();
		lc = 0; // factor2.length();
		size = lr + lc + 1;

		block = new AlignmentBlock (factor1, factor2, size);

		// set up pointer to top prefix
		top_prefix = getTopPrefix (block);

		// compute deletion's score
		score_del = scoreDeletion (factor1.getNewChar());

		// first position
		block.dist_column[0] = 0;
		block.direction[0] = STOP_DIRECTION;

		// compute dist column and direction
		for (int i = 1; i < size; i++)
		{
			block.dist_column[i] = top_prefix.dist_column[i - 1] + score_del;
			block.direction[i] = TOP_DIRECTION;
		}

		computeOutputBorder (block, row, 0, size, lc, lr);

		return block;
	}

	/**
	 * Computes the output border of a block. This is performed in five steps:
	 *
	 * <LU>
	 * <LI>Retrieve the block's input border;
	 * <LI>Retrieve the block's complete DIST matrix;
	 * <LI>Create an interface to the {@linkplain OutMatrix OUT} matrix from the input
	 * border and DIST matrix;
	 * <LI>Use {@linkplain Smawk SMAWK} to compute all column maxima of the OUT matrix
	 * (SMAWK finds the index of the row that contains the maximum value of a column);
	 * <LI>Assemble the output border by extracting the maximum values of each column of
	 * the OUT matrix using the information obtained in the previous step.
	 * </LU>
	 *
	 * @param block the block for which the output border is to be computed
	 * @param row row index of the block in the block table
	 * @param col column index of the block in the block table
	 * @param dim dimension of the output border
	 * @param lc number of columns of the block
	 * @param lr number of row of the block
	 */
	protected void computeOutputBorder (AlignmentBlock block, int row, int col, int dim,
		int lc, int lr)
	{
		int[] input = assembleInputBorder (dim, row, col, lr);

		int[][] dist = assembleDistMatrix (block, dim, row, col, lc);

		// update the interface to the OUT matrix
		out_matrix.setData (dist, input, dim, lc);

		// compute source_path using Smawk
		smawk.computeColumnMaxima (out_matrix, block.source_path);

		// update output border
		for (int i = 0; i < dim; i++)
			block.output_border[i] = out_matrix.valueAt(block.source_path[i], i);
	}

	/**
	 * Builds an optimal global alignment between the loaded sequences after the block
	 * table has been computed. This method traces a path back in the block table, from
	 * the last block to the first.
	 *
	 * @return an optimal global alignment
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see CrochemoreLandauZivUkelson#traverseBlock
	 */
	protected PairwiseAlignment buildOptimalAlignment ()
		throws IncompatibleScoringSchemeException
	{
		AlignmentBlock	block, ancestor;
		StringBuffer	gapped_seq1, tag_line, gapped_seq2;
		int				source, dest, ancestor_source;
		int				row, col;

		gapped_seq1	= new StringBuffer();
		tag_line	= new StringBuffer();
		gapped_seq2	= new StringBuffer();

		// start at the last row, last column of block table
		row	  = num_rows - 1; col = num_cols - 1;
		block = block_table[row][col];
		dest  = block.factor2.length();

		while (row > 0 || col > 0)
		{
			block	 = block_table[row][col];
			source	 = block.source_path[dest];
			ancestor = block.ancestor[dest];

			ancestor_source = source;
			if (dest > block.factor2.length())
				ancestor_source -= (block.factor1.length() - ancestor.factor1.length());

			traverseBlock (ancestor, ancestor_source, gapped_seq1, tag_line, gapped_seq2);

			if (row == 0)
			{
				col = col - 1;
				dest = block_table[row][col].factor2.length();
			}
			else if (col == 0)
			{
				row = row - 1;
				dest = 0;
			}
			else
			{
				if (source < block.factor1.length())
				{
					col = col - 1;
					dest = block_table[row][col].factor2.length() + source;
				}
				else if (source == block.factor1.length())
				{
					row = row - 1; col = col - 1;
					dest = block_table[row][col].factor2.length();
				}
				else
				{
					row = row - 1;
					dest = source - block.factor1.length();
				}
			}
		}

		return new PairwiseAlignment (gapped_seq1.toString(), tag_line.toString(),
			gapped_seq2.toString(), locateScore());
	}

	/**
	 * Locate the score of the highest scoring global alignment in the block table. This
	 * value is found in the output border of the last block (last row, last column).
	 *
	 * @return the score of the highest scoring global alignment
	 */
	protected int locateScore ()
	{
		AlignmentBlock last_block = block_table[num_rows - 1][num_cols - 1];

		return last_block.output_border[last_block.factor2.length()];
	}
}
