/*
 * CrochemoreLandauZivUkelson.java
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

import java.io.Reader;
import java.io.IOException;

/**
 * This abstract class is the superclass of both global and local sequence alignment
 * algorithms (with linear gap penalty function) due to Maxime Crochemore, Gad Landau and
 * Michal Ziv-Ukelson (2002).
 *
 * <P>This implementation derives from the paper of M.Crochemore, G.Landau and
 * M.Ziv-Ukelson, <I>A Sub-quadratic Sequence Alignment Algorithm for Unrestricted Scoring
 * Matrices</I> (available here as
 * <A HREF="doc-files/Crochemore_Landau_Ziv-Ukelson_algorithm.pdf">PDF</A> or
 * <A HREF="doc-files/Crochemore_Landau_Ziv-Ukelson_algorithm.pdf">Postscript</A>).</P>
 *
 * <P>It employs Lempel-Ziv compression techniques to speed up the classic dynamic
 * programmin approach to sequence alignment (see {@linkplain NeedlemanWunsch} and
 * {@linkplain SmithWaterman} classes). It reduces the time and space complexity from
 * O(n<SUP>2</SUP>) to O(n<SUP>2</SUP>/log n). In fact, the complexity is actually O(h
 * n<SUP>2</SUP>/log n), where 0 <= h <= 1 is a real number denoting the entropy of the
 * text (a measure of how compressible it is). This means that, the more compressible a
 * sequence is, the less memory the algorithm will require, and the faster it will
 * run.</P>
 *
 * <P>The idea behind this improvement is to identify repetitions in the sequences and
 * reuse the computation of their alignments. The first step is, therefore, to parse the
 * sequences into LZ78-like factors. LZ78 is a popular compression algorithm of the
 * Lempel-Ziv familiy due to J.Ziv and A.Lempel (1978). This factorisation is accomplished
 * by the {@linkplain FactorSequence} class (for more information about this
 * factorisation, please refer to the specification of that class) that builds a
 * doubly-linked list of factors. Each factor is an instance of the {@linkplain Factor}
 * class (refer to the specification of this class for more information).</P>
 *
 * <P>Once the sequences have been parsed, the algorithm builds a matrix of blocks, called
 * block table, that is vaguely similar to the dynamic programming matrix used by both
 * <CODE>NeedlemanWunsch</CODE> and <CODE>SmithWaterman</CODE>. Each block contains an
 * instance of an {@linkplain AlignmentBlock} (please refer to its specification for more
 * information on what information is stored) and represents the alignment beteween one
 * factor of each sequence. This block table is, in fact, a partition of the alignment
 * graph.</P>
 *
 * <P>Consider a block B which corresponds to the alignment of factor F1 = xa from
 * sequence S1 and factor F2 = yb from sequence S2. Here, F1 extends a previous factor of
 * S1 with character a, while F2 extends a previous factor of S2 with character b. We can
 * define the input border of B as the set of values at the left and top borders of block
 * B, and the output border as the set of values at the right and bottom borders of B.
 * Moreover, we can define the following prefix blocks of B:</P>
 *
 * <UL>
 * <LI><B>Left prefix</B> - is the block that contains the alignment of factor F1 with
 * a factor F2' = y (a prefix of factor F2).
 * <LI><B>Diagonal prefix</B> - is the block that contains the alignment of factor F1' = x
 * (a prefix of factor F1) with a factor F2' = y (a prefix of factor F2).
 * <LI><B>Top prefix</B> - is the block that contains the alignment of factor F1' = x (a
 * prefix of factor F1) with factor F2.
 * </UL>
 *
 * <P>Note that each factor has a pointer to its prefix factor, called ancestor (see the
 * specification of the <CODE>Factor</CODE> class). This pointer makes it easy to retrieve
 * any of the three prefix blocks of B in constant time.</P>
 *
 * <P>Rather than computing each value of the alignment block B, the algorithm will only
 * compute the values on its input and output borders. This is precisely what makes it
 * more efficient.</P>
 *
 * <P>In this class there is a general specification of how the block table is computed
 * (see the {@link #computeBlockTable computeBlockTable} method for details). The actual
 * method depends on the subclasses. In general, there are two phases:</P>
 *
 * <UL>
 * <LI><B>Encoding</B> - the structure of a block B is studied and represented in an
 * efficient way by computing weights of optimal paths connecting each entry of its input
 * border to each entry of its output border. This information is encoded in a DIST matrix
 * where DIST[i,j] stores the weight of an optimal paths connecting entry i of the input
 * border to entry j the output border of B.
 * <LI><B>Propagation</B> - the input border of a block B is retrieved (from the left and
 * top blocks of B) and its output border is computed with the help of the DIST matrix.
 * </UL>
 *
 * <P>In fact, for each block, only one column of the DIST matrix needs to be computed,
 * all other columns are actually retrieved from its prefix blocks. This is precisely what
 * is accomplished by the {@link #assembleDistMatrix assembleDistMatrix} method found in
 * this class (it is general enough for both global and local alignment versions of the
 * algorithm.</P>
 *
 * <P>From the DIST matrix, we obtain the OUT matrix defined as
 * <CODE>OUT[i,j] = I[i] + DIST[i,j]</CODE> where I is the input border array. This means
 * that the OUT matrix is the DIST matrix updated by the input border of a block. The
 * output border is then computed from the OUT matrix by taking the maximum value of each
 * column. This class also have a general method for assembling the input border (see
 * {@link #assembleInputBorder assembleInputBorder}</P>
 *
 * <P>The OUT matrix is encoded by the {@linkplain OutMatrix} class that takes as
 * both a DIST matrix and an input border array. Note that <B>it does not compute the OUT
 * matrix</B>, it just stores the necessary information to retrieve a value at any
 * position of the matrix.</P>
 *
 * <P>A naive approach to compute the output border of a block from the OUT matrix of size
 * n x n would take a time proportional to n<SUP>2</SUP>. However, it happens that, due to
 * the nature of the DIST matrix, both DIST and OUT matrices are Monge arrays, which
 * implies that they are also <I>totally monotone</I>. This property allows the
 * computation of the output border of B in linear time with the SMAWK algorithm (see the
 * specification of the {@linkplain Smawk} class for more information on SMAWK).</P>
 *
 * <P>This class contains a general specification that is pertinent to both global and
 * local versions of the algorithm. For more information on each version of, please refer
 * to the appropriate subclass.</P>
 *
 * <P><B>A note about the performance of these algorithms.</B> Although theoretical
 * results suggest that these algorithms are faster and consume less memory than the
 * classical methods, in practice it is hard to realise their performance gains.
 *
 * <P>These algorithms are extremely complex and require the storage of many extra
 * pointers and other auxiliary data for each block (see the <CODE>AlignmentBlock</CODE>
 * class for more details). Hence, even though the space requirement is
 * O(n<SUP>2</SUP>/log n), which is less than O(n<SUP>2</SUP>), in practice, for most of
 * the cases these algorithms will take more time and memory space than their clasical
 * counterparts (we have to keep in mind that the Big Oh notation ignores all constants
 * involved).</P>
 *
 * <P>Therefore, in order to realise the full power of these algorithms, they have to be
 * used with extremly large and redundant sequences. This will allow a proper
 * reutilisation of the computations and, maybe, provide an improvement in terms of space
 * and run time. For instance, it is easy to devise such a sequence if we use a
 * one-character alphabet because, in this case, a sequence is factorised into a series
 * of factors that are a prefix of the next.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see CrochemoreLandauZivUkelsonGlobalAlignment
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @see NeedlemanWunsch
 * @see SmithWaterman
 * @see FactorSequence
 * @see AlignmentBlock
 * @see OutMatrix
 * @see Smawk
 * @see #computeBlockTable
 * @see #assembleDistMatrix
 */
