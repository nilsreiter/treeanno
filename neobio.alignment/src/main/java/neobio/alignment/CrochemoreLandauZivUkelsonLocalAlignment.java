/*
 * CrochemoreLandauZivUkelsonLocalAlignment.java
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
 * This class implements the <B>local</B> pairwise sequence alignment algorithm (with
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
 * <P>This algorithm works essentially in the same way as the global alignment version.
 * The main differences is that an aptimal path can either be contained entirely in a
 * block (called <B>C-path</B>) or be a block-crossing path. A block-crossing path
 * consists of a (possibly empty) <B>S-path</B> (a path that starts inside a block and
 * ends in its output border), followed by any number of paths that cross a block from its
 * input border to its output border, and ending in an <B>E-path</B> (a path that starts
 * in the input border of a block and ends inside the block).</P>
 *
 * <P>Therefore, it is necessary to compute extra information to keep track of these
 * possibilities. This is accomplished by using an instance of a {@linkplain
 * LocalAlignmentBlock} (which extends the {@linkplain AlignmentBlock} class) for every
 * block in the block table.</P>
 *
 * @see CrochemoreLandauZivUkelson
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @author Sergio A. de Carvalho Jr.
 */
public class CrochemoreLandauZivUkelsonLocalAlignment extends CrochemoreLandauZivUkelson
{
	/**
	 * A constant that indicates that the best path ending at a given entry of the output
	 * border is a block-crossing path (one that starts outside the block).
	 */
	protected static final byte TYPE_CROSSING_PATH = 0;

	/**
	 * A constant that indicates that the best path ending at a given entry of the output
	 * border is a S-path (one that starts inside the block).
	 */
	protected static final byte TYPE_S_PATH = 1;

	/**
	 * A constant that indicates that the high scoring path ending in a given block is a
	 * C-path, i.e. one that starts inside the block.
	 */
	protected static final byte TYPE_C_PATH = 2;

	/**
	 * A constant that indicates that the high scoring path ending in a given block is an
	 * E-path, i.e. one that starts at its input border.
	 */
	protected static final byte TYPE_E_PATH = 3;

	/**
	 * The score of the high scoring local alignment found.
	 */
	protected int max_score;

	/**
	 * The row index of a block (in the block table) where the high scoring local
	 * alignment ends.
	 */
	protected int max_row;

	/**
	 * The column index of a block (in the block table) where the high scoring local
	 * alignment ends.
	 */
	protected int max_col;

	/**
	 * The type of the high scoring local alignment found.
	 */
	protected byte max_path_type;

	/**
	 * If the high scoring local alignment ends in an E-path at a block B, this field
	 * contains the index of the entry in the input border of B that where the E-path
	 * starts.
	 */
	protected int max_source_index;

