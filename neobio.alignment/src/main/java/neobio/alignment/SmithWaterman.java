/*
 * SmithWaterman.java
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
 * This class implement the classic local alignment algorithm (with linear gap penalty
 * function) due to T.F.Smith and M.S.Waterman (1981).
 *
 * <P>This algorithm is very similar to the {@linkplain NeedlemanWunsch} algorithm for
 * global alignment. The idea here also consists of building an (n+1 x m+1) matrix M given
 * two sequences A and B of sizes n and m, respectively. However, unlike in the global
 * alignment case, every position M[i,j] in the matrix contains the similarity score of
 * <B>suffixes</B> of A[1..i] and B[1..j].</P>
 *
 * <P>Starting from row 0, column 0, the {@link #computeMatrix computeMatrix} method
 * computes each position M[i,j] with the following recurrence:</P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 * M[0,0] = <B>M[0,j]</B> = <B>M[i,0]</B> = 0
 * M[i,j] = max { M[i,j-1]   + scoreInsertion (B[j]),
 *                M[i-1,j-1] + scoreSubstitution (A[i], B[j]),
 *                M[i-1,j]   + scoreDeletion(A[i])             }
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>Note that, here, all cells in the first row and column are set to zero. The best
 * local alignment score is the highest value found anywhere in the matrix.</P>
 *
 * <P>Just like in global alignment case, this algorithm has quadratic space complexity
 * because it needs to keep an (n+1 x m+1) matrix in memory. And since the work of
 * computing each cell is constant, it also has quadratic time complexity.</P>
 *
 * <P>After the matrix has been computed, the alignment can be retrieved by tracing a path
 * back in the matrix from the position of the highest score until a cell of value zero is
 * reached. This step is performed by the {@link #buildOptimalAlignment
 * buildOptimalAlignment} method, and its time complexity is linear on the size of the
 * alignment.
 *
 * <P>If the similarity value only is needed (and not the alignment itself), it is easy to
 * reduce the space requirement to O(n) by keeping just the last row or column in memory.
 * This is precisely what is done by the {@link #computeScore computeScore} method. Note
 * that it still requires O(n<SUP>2</SUP>) time.</P>
 *
 * <P>For a more efficient approach to the local alignment problem, see the
 * {@linkplain CrochemoreLandauZivUkelson} algorithm. For global alignment, see the
 * {@linkplain NeedlemanWunsch} algorithm.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see NeedlemanWunsch
 * @see CrochemoreLandauZivUkelson
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @see CrochemoreLandauZivUkelsonGlobalAlignment
 */
public class SmithWaterman extends PairwiseAlignmentAlgorithm
{
	/**
	 * The first sequence of an alignment.
	 */
	protected CharSequence seq1;

	/**
	 * The second sequence of an alignment.
	 */
	protected CharSequence seq2;

	/**
	 * The dynamic programming matrix. Each position (i, j) represents the best score
	 * between a suffic of the firsts i characters of <CODE>seq1</CODE> and a suffix of
	 * the first j characters of <CODE>seq2</CODE>.
	 */
	protected int[][] matrix;

	/**
	 * Indicate the row of where an optimal local alignment can be found in the matrix..
	 */
	protected int max_row;

	/**
	 * Indicate the column of where an optimal local alignment can be found in the matrix.
	 */
	protected int max_col;

	/**
	 * Loads sequences into {@linkplain CharSequence} instances. In case of any error, an
	 * exception is raised by the constructor of <CODE>CharSequence</CODE> (please check
	 * the specification of that class for specific requirements).
	 *
	 * @param input1 Input for first sequence
	 * @param input2 Input for second sequence
	 * @throws IOException If an I/O error occurs when reading the sequences
	 * @throws InvalidSequenceException If the sequences are not valid
	 * @see CharSequence
	 */
	protected void loadSequencesInternal (Reader input1, Reader input2)
		throws IOException, InvalidSequenceException
	{
		// load sequences into instances of CharSequence
		this.seq1 = new CharSequence(input1);
		this.seq2 = new CharSequence(input2);
	}

	/**
	 * Frees pointers to loaded sequences and the dynamic programming matrix so that their
	 * data can be garbage collected.
	 */
	protected void unloadSequencesInternal ()
	{
		this.seq1 = null;
		this.seq2 = null;
		this.matrix = null;
	}

	/**
	 * Builds an optimal local alignment between the loaded sequences after computing the
	 * dynamic programming matrix. It calls the <CODE>buildOptimalAlignment</CODE> method
	 * after the <CODE>computeMatrix</CODE> method computes the dynamic programming
	 * matrix.
	 *
	 * @return an optimal pairwise alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see #computeMatrix
	 * @see #buildOptimalAlignment
	 */
	protected PairwiseAlignment computePairwiseAlignment ()
		throws IncompatibleScoringSchemeException
	{
		// compute the matrix
		computeMatrix ();

		// build and return an optimal local alignment
		PairwiseAlignment alignment = buildOptimalAlignment ();

		// allow the matrix to be garbage collected
		matrix = null;

		return alignment;
	}