public abstract class CrochemoreLandauZivUkelson extends PairwiseAlignmentAlgorithm
{
	/**
	 * A constant that indicates that the source of an optimal path has been reached in a
	 * block and that the trace back procedure to retrieve a high scoring alignment can
	 * stop.
	 */
	protected static final byte STOP_DIRECTION = 0;

	/**
	 * A constant that indicates that the left direction must be followed to reach the
	 * source of an optimal path in a block during the trace back procedure to retrieve a
	 * high scoring alignment.
	 */
	protected static final byte LEFT_DIRECTION = 1;

	/**
	 * A constant that indicates that the diagonal direction must be followed to reach the
	 * source of an optimal path in a block during the trace back procedure to retrieve a
	 * high scoring alignment.
	 */
	protected static final byte DIAGONAL_DIRECTION = 2;

	/**
	 * A constant that indicates that the top direction must be followed to reach the
	 * source of an optimal path in a block during the trace back procedure to retrieve a
	 * high scoring alignment.
	 */
	protected static final byte TOP_DIRECTION = 3;

	/**
	 * The first factorised sequence being aligned.
	 */
	protected FactorSequence seq1;

	/**
	 * The second factorised sequence being aligned.
	 */
	protected FactorSequence seq2;

	/**
	 * The block table, which is a matrix of alignment blocks where each block represents
	 * the alignment between one factor of each sequence.
	 */
	protected AlignmentBlock[][] block_table;