	/**
	 * Creates and computes all information of an alignment block. This method works
	 * essentially in the same way as its global alignment counterpart. Its main job is to
	 * compute the DIST column for the block. It then request the
	 * <CODE>computeOutputBorder</CODE> method to compute the block's output border. It
	 * also computes all S, C and E-paths of this block. Finally, it checks if the C-path
	 * of this block is higher than the highest score found so far.
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
		LocalAlignmentBlock	block, left_prefix, diag_prefix, top_prefix;
		int					size, lr, lc, max, ins_E, del_E;
		int					score_ins, score_sub, score_del, ins, del, sub;

		lr = factor1.length();
		lc = factor2.length();
		size = lr + lc + 1;

		block = new LocalAlignmentBlock (factor1, factor2, size);

		// retrieve pointers to prefixes
		left_prefix = (LocalAlignmentBlock) getLeftPrefix (block);
		diag_prefix = (LocalAlignmentBlock) getDiagonalPrefix (block);
		top_prefix  = (LocalAlignmentBlock) getTopPrefix (block);

		// compute scores
		score_ins = scoreInsertion (factor2.getNewChar());
		score_sub = scoreSubstitution (factor1.getNewChar(), factor2.getNewChar());
		score_del = scoreDeletion (factor1.getNewChar());

		// compute block's data
		for (int i = 0; i < size; i++)
		{
			ins = sub = del = ins_E = del_E = Integer.MIN_VALUE;

			if (i < size - 1)
			{
				ins = left_prefix.dist_column[i] + score_ins;
				ins_E = left_prefix.E_path_score[i];
			}

			if ((i > 0) && (i < size - 1))
			{
				sub = diag_prefix.dist_column[i - 1] + score_sub;
			}

			if (i > 0)
			{
				del = top_prefix.dist_column[i - 1] + score_del;
				del_E = top_prefix.E_path_score[i - 1];
			}

			block.dist_column[i] = max = max (ins, sub, del);

			if (max == ins)
				block.direction[i] = LEFT_DIRECTION;
			else if (max == sub)
				block.direction[i] = DIAGONAL_DIRECTION;
			else
				block.direction[i] = TOP_DIRECTION;

			block.E_path_score[i] = max = max (ins_E, block.dist_column[i], del_E);

			if (max == ins_E)
			{
				block.E_path_ancestor[i] = left_prefix.E_path_ancestor[i];
				block.E_path_ancestor_index[i] = left_prefix.E_path_ancestor_index[i];
			}
			else if (max == block.dist_column[i])
			{
				block.E_path_ancestor[i] = block;
				block.E_path_ancestor_index[i] = i;
			}
			else
			{
				block.E_path_ancestor[i] = top_prefix.E_path_ancestor[i - 1];
				block.E_path_ancestor_index[i] = top_prefix.E_path_ancestor_index[i - 1];
			}

			if (i < lc)
			{
				block.S_path_score[i] = left_prefix.S_path_score[i];
			}
			else if (i == lc)
			{
				ins = left_prefix.S_path_score[i-1] + score_ins;
				sub = diag_prefix.S_path_score[i-1] + score_sub;
				del = top_prefix.S_path_score[i]    + score_del;

				block.S_path_score[i] = max = max (0, ins, sub, del);

				if (max == ins)
					block.S_direction = LEFT_DIRECTION;
				else if (max == sub)
					block.S_direction = DIAGONAL_DIRECTION;
				else if (max == del)
					block.S_direction = TOP_DIRECTION;
				else
					block.S_direction = STOP_DIRECTION;
			}
			else
			{
				block.S_path_score[i] = top_prefix.S_path_score[i - 1];
			}
		}

		computeOutputBorder (block, row, col, size, lc, lr);

		ins = left_prefix.C;
		del = top_prefix.C;
		block.C = max = max (ins, block.S_path_score[lc], del);

		if (block.C > max_score)
		{
			// assert block.C == block.S_path_score[lc]; => always true
			max_score = block.C;
			max_row = row;
			max_col = col;
			max_path_type = TYPE_C_PATH;
		}

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
		// resets the variables that keep track
		// of the high scoring alignment
		max_row = max_col = max_score = 0;
		max_path_type = TYPE_C_PATH;

		return new LocalAlignmentBlock (factor1, factor2);
	}

	/**
	 * Creates and computes all information of an alignment block of the first column of
	 * the block table. This is a special case of the <CODE>createBlock</CODE> method.
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
		LocalAlignmentBlock	block, left_prefix;
		int			size, lr, lc, score_ins;

		lr = 0; // factor1.length();
		lc = factor2.length();
		size = lr + lc + 1;

		block = new LocalAlignmentBlock (factor1, factor2, size);

		// retrieve a pointer to left prefix
		left_prefix = (LocalAlignmentBlock) getLeftPrefix (block);

		// compute insertion's score
		score_ins = scoreInsertion (factor2.getNewChar());

		// compute block's data
		for (int i = 0; i < lc; i++)
		{
			block.dist_column[i] = left_prefix.dist_column[i] + score_ins;
			block.direction[i] = LEFT_DIRECTION;
			block.S_path_score[i] = left_prefix.S_path_score[i];

			block.E_path_score[i] = left_prefix.E_path_score[i];
			block.E_path_ancestor[i] = left_prefix.E_path_ancestor[i];
			block.E_path_ancestor_index[i] = left_prefix.E_path_ancestor_index[i];
			if (block.dist_column[i] > block.E_path_score[i])
			{
				block.E_path_score[i] = block.dist_column[i];
				block.E_path_ancestor[i] = block;
				block.E_path_ancestor_index[i] = i;
			}
		}

		// last position
		block.E_path_score[lc] = block.dist_column[lc] = 0;
		block.direction[lc] = STOP_DIRECTION;

		block.E_path_ancestor[lc] = block;
		block.E_path_ancestor_index[lc] = lc;

		block.S_direction = LEFT_DIRECTION;
		block.S_path_score[lc] = left_prefix.S_path_score[lc - 1] + score_ins;
		if (block.S_path_score[lc] <= 0)
		{
			block.S_path_score[lc] = 0;
			block.S_direction = STOP_DIRECTION;
		}

		computeOutputBorder (block, 0, col, size, lc, lr);

		block.C = max (left_prefix.C, block.S_path_score[lc]);

		if (block.C > max_score)
		{
			max_score = block.C;
			max_row = 0;
			max_col = col;
			max_path_type = TYPE_C_PATH;
		}

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
		LocalAlignmentBlock	block, top_prefix;
		int			size, lr, lc, score_del;

		lr = factor1.length();
		lc = 0; // factor2.length();
		size = lr + lc + 1;

		block = new LocalAlignmentBlock (factor1, factor2, size);

		// retrieve a pointer to top prefix
		top_prefix  = (LocalAlignmentBlock) getTopPrefix (block);

		// compute deletion's score
		score_del = scoreDeletion (factor1.getNewChar());

		// first position
		block.E_path_score[0] = block.dist_column[0] = 0;
		block.direction[0] = STOP_DIRECTION;

		block.E_path_ancestor[0] = block;
		block.E_path_ancestor_index[0] = 0;

		block.S_direction = TOP_DIRECTION;
		block.S_path_score[0] = top_prefix.S_path_score[0] + score_del;
		if (block.S_path_score[0] <= 0)
		{
			block.S_path_score[0] = 0;
			block.S_direction = STOP_DIRECTION;
		}

		// compute block's data
		for (int i = 1; i < size; i++)
		{
			block.dist_column[i] = top_prefix.dist_column[i - 1] + score_del;
			block.direction[i] = TOP_DIRECTION;
			block.S_path_score[i] = top_prefix.S_path_score[i - 1];

			block.E_path_score[i] = top_prefix.E_path_score[i - 1];
			block.E_path_ancestor[i] = top_prefix.E_path_ancestor[i - 1];
			block.E_path_ancestor_index[i] = top_prefix.E_path_ancestor_index[i - 1];
			if (block.dist_column[i] > block.E_path_score[i])
			{
				block.E_path_score[i] = block.dist_column[i];
				block.E_path_ancestor[i] = block;
				block.E_path_ancestor_index[i] = i;
			}
		}

		computeOutputBorder (block, row, 0, size, lc, lr);

		block.C = max (block.S_path_score[lc], top_prefix.C);

		if (block.C > max_score)
		{
			max_score = block.C;
			max_row = row;
			max_col = 0;
			max_path_type = TYPE_C_PATH;
		}

		return block;
	}

	/**
	 * Computes the output border of a block. This method works essentially in the same
	 * way as its global alignment counterpart:
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
	 * <P>However, it also check if there is a better path starting inside the block (an
	 * S path) and oupdate the output border accordingly. It also checks if this block has
	 * any path of score higher than the maximum score found so far.
	 *
	 * @param block the block for which the output border is to be computed
	 * @param row row index of the block in the block table
	 * @param col column index of the block in the block table
	 * @param dim dimension of the output border
	 * @param lc number of columns of the block
	 * @param lr number of row of the block
	 */
	protected void computeOutputBorder (LocalAlignmentBlock block, int row, int col, int
		dim, int lc, int lr)
	{
		int[] input = assembleInputBorder (dim, row, col, lr);

		int[][] dist = assembleDistMatrix (block, dim, row, col, lc); // (AlignmentBlock)

		// build an interface to the OUT matrix
		out_matrix.setData (dist, input, dim, lc);

		// compute source_path using SMAWK
		smawk.computeColumnMaxima(out_matrix, block.source_path);

		// update output border
		for (int i = 0; i < dim; i++)
		{
			block.path_type[i] = TYPE_CROSSING_PATH;
			block.output_border[i] = out_matrix.valueAt(block.source_path[i], i);

			// check if there is a better path starting inside the block
			// (if there is a path of equal score, preference is given
			// to the S-path because it ends sooner)
			if (block.S_path_score[i] >= block.output_border[i])
			{
				block.output_border[i] = block.S_path_score[i];
				block.path_type[i] = TYPE_S_PATH;
			}

			// check if this block contains a score higher
			// than the best path found so far
			if (input[i] + block.E_path_score[i] > max_score)
			{
				max_score = input[i] + block.E_path_score[i];
				max_row = row;
				max_col = col;
				max_source_index = i;
				max_path_type = TYPE_E_PATH;
			}
		}
	}