	/**
	 * Computes the dynamic programming matrix.
	 *
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected void computeMatrix () throws IncompatibleScoringSchemeException
	{
		int	r, c, rows, cols, ins, sub, del, max_score;

		rows = seq1.length()+1;
		cols = seq2.length()+1;

		matrix = new int [rows][cols];

		// initiate first row
		for (c = 0; c < cols; c++)
			matrix[0][c] = 0;

		// keep track of the maximum score
		this.max_row = this.max_col = max_score = 0;

		// calculates the similarity matrix (row-wise)
		for (r = 1; r < rows; r++)
		{
			// initiate first column
			matrix[r][0] = 0;

			for (c = 1; c < cols; c++)
			{
				ins = matrix[r][c-1] + scoreInsertion(seq2.charAt(c));
				sub = matrix[r-1][c-1] + scoreSubstitution(seq1.charAt(r),seq2.charAt(c));
				del = matrix[r-1][c] + scoreDeletion(seq1.charAt(r));

				// choose the greatest
				matrix[r][c] = max (ins, sub, del, 0);

				if (matrix[r][c] > max_score)
				{
					// keep track of the maximum score
					max_score = matrix[r][c];
					this.max_row = r; this.max_col = c;
				}
			}
		}
	}

	/**
	 * Builds an optimal local alignment between the loaded sequences.  Before it is
	 * executed, the dynamic programming matrix must already have been computed by
	 * the <CODE>computeMatrix</CODE> method.
	 *
	 * @return an optimal local alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see #computeMatrix
	 */
	protected PairwiseAlignment buildOptimalAlignment () throws
		IncompatibleScoringSchemeException
	{
		StringBuffer gapped_seq1, score_tag_line, gapped_seq2;
		int			 r, c, max_score, sub;

		// start at the cell with maximum score
		r = this.max_row;
		c = this.max_col;

		max_score = matrix[r][c];

		gapped_seq1		= new StringBuffer();
		score_tag_line	= new StringBuffer();
		gapped_seq2		= new StringBuffer();

		while ((r > 0 || c > 0) && (matrix[r][c] > 0))
		{
			if (c > 0)
				if (matrix[r][c] == matrix[r][c-1] + scoreInsertion(seq2.charAt(c)))
				{
					// insertion
					gapped_seq1.insert (0, GAP_CHARACTER);
					score_tag_line.insert (0, GAP_TAG);
					gapped_seq2.insert (0, seq2.charAt(c));

					c = c - 1;

					// skip to the next iteration
					continue;
				}

			if ((r > 0) && (c > 0))
			{
				sub = scoreSubstitution(seq1.charAt(r), seq2.charAt(c));

				if (matrix[r][c] == matrix[r-1][c-1] + sub)
				{
					// substitution
					gapped_seq1.insert (0, seq1.charAt(r));
					if (seq1.charAt(r) == seq2.charAt(c))
						if (useMatchTag())
							score_tag_line.insert (0, MATCH_TAG);
						else
							score_tag_line.insert (0, seq1.charAt(r));
					else if (sub > 0)
						score_tag_line.insert (0, APPROXIMATE_MATCH_TAG);
					else
						score_tag_line.insert (0, MISMATCH_TAG);
					gapped_seq2.insert (0, seq2.charAt(c));

					r = r - 1; c = c - 1;

					// skip to the next iteration
					continue;
				}
			}

			// must be a deletion
			gapped_seq1.insert (0, seq1.charAt(r));
			score_tag_line.insert (0, GAP_TAG);
			gapped_seq2.insert  (0,GAP_CHARACTER);

			r = r - 1;
		}

		return new PairwiseAlignment (gapped_seq1.toString(), score_tag_line.toString(),
										gapped_seq2.toString(), max_score);
	}

	/**
	 * Computes the score of the best local alignment between the two sequences using the
	 * scoring scheme previously set. This method calculates the similarity value only
	 * (doesn't build the whole matrix so the alignment cannot be recovered, however it
	 * has the advantage of requiring O(n) space only).
	 *
	 * @return the score of the best local alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected int computeScore () throws IncompatibleScoringSchemeException
	{
		int[]	array;
		int 	rows = seq1.length()+1, cols = seq2.length()+1;
		int 	r, c, tmp, ins, del, sub, max_score;

		// keep track of the maximum score
		max_score = 0;

		if (rows <= cols)
		{
			// goes columnwise
			array = new int [rows];

			// initiate first column
			for (r = 0; r < rows; r++)
				array[r] = 0;

			// calculate the similarity matrix (keep current column only)
			for (c = 1; c < cols; c++)
			{
				// set first position to zero (tmp hold values
				// that will be later moved to the array)
				tmp = 0;

				for (r = 1; r < rows; r++)
				{
					ins = array[r] + scoreInsertion(seq2.charAt(c));
					sub = array[r-1] + scoreSubstitution(seq1.charAt(r), seq2.charAt(c));
					del = tmp + scoreDeletion(seq1.charAt(r));

					// move the temp value to the array
					array[r-1] = tmp;

					// choose the greatest (or zero if all negative)
					tmp = max (ins, sub, del, 0);

					// keep track of the maximum score
					if (tmp > max_score) max_score = tmp;
				}

				// move the temp value to the array
				array[rows - 1] = tmp;
			}
		}
		else
		{
			// goes rowwise
			array = new int [cols];

			// initiate first row
			for (c = 0; c < cols; c++)
				array[c] = 0;

			// calculate the similarity matrix (keep current row only)
			for (r = 1; r < rows; r++)
			{
				// set first position to zero (tmp hold values
				// that will be later moved to the array)
				tmp = 0;

				for (c = 1; c < cols; c++)
				{
					ins = tmp + scoreInsertion(seq2.charAt(c));
					sub = array[c-1] + scoreSubstitution(seq1.charAt(r), seq2.charAt(c));
					del = array[c] + scoreDeletion(seq1.charAt(r));

					// move the temp value to the array
					array[c-1] = tmp;

					// choose the greatest (or zero if all negative)
					tmp = max (ins, sub, del, 0);

					// keep track of the maximum score
					if (tmp > max_score) max_score = tmp;
				}

				// move the temp value to the array
				array[cols - 1] = tmp;
			}
		}

		return max_score;
	}
}