	/**
	 * Number of rows of the block table. It is determined by the number of factors of the
	 * first sequence.
	 */
	protected int num_rows;

	/**
	 * Number of columns of the block table. It is determined by the number of factors of
	 * the second sequence.
	 */
	protected int num_cols;

	/**
	 * An instance of the <CODE>Smawk</CODE> class that implements the SMAWK algorithm to
	 * compute the column maxima of a totally monotone matrix. It is used to speed up the
	 * computation of the output border of a block.
	 */
	protected Smawk smawk = new Smawk();

	/**
	 * An instance of the <CODE>OutMatrix</CODE> class that encodes the OUT matrix of a
	 * block when supplied with the DIST matrix and the input border array of a block.
	 * Note that it does not compute the OUT matrix itselft, it just stores the necessary
	 * information to retrieve a value at any position of the matrix.
	 *
	 * <P>This object is then used to compute the output border of a block with the
	 * <CODE>Smawk</CODE> class. Note that the <CODE>OutMatrix</CODE> class implements the
	 * <CODE>Matrix</CODE> interface as required by the <CODE>Smawk</CODE> class.
	 *
	 * @see Matrix
	 * @see Smawk
	 */
	protected OutMatrix out_matrix = new OutMatrix ();

	/**
	 * Loads sequences into <CODE>FactorSequence</CODE> instances. In case of any error,
	 * an exception is raised by the constructor of <CODE>FactorSequence</CODE> (please
	 * check the specification of that class for specific requirements).
	 *
	 * <P>A <CODE>FactorSequence</CODE> is an LZ78-like factorisation of the sequences
	 * being aligned.
	 *
	 * @param input1 Input for first sequence
	 * @param input2 Input for second sequence
	 * @throws IOException If an I/O error occurs when reading the sequences
	 * @throws InvalidSequenceException If the sequences are not valid
	 * @see FactorSequence
	 */
	protected void loadSequencesInternal (Reader input1, Reader input2)
		throws IOException, InvalidSequenceException
	{
		// load sequences into instances of CharSequence
		this.seq1 = new FactorSequence(input1);
		this.seq2 = new FactorSequence(input2);

		// determine the block table's dimensions
		this.num_rows = seq1.numFactors();
		this.num_cols = seq2.numFactors();
	}

