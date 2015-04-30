/*
 * NeedlemanWunsch.java
 * 
 * Copyright 2003 Sergio Anibal de Carvalho Junior
 * 
 * This file is part of NeoBio.
 * 
 * NeoBio is free software; you can redistribute it and/or modify it under the
 * terms of
 * the GNU General Public License as published by the Free Software Foundation;
 * either
 * version 2 of the License, or (at your option) any later version.
 * 
 * NeoBio is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * NeoBio;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite
 * 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Proper attribution of the author as the source of the software would be
 * appreciated.
 * 
 * Sergio Anibal de Carvalho Junior mailto:sergioanibaljr@users.sourceforge.net
 * Department of Computer Science http://www.dcs.kcl.ac.uk
 * King's College London, UK http://www.kcl.ac.uk
 * 
 * Please visit http://neobio.sourceforge.net
 * 
 * This project was supervised by Professor Maxime Crochemore.
 */

package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import neobio.alignment.CrochemoreLandauZivUkelson;
import neobio.alignment.CrochemoreLandauZivUkelsonGlobalAlignment;
import neobio.alignment.CrochemoreLandauZivUkelsonLocalAlignment;
import neobio.alignment.IncompatibleScoringSchemeException;
import de.nilsreiter.alignment.neobio.AlignmentType;
import de.nilsreiter.alignment.neobio.IndividualAlignment;
import de.nilsreiter.alignment.neobio.PairwiseAlignment;
import de.nilsreiter.alignment.neobio.PairwiseAlignmentAlgorithm;
import de.nilsreiter.alignment.neobio.SmithWaterman;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * This class implements the classic global alignment algorithm (with linear gap
 * penalty function) due to S.B.Needleman and C.D.Wunsch (1970).
 * 
 * <P>
 * It is based on a dynamic programming approach. The idea consists of, given
 * two sequences A and B of sizes n and m, respectively, building an (n+1 x m+1)
 * matrix M that contains the similarity of prefixes of A and B. Every position
 * M[i,j] in the matrix holds the score between the subsequences A[1..i] and
 * B[1..j]. The first row and column represent alignments with spaces.
 * </P>
 * 
 * <P>
 * Starting from row 0, column 0, the algorithm computes each position M[i,j]
 * with the following recurrence:
 * </P>
 * 
 * <CODE><BLOCKQUOTE><PRE>
 * M[0,0] = 0
 * M[i,j] = max { M[i,j-1]   + scoreInsertion (B[j]),
 *                M[i-1,j-1] + scoreSubstitution (A[i], B[j]),
 *                M[i-1,j]   + scoreDeletion(A[i])             }
 * </PRE></BLOCKQUOTE></CODE>
 * 
 * <P>
 * In the end, the value at the last position (last row, last column) will
 * contain the similarity between the two sequences. This part of the algorithm
 * is accomplished by the {@link #computeMatrix computeMatrix} method. It has
 * quadratic space complexity since it needs to keep an (n+1 x m+1) matrix in
 * memory. And since the work of computing each cell is constant, it also has
 * quadratic time complexity.
 * </P>
 * 
 * <P>
 * After the matrix has been computed, the alignment can be retrieved by tracing
 * a path back in the matrix from the last position to the first. This step is
 * performed by the {@link #buildOptimalAlignment buildOptimalAlignment} method,
 * and since the path can be roughly as long as (m + n), this method has O(n)
 * time complexity.
 * </P>
 * 
 * <P>
 * If the similarity value only is needed (and not the alignment itself), it is
 * easy to reduce the space requirement to O(n) by keeping just the last row or
 * column in memory. This is precisely what is done by the {@link #computeScore
 * computeScore} method. Note that it still requires O(n<SUP>2</SUP>) time.
 * </P>
 * 
 * <P>
 * For a more efficient approach to the global alignment problem, see the
 * {@linkplain CrochemoreLandauZivUkelson} algorithm. For local alignment, see
 * the {@linkplain SmithWaterman} algorithm.
 * </P>
 * 
 * @author Sergio A. de Carvalho Jr.
 * @see SmithWaterman
 * @see CrochemoreLandauZivUkelson
 * @see CrochemoreLandauZivUkelsonLocalAlignment
 * @see CrochemoreLandauZivUkelsonGlobalAlignment
 */
public class DoubleNeedlemanWunsch<T> extends PairwiseAlignmentAlgorithm<T> {
	/**
	 * The dynamic programming matrix. Each position (i, j) represents the best
	 * score between the firsts i characters of <CODE>seq1</CODE> and j
	 * characters of <CODE>seq2</CODE>.
	 */
	protected double[][] matrix;

	/**
	 * Frees pointers to loaded sequences and the dynamic programming matrix so
	 * that their data can be garbage collected.
	 */
	protected void unloadSequencesInternal() {
		this.seq1 = null;
		this.seq2 = null;
		this.matrix = null;
	}

	public DoubleNeedlemanWunsch(final AdvancedScoringScheme<T> scoring) {
		this.setScoring(scoring);
	}

	/**
	 * Builds an optimal global alignment between the loaded sequences after
	 * computing the dynamic programming matrix. It calls the
	 * <CODE>buildOptimalAlignment</CODE> method after the
	 * <CODE>computeMatrix</CODE> method computes the dynamic programming
	 * matrix.
	 * 
	 * @return an optimal global alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException
	 *             If the scoring scheme is not compatible with the loaded
	 *             sequences.
	 * @see #computeMatrix
	 * @see #buildOptimalAlignment
	 */
	@Override
	public PairwiseAlignment<T> computePairwiseAlignment()
			throws IncompatibleScoringSchemeException {
		// compute the matrix
		computeMatrix();

		// build and return an optimal global alignment
		PairwiseAlignment<T> alignment = buildOptimalAlignment();

		// allow the matrix to be garbage collected
		matrix = null;

		alignment.setOriginalSeq1(seq1);
		alignment.setOriginalSeq2(seq2);

		return alignment;
	}

	/**
	 * Computes the dynamic programming matrix.
	 * 
	 * @throws IncompatibleScoringSchemeException
	 *             If the scoring scheme is not compatible with the loaded
	 *             sequences.
	 */
	protected void computeMatrix() throws IncompatibleScoringSchemeException {
		int r, c, rows, cols;
		double ins, del, sub;

		rows = seq1.size() + 1;
		cols = seq2.size() + 1;

		matrix = new double[rows][cols];

		// initiate first row
		matrix[0][0] = 0;
		for (c = 1; c < cols; c++)
			matrix[0][c] = matrix[0][c - 1] + scoreInsertion(seq2.get(c - 1));

		// calculates the similarity matrix (row-wise)
		for (r = 1; r < rows; r++) {
			// initiate first column
			matrix[r][0] = matrix[r - 1][0] + scoreDeletion(seq1.get(r - 1));

			for (c = 1; c < cols; c++) {
				ins = matrix[r][c - 1] + scoreInsertion(seq2.get(c - 1));
				sub =
						matrix[r - 1][c - 1]
								+ scoreSubstitution(seq1.get(r - 1),
										seq2.get(c - 1));
				del = matrix[r - 1][c] + scoreDeletion(seq1.get(r - 1));

				// choose the greatest
				matrix[r][c] = max(ins, sub, del);
			}
		}
	}

	/**
	 * Builds an optimal global alignment between the loaded sequences. Before
	 * it is executed, the dynamic programming matrix must already have been
	 * computed by the <CODE>computeMatrix</CODE> method.
	 * 
	 * @return an optimal global alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException
	 *             If the scoring scheme is not compatible with the loaded
	 *             sequences.
	 * @see #computeMatrix
	 */
	protected PairwiseAlignment<T> buildOptimalAlignment()
			throws IncompatibleScoringSchemeException {
		List<T> gapped_seq1, gapped_seq2;
		List<IndividualAlignment> score_tag_line;
		int r, c;
		double max_score, sub;

		gapped_seq1 = new LinkedList<T>();
		score_tag_line = new ArrayList<IndividualAlignment>();
		gapped_seq2 = new LinkedList<T>();

		// start at the last row, last column
		r = matrix.length - 1;
		c = matrix[r].length - 1;
		max_score = matrix[r][c];

		while ((r > 0) || (c > 0)) {
			if (c > 0)
				if (matrix[r][c] == matrix[r][c - 1]
						+ scoreInsertion(seq2.get(c - 1))) {
					// insertion was used
					gapped_seq1.add(0, null);
					score_tag_line.add(0, new IndividualAlignment(
							AlignmentType.Gap, 0.0));
					gapped_seq2.add(0, seq2.get(c - 1));
					c = c - 1;

					// skip to the next iteration
					continue;
				}

			if ((r > 0) && (c > 0)) {
				sub = scoreSubstitution(seq1.get(r - 1), seq2.get(c - 1));
				Probability p =
						getAdvancedScoring().sim(seq1.get(r - 1),
								seq2.get(c - 1));

				if (matrix[r][c] == matrix[r - 1][c - 1] + sub) {
					// substitution was used
					gapped_seq1.add(0, seq1.get(r - 1));
					if (seq1.get(r - 1).equals(seq2.get(c - 1)))
						score_tag_line.add(0, new IndividualAlignment(
								AlignmentType.Full, 0.0));
					else if (sub > 0)
						score_tag_line.add(0, new IndividualAlignment(
								AlignmentType.Partial, p.getProbability()));
					else
						score_tag_line.add(0, new IndividualAlignment(
								AlignmentType.None, 1.0));
					gapped_seq2.add(0, seq2.get(c - 1));
					r = r - 1;
					c = c - 1;

					// skip to the next iteration
					continue;
				}
			}

			// must be a deletion
			gapped_seq1.add(0, seq1.get(r - 1));
			score_tag_line.add(0, new IndividualAlignment(AlignmentType.Gap,
					0.0));
			gapped_seq2.add(0, null);
			r = r - 1;
		}

		return new PairwiseAlignment<T>(gapped_seq1, score_tag_line,
				gapped_seq2, max_score);
	}

	/**
	 * Computes the score of the best global alignment between the two sequences
	 * using the scoring scheme previously set. This method calculates the
	 * similarity value only (doesn't build the whole matrix so the alignment
	 * cannot be recovered, however it has the advantage of requiring O(n) space
	 * only).
	 * 
	 * @return score of the best global alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException
	 *             If the scoring scheme is not compatible with the loaded
	 *             sequences.
	 */
	protected double computeScore() throws IncompatibleScoringSchemeException {
		double[] array;
		int r, c, rows, cols;
		double tmp, ins, del, sub;

		rows = seq1.size() + 1;
		cols = seq2.size() + 1;

		if (rows <= cols) {
			// goes columnwise
			array = new double[rows];

			// initiate first column
			array[0] = 0;
			for (r = 1; r < rows; r++)
				array[r] = array[r - 1] + scoreDeletion(seq1.get(r));

			// calculate the similarity matrix (keep current column only)
			for (c = 1; c < cols; c++) {
				// initiate first row (tmp hold values
				// that will be later moved to the array)
				tmp = array[0] + scoreInsertion(seq2.get(c));

				for (r = 1; r < rows; r++) {
					ins = array[r] + scoreInsertion(seq2.get(c));
					sub =
							array[r - 1]
									+ scoreSubstitution(seq1.get(r),
											seq2.get(c));
					del = tmp + scoreDeletion(seq1.get(r));

					// move the temp value to the array
					array[r - 1] = tmp;

					// choose the greatest
					tmp = max(ins, sub, del);
				}

				// move the temp value to the array
				array[rows - 1] = tmp;
			}

			return array[rows - 1];
		} else {
			// goes rowwise
			array = new double[cols];

			// initiate first row
			array[0] = 0;
			for (c = 1; c < cols; c++)
				array[c] = array[c - 1] + scoreInsertion(seq2.get(c));

			// calculate the similarity matrix (keep current row only)
			for (r = 1; r < rows; r++) {
				// initiate first column (tmp hold values
				// that will be later moved to the array)
				tmp = array[0] + scoreDeletion(seq1.get(r));

				for (c = 1; c < cols; c++) {
					ins = tmp + scoreInsertion(seq2.get(c));
					sub =
							array[c - 1]
									+ scoreSubstitution(seq1.get(r),
											seq2.get(c));
					del = array[c] + scoreDeletion(seq1.get(r));

					// move the temp value to the array
					array[c - 1] = tmp;

					// choose the greatest
					tmp = max(ins, sub, del);
				}

				// move the temp value to the array
				array[cols - 1] = tmp;
			}

			return array[cols - 1];
		}
	}

	private AdvancedScoringScheme<T> getAdvancedScoring() {
		return (AdvancedScoringScheme<T>) this.getScoring();
	}
}