	/**
	 * Builds an optimal local alignment between the loaded sequences after the block
	 * table has been computed by tracing a path back in the block table.
	 *
	 * @return an optimal global alignment
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see CrochemoreLandauZivUkelson#traverseBlock
	 */
	protected PairwiseAlignment buildOptimalAlignment ()
		throws IncompatibleScoringSchemeException
	{
		LocalAlignmentBlock	block;
		StringBuffer		gapped_seq1, tag_line, gapped_seq2;

		gapped_seq1	= new StringBuffer();
		tag_line	= new StringBuffer();
		gapped_seq2	= new StringBuffer();

		block = (LocalAlignmentBlock) block_table[max_row][max_col];

		if (max_path_type == TYPE_C_PATH)
		{
			// a C-path is essentially an S-path
			traverseS_Path (block, gapped_seq1, tag_line, gapped_seq2);
		}
		else
		{
			traverseBlockCrossingPath (block, gapped_seq1, tag_line, gapped_seq2);
		}

		return new PairwiseAlignment (gapped_seq1.toString(), tag_line.toString(),
			gapped_seq2.toString(), locateScore());
	}

	/**
	 * Traverses a series of block crossing paths to retrieve an optimal alignment. A
	 * block-crossing path consists of a (possibly empty) <B>S-path</B> (a path that
	 * starts inside a block and ends in its output border), followed by any number of
	 * paths that cross a block from its input border to its output border, and ending in
	 * an <B>E-path</B> (a path that starts in the input border of a block and ends inside
	 * the block).
	 *
	 * @param block the block to be traversed
	 * @param gapped_seq1 the StringBuffer to where the gapped sequence 1 is written to
	 * @param tag_line the StringBuffer to where the tag_line is written to
	 * @param gapped_seq2 the StringBuffer to where the gapped sequence 2 is written to
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected void traverseBlockCrossingPath (LocalAlignmentBlock block,
		StringBuffer gapped_seq1, StringBuffer tag_line, StringBuffer gapped_seq2)
		throws IncompatibleScoringSchemeException
	{
		LocalAlignmentBlock	ancestor;
		int					source, dest, ancestor_source;
		int					row, col;

		row = max_row;
		col = max_col;

		// recover the E-path
		source = max_source_index;
		ancestor = block.E_path_ancestor[source];
		ancestor_source = block.E_path_ancestor_index[source];
		traverseBlock (ancestor, ancestor_source, gapped_seq1, tag_line, gapped_seq2);

		// now recover crossing paths
		while (true)
		{
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

			// check if has reached the root block
			if (!(row > 0 || col > 0)) break;

			block = (LocalAlignmentBlock) block_table[row][col];

			if (block.path_type[dest] == TYPE_S_PATH)
			{
				// last part, an S-path, and we're done
				ancestor = (LocalAlignmentBlock) block.ancestor[dest];
				traverseS_Path (ancestor, gapped_seq1, tag_line, gapped_seq2);
				break;
			}

			source	 = block.source_path[dest];
			ancestor = (LocalAlignmentBlock) block.ancestor[dest];
			ancestor_source = source;

			if (dest > block.factor2.length())
				ancestor_source -= (block.factor1.length() - ancestor.factor1.length());

			traverseBlock (ancestor, ancestor_source, gapped_seq1, tag_line, gapped_seq2);
		}
	}

	/**
	 * Traverses an S-path of a block to retrieve a part of an optimal alignment from the
	 * new vertex of a block to entry in its input border. This method is essentially
	 * similar to the <CODE>traverseBlock</CODE>. The only difference is that it uses
	 * the information of the <CODE>S_direction field</CODE> of the
	 * <CODE>LocalAlignmentBlock</CODE> class.
	 *
	 * @param block the block to be traversed
	 * @param gapped_seq1 the StringBuffer to where the gapped sequence 1 is written to
	 * @param tag_line the StringBuffer to where the tag_line is written to
	 * @param gapped_seq2 the StringBuffer to where the gapped sequence 2 is written to
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected void traverseS_Path (LocalAlignmentBlock block, StringBuffer gapped_seq1,
		StringBuffer tag_line, StringBuffer gapped_seq2)
		throws IncompatibleScoringSchemeException
	{
		char char1, char2;

		while (block.S_direction != STOP_DIRECTION)
		{
			char1 = block.factor1.getNewChar();
			char2 = block.factor2.getNewChar();

			switch (block.S_direction)
			{
				case LEFT_DIRECTION:
					gapped_seq1.insert (0, GAP_CHARACTER);
					tag_line.insert (0, GAP_TAG);
					gapped_seq2.insert (0, char2);

					block = (LocalAlignmentBlock) getLeftPrefix (block);
					break;

				case DIAGONAL_DIRECTION:
					gapped_seq1.insert (0, char1);
					if (char1 == char2)
						if (useMatchTag())
							tag_line.insert (0, MATCH_TAG);
						else
							tag_line.insert (0, char1);
					else if (scoreSubstitution(char1, char2) > 0)
						tag_line.insert (0, APPROXIMATE_MATCH_TAG);
					else
						tag_line.insert (0, MISMATCH_TAG);
					gapped_seq2.insert(0, char2);

					block = (LocalAlignmentBlock) getDiagonalPrefix (block);
					break;

				case TOP_DIRECTION:
					gapped_seq1.insert (0, char1);
					tag_line.insert (0, GAP_TAG);
					gapped_seq2.insert (0, GAP_CHARACTER);

					block = (LocalAlignmentBlock) getTopPrefix (block);
					break;
			}
		}
	}

	/**
	 * Returns the score of the high scoring local alignment in the block table.
	 *
	 * @return the score of the highest scoring local alignment
	 */
	protected int locateScore ()
	{
		return max_score;
	}
}