	/**
	 * Frees pointers to loaded sequences and the the block table so that their data can
	 * be garbage collected.
	 */
	protected void unloadSequencesInternal ()
	{
		this.seq1 = null;
		this.seq2 = null;
		this.block_table = null;
	}

	/**
	 * Computes the block table (the result depends on subclasses, see
	 * <CODE>computeBlockTable</CODE> for details) and requests subclasses to retrieve an
	 * optimal alignment between the loaded sequences. The actual product depends on the
	 * subclasses which can produce a global (see
	 * <CODE>CrochemoreLandauZivUkelsonGlobalAlignment</CODE>) or local alignment (see
	 * <CODE>CrochemoreLandauZivUkelsonLocalAlignment</CODE>).
	 *
	 * <P>Subclasses are required to implement the <CODE>buildOptimalAlignment</CODE>
	 * abstract method defined by this class according to their own method.</P>
	 *
	 * @return an optimal alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see CrochemoreLandauZivUkelsonGlobalAlignment
	 * @see CrochemoreLandauZivUkelsonLocalAlignment
	 * @see #computeBlockTable
	 * @see #buildOptimalAlignment
	 */
	protected PairwiseAlignment computePairwiseAlignment ()
		throws IncompatibleScoringSchemeException
	{
		// compute block table
		computeBlockTable ();

		// build and return an optimal global alignment
		PairwiseAlignment alignment = buildOptimalAlignment ();

		// allow the block table to be garbage collected
		block_table = null;

		return alignment;
	}

	/**
	 * Computes the block table (the result depends on subclasses, see
	 * <CODE>computeBlockTable</CODE> for details) and requests subclasses to locate the
	 * score of the highest scoring alignment between the two sequences in the block
	 * table. The result depends on the subclasses, and either a global alignment
	 * (see <CODE>CrochemoreLandauZivUkelsonGlobalAlignment</CODE>) or local alignment
	 * score (see <CODE>CrochemoreLandauZivUkelsonLocalAlignment</CODE>) will be produced.
	 *
	 * <P>Subclasses are required to implement the <CODE>locateScore</CODE> abstract
	 * method defined by this class according to their own method.</P>
	 *
	 * <P>Note that this method calculates the similarity value only (it doesn't trace
	 * back into the block table to retrieve the alignment itself).</P>
	 *
	 * @return the score of the highest scoring alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see CrochemoreLandauZivUkelsonGlobalAlignment
	 * @see CrochemoreLandauZivUkelsonLocalAlignment
	 * @see #locateScore
	 */
	protected int computeScore () throws IncompatibleScoringSchemeException
	{
		// compute block table
		computeBlockTable ();

		// get score
		int	score = locateScore ();

		// allow the block table to be garbage collected
		block_table = null;

		return score;
	}

	/**
	 * Computes the block table. This method is a general specification of how the block
	 * table should be computed. It creates the block table according to the number of
	 * factors of each sequence. It then goes over each position of the block table,
	 * retrieves the corresponding factors from each sequence, and repasses the
	 * information to the subclasses that will do the actual computation of each block
	 * using the scoring scheme previously set.
	 *
	 * <P>There are four different cases that defines four abstract methods in this class,
	 * which subclasses must implement:</P>
	 *
	 * <UL>
	 * <LI><B>createRootBlock</B> - creates and computes block at row 0, column 0;
	 * <LI><B>createFirstColumnBlock</B> - creates and computes blocks at column 0 which
	 * corresponds to alignment blocks between factors of sequence 1 and an empty string;
	 * <LI><B>createFirstRowBlock</B> - creates and computes blocks at row 0 which
	 * corresponds to alignment blocks between factors of sequence 2 and an empty string;
	 * <LI><B>createBlock</B> - creates and computes blocks at row > 0 and column > 0
	 * which corresponds to alignment blocks between one factor of sequence 1 and one
	 * factor of sequence 2;
	 * </UL>
	 *
	 * <P>Note that each factor has a serial number which indicates its order in the list
	 * of factors of a sequence. This number will match with the row and column index of
	 * a block in the block table. For instance, if a block has factors F1 and F2 with
	 * serial numbers 12 and 53, respectively, this means that this block is found at row
	 * 12, column 53 of the block table.</P>
	 *
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see #createRootBlock
	 * @see #createFirstColumnBlock
	 * @see #createFirstRowBlock
	 * @see #createBlock
	 */
	protected void computeBlockTable () throws IncompatibleScoringSchemeException
	{
		Factor	factor1, factor2;
		int		r, c, max_length;

		// create block table
		block_table = new AlignmentBlock[num_rows][num_cols];

		// find the length of the longest sequence (number of characters)
		max_length = Math.max(seq1.numChars(), seq2.numChars());

		// prepares the OUT matrix object
		out_matrix.init (max_length, scoring.maxAbsoluteScore());

		// start at the root of each trie
		factor1 = seq1.getRootFactor();
		factor2 = seq2.getRootFactor();

		// check if roots' indexes are both zero
		if (factor1.getSerialNumber() != 0 || factor2.getSerialNumber() != 0)
			throw new IndexOutOfBoundsException ("Unexpected factor index.");

		// initiate first cell of block table
		block_table[0][0] = createRootBlock (factor1, factor2);

		// compute first row
		for (c = 1; c < num_cols; c++)
		{
			factor2 = factor2.getNext();

			// check if factor2's index equals its column number (except for
			// the last factor that can be a repetition of a previous one)
			if (c < num_cols - 1 && factor2.getSerialNumber() != c)
				throw new IndexOutOfBoundsException ("Unexpected factor index.");

			block_table[0][c] = createFirstRowBlock (factor1, factor2, c);
		}

		for (r = 1; r < num_rows; r++)
		{
			factor1 = factor1.getNext();

			// check if factor1's index equals its row number (except for
			// the last factor that can be a repetition of a previous one)
			if (r < num_rows - 1 && factor1.getSerialNumber() != r)
				throw new IndexOutOfBoundsException ("Unexpected factor index.");

			// go back to the root of sequence 2
			factor2 = seq2.getRootFactor();

			if (factor2.getSerialNumber() != 0)
				throw new IndexOutOfBoundsException ("Unexpected factor index.");

			// compute first column of current row
			block_table[r][0] = createFirstColumnBlock (factor1, factor2, r);

			for (c = 1; c < num_cols; c++)
			{
				factor2 = factor2.getNext();

				// check if factor2's index equals its column number (except for
				// the last factor that can be a repetition of a previous one)
				if (c < num_cols - 1 && factor2.getSerialNumber() != c)
					throw new IndexOutOfBoundsException ("Unexpected factor index.");

				// compute row r, col c
				block_table[r][c] = createBlock (factor1, factor2, r, c);
			}
		}
	}

	/**
	 * Assembles the DIST matrix of a block by retrieving the DIST columns of its prefix
	 * blocks. In fact, it also stores pointers to the owner block for each column
	 * retrieved. These pointers are later used during the trace back procedure that
	 * builds an optimal alignment from the information computed in the block table. This
	 * method is general enough to suit both global and local alignment versions of the
	 * algorithm.
	 *
	 * @param block the block for which the DIST matrix is needed
	 * @param dim the dimension of the DIST matrix
	 * @param r the row index of this block in the block table
	 * @param c the column index of this block in the block table
	 * @param lc the number of columns of the alignment block
	 * @return the DIST matrix
	 */
	protected int[][] assembleDistMatrix (AlignmentBlock block, int dim, int r, int c,
		int lc)
	{
		AlignmentBlock	ancestor;
		Factor			parent;
		int[][]			dist;
		int				i;

		dist = new int[dim][];

		// columns to the left of lc
		parent = block.factor2.getAncestor();
		for (i = lc - 1; i >= 0; i--)
		{
			ancestor = block_table[r][parent.getSerialNumber()];
			block.ancestor[i] = ancestor;
			dist[i] = ancestor.dist_column;
			parent = parent.getAncestor();
		}

		// column lc
		dist[lc] = block.dist_column;
		block.ancestor[lc] = block;

		// columns to the right of lc
		parent = block.factor1.getAncestor();
		for (i = lc + 1; i < dim; i++)
		{
			ancestor = block_table[parent.getSerialNumber()][c];
			block.ancestor[i] = ancestor;
			dist[i] = ancestor.dist_column;
			parent = parent.getAncestor();
		}

		return dist;
	}

	/**
	 * Assembles the input border of a block by retrieving the values at the output
	 * borders of the left and top blocks. This method is general enough to suit both
	 * global and local alignment versions of the algorithm. Note that it can be used to
	 * assemble the input border of any block but the root one (it will cause an
	 * <CODE>ArrayIndexOutOfBoundsException</CODE>.
	 *
	 * @param dim dimension of the input border
	 * @param r row index of the block in the block table
	 * @param c column index of the block in the block table
	 * @param lr number of row of the block
	 * @return the block's input border
	 */
	protected int[] assembleInputBorder (int dim, int r, int c, int lr)
	{
		AlignmentBlock	left = null, top = null;
		int[]	input;
		int		i;

		input = new int [dim];

		// set up pointers to the left and top blocks (if applicable)
		if (c > 0) left = block_table[r][c-1];
		if (r > 0) top	= block_table[r-1][c];

		for (i = 0; i < dim; i++)
		{
			if (i < lr)
			{
				if (left != null)
					input[i] = left.output_border[left.factor2.length() + i];
				else
					// there is no block to the left, so set a big negative value
					// to make sure it will not be used (unfortunately, MIN_VALUE
					// can overflow to a positive value when substracted by any
					// number, so we use half of it as a workaround)
					input[i] = Integer.MIN_VALUE / 2;
			}
			else if (i == lr)
			{
				if (left != null)
					input[i] = left.output_border[left.factor2.length() + i];
				else
					// no need to check if top is not null
					// (because we assume this is not the root block)
					input[i] = top.output_border[i - lr];
			}
			else
			{
				if (top != null)
					input[i] = top.output_border[i - lr];
				else
					// there is no top block (see note for the left case)
					input[i] = Integer.MIN_VALUE / 2;
			}
		}

		return input;
	}

	/**
	 * Traverses a block to retrieve a part of an optimal alignment from the specified
	 * source in the output border to an entry in the input border.
	 *
	 * @param block the block to be traversed
	 * @param source the source of the path in the output border
	 * @param gapped_seq1 the StringBuffer to where the gapped sequence 1 is written to
	 * @param tag_line the StringBuffer to where the tag_line is written to
	 * @param gapped_seq2 the StringBuffer to where the gapped sequence 2 is written to
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected void traverseBlock (AlignmentBlock block, int source,
		StringBuffer gapped_seq1, StringBuffer tag_line, StringBuffer gapped_seq2)
		throws IncompatibleScoringSchemeException
	{
		char char1, char2;

		while (block.direction[source] != STOP_DIRECTION)
		{
			char1 = block.factor1.getNewChar();
			char2 = block.factor2.getNewChar();

			switch (block.direction[source])
			{
				case LEFT_DIRECTION:
					gapped_seq1.insert (0, GAP_CHARACTER);
					tag_line.insert (0, GAP_TAG);
					gapped_seq2.insert (0, char2);

					block = getLeftPrefix (block);
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
					gapped_seq2.insert (0, char2);

					block = getDiagonalPrefix (block);
					source --;
					break;

				case TOP_DIRECTION:
					gapped_seq1.insert (0, char1);
					tag_line.insert (0, GAP_TAG);
					gapped_seq2.insert (0, GAP_CHARACTER);

					block = getTopPrefix (block);
					source --;
					break;
			}
		}
	}

	/**
	 * This method is a shorthand to retrieve the left prefix of a block from the block
	 * table.
	 *
	 * @param block the block
	 * @return the block's left prefix
	 */
	protected AlignmentBlock getLeftPrefix (AlignmentBlock block)
	{
		int prefix_row = block.factor1.getSerialNumber();
		int prefix_col = block.factor2.getAncestorSerialNumber();

		return block_table[prefix_row][prefix_col];
	}

	/**
	 * This method is a shorthand to retrieve the diagonal prefix of a block from the
	 * block table.
	 *
	 * @param block the block
	 * @return the block's diagonal prefix
	 */
	protected AlignmentBlock getDiagonalPrefix (AlignmentBlock block)
	{
		int prefix_row = block.factor1.getAncestorSerialNumber();
		int prefix_col = block.factor2.getAncestorSerialNumber();

		return block_table[prefix_row][prefix_col];
	}

	/**
	 * This method is a shorthand to retrieve the top prefix of a block from the block
	 * table.
	 *
	 * @param block the block
	 * @return the block's top prefix
	 */
	protected AlignmentBlock getTopPrefix (AlignmentBlock block)
	{
		int prefix_row = block.factor1.getAncestorSerialNumber();
		int prefix_col = block.factor2.getSerialNumber();

		return block_table[prefix_row][prefix_col];
	}

	/**
	 * Computes the root block of the block table. See subclasses for actual
	 * implementation.
	 *
	 * @param factor1 the factor of the first sequence being aligned
	 * @param factor2 the factor of the second sequence being aligned
	 * @return the root block
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected abstract AlignmentBlock createRootBlock (Factor factor1, Factor factor2)
		throws IncompatibleScoringSchemeException;

	/**
	 * Computes a block at the first row (row zero) of the block table, which corresponds
	 * to an alignment block between one factor of sequence 2 and an empty string. See
	 * subclasses for actual implementation.
	 *
	 * @param factor1 the factor of the first sequence being aligned
	 * @param factor2 the factor of the second sequence being aligned
	 * @param col the column index of block in the block table
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected abstract AlignmentBlock createFirstRowBlock (Factor factor1, Factor factor2,
		int col) throws IncompatibleScoringSchemeException;

	/**
	 * Computes a block at the first column (column zero) of the block table, which
	 * corresponds to an alignment block between one factor of sequence 1 and an empty
	 * string. See subclasses for actual implementation.
	 *
	 * @param factor1 the factor of the first sequence being aligned
	 * @param factor2 the factor of the second sequence being aligned
	 * @param row the row index of block in the block table
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected abstract AlignmentBlock createFirstColumnBlock (Factor factor1,
		Factor factor2, int row) throws IncompatibleScoringSchemeException;

	/**
	 * Computes a block of the block table, which corresponds to an alignment block
	 * between one factor of sequence 1 and one factor of sequence 2. See subclasses for
	 * actual implementation.
	 *
	 * @param factor1 the factor of the first sequence being aligned
	 * @param factor2 the factor of the second sequence being aligned
	 * @param row the row index of block in the block table
	 * @param col the column index of block in the block table
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected abstract AlignmentBlock createBlock (Factor factor1, Factor factor2,
		int row, int col) throws IncompatibleScoringSchemeException;

	/**
	 * Retrieves an optimal alignment between the loaded sequences. See subclasses for
	 * actual implementation.
	 *
	 * @return the computed block
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected abstract PairwiseAlignment buildOptimalAlignment ()
		throws IncompatibleScoringSchemeException;

	/**
	 * Locates the score of the highest scoring alignment between the two sequences in the
	 * block table after is thas been computed. See subclasses for actual implementation.
	 *
	 * @return the score of the highest scoring alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected abstract int locateScore ();
}
