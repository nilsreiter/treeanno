package de.nilsreiter.alignment.neobio;

import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import neobio.alignment.IncompatibleScoringSchemeException;

public class SmithWaterman<T> extends PairwiseAlignmentAlgorithm<T> {

	/**
	 * The dynamic programming matrix. Each position (i, j) represents the best
	 * score between a suffic of the firsts i characters of <CODE>seq1</CODE>
	 * and a suffix of the first j characters of <CODE>seq2</CODE>.
	 */
	protected int[][] matrix;

	/**
	 * Indicate the row of where an optimal local alignment can be found in the
	 * matrix..
	 */
	protected int max_row;

	/**
	 * Indicate the column of where an optimal local alignment can be found in
	 * the matrix.
	 */
	protected int max_col;

	/**
	 * Frees pointers to loaded sequences and the dynamic programming matrix so
	 * that their data can be garbage collected.
	 */
	protected void unloadSequencesInternal() {
		this.seq1 = null;
		this.seq2 = null;
		this.matrix = null;
	}

	/**
	 * Builds an optimal local alignment between the loaded sequences after
	 * computing the dynamic programming matrix. It calls the
	 * <CODE>buildOptimalAlignment</CODE> method after the
	 * <CODE>computeMatrix</CODE> method computes the dynamic programming
	 * matrix.
	 * 
	 * @return an optimal pairwise alignment between the loaded sequences
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
		// build and return an optimal local alignment
		PairwiseAlignment<T> alignment = buildOptimalAlignment();

		alignment.setOriginalSeq1(seq1);
		alignment.setOriginalSeq2(seq2);

		// allow the matrix to be garbage collected
		matrix = null;

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
		int r, c, rows, cols, ins, sub, del, max_score;

		rows = seq1.size() + 1;
		cols = seq2.size() + 1;

		matrix = new int[rows][cols];

		// initiate first row
		for (c = 0; c < cols; c++)
			matrix[0][c] = 0;

		// keep track of the maximum score
		this.max_row = this.max_col = max_score = 0;

		// calculates the similarity matrix (row-wise)
		for (r = 1; r < rows; r++) {
			// initiate first column
			matrix[r][0] = 0;

			for (c = 1; c < cols; c++) {
				ins = (matrix[r][c - 1] + (int) scoreInsertion(seq2.get(c)));
				sub = (matrix[r - 1][c - 1] + (int) scoreSubstitution(
						seq1.get(r), seq2.get(c)));
				del = matrix[r - 1][c] + (int) scoreDeletion(seq1.get(r));

				// choose the greatest
				matrix[r][c] = max(ins, sub, del, 0);

				if (matrix[r][c] > max_score) {
					// keep track of the maximum score
					max_score = matrix[r][c];
					this.max_row = r;
					this.max_col = c;
				}
			}
		}
	}

	public String getMatrixString() {
		StringBuffer buf = new StringBuffer();
		int rows = seq1.size() + 1;
		int cols = seq2.size() + 1;
		Formatter formatter = new Formatter(buf, Locale.US);
		for (int r = 1; r < rows; r++) {
			formatter.format("%1$10s", seq1.get(r));
			buf.append(' ');
			for (int c = 1; c < cols; c++) {
				buf.append(matrix[r][c]);
				buf.append(' ');
			}
			buf.append('\n');
		}
		formatter.close();
		return buf.toString();
	}

	/**
	 * Builds an optimal local alignment between the loaded sequences. Before it
	 * is executed, the dynamic programming matrix must already have been
	 * computed by the <CODE>computeMatrix</CODE> method.
	 * 
	 * @return an optimal local alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException
	 *             If the scoring scheme is not compatible with the loaded
	 *             sequences.
	 * @see #computeMatrix
	 */
	protected PairwiseAlignment<T> buildOptimalAlignment()
			throws IncompatibleScoringSchemeException {
		List<T> gapped_seq1, gapped_seq2;
		List<IndividualAlignment> score_tag_line;
		int r, c, max_score, sub;

		// start at the cell with maximum score
		r = this.max_row;
		c = this.max_col;

		max_score = matrix[r][c];

		gapped_seq1 = new LinkedList<T>();
		score_tag_line = new LinkedList<IndividualAlignment>();
		gapped_seq2 = new LinkedList<T>();

		while ((r > 0 || c > 0) && (matrix[r][c] > 0)) {
			if (c > 0)
				if (matrix[r][c] == matrix[r][c - 1]
						+ scoreInsertion(seq2.get(c))) {
					// insertion
					gapped_seq1.add(0, gap);
					score_tag_line.add(0, new IndividualAlignment(
							AlignmentType.Gap));
					gapped_seq2.add(0, seq2.get(c));

					c = c - 1;

					// skip to the next iteration
					continue;
				}

			if ((r > 0) && (c > 0)) {
				sub = (int) scoreSubstitution(seq1.get(r), seq2.get(c));

				if (matrix[r][c] == matrix[r - 1][c - 1] + sub) {
					// substitution
					gapped_seq1.add(0, seq1.get(r));
					if (seq1.get(r).equals(seq2.get(c)))
						score_tag_line.add(0, new IndividualAlignment(
								AlignmentType.Full));
					else if (sub > 0)
						score_tag_line.add(0, new IndividualAlignment(
								AlignmentType.Partial, sub));
					else
						score_tag_line.add(0, new IndividualAlignment(
								AlignmentType.None));
					gapped_seq2.add(0, seq2.get(c));

					r = r - 1;
					c = c - 1;

					// skip to the next iteration
					continue;
				}
			}

			// must be a deletion
			gapped_seq1.add(0, seq1.get(r));
			score_tag_line.add(0, new IndividualAlignment(AlignmentType.Gap));
			gapped_seq2.add(0, this.getGap());

			r = r - 1;
		}

		PairwiseAlignment<T> pw = new PairwiseAlignment<T>(gapped_seq1,
				score_tag_line, gapped_seq2, max_score);
		pw.seq1_end = this.max_row;
		pw.seq2_end = this.max_col;
		pw.seq1_start = r;
		pw.seq2_start = c;
		return pw;
	}

	/**
	 * Computes the score of the best local alignment between the two sequences
	 * using the scoring scheme previously set. This method calculates the
	 * similarity value only (doesn't build the whole matrix so the alignment
	 * cannot be recovered, however it has the advantage of requiring O(n) space
	 * only).
	 * 
	 * @return the score of the best local alignment between the loaded
	 *         sequences
	 * @throws IncompatibleScoringSchemeException
	 *             If the scoring scheme is not compatible with the loaded
	 *             sequences.
	 */
	protected int computeScore() throws IncompatibleScoringSchemeException {
		int[] array;
		int rows = seq1.size() + 1, cols = seq2.size() + 1;
		int r, c, tmp, ins, del, sub, max_score;

		// keep track of the maximum score
		max_score = 0;

		if (rows <= cols) {
			// goes columnwise
			array = new int[rows];

			// initiate first column
			for (r = 0; r < rows; r++)
				array[r] = 0;

			// calculate the similarity matrix (keep current column only)
			for (c = 1; c < cols; c++) {
				// set first position to zero (tmp hold values
				// that will be later moved to the array)
				tmp = 0;

				for (r = 1; r < rows; r++) {
					ins = array[r] + (int) scoreInsertion(seq2.get(c));
					sub = array[r - 1]
							+ (int) scoreSubstitution(seq1.get(r), seq2.get(c));
					del = tmp + (int) scoreDeletion(seq1.get(r));

					// move the temp value to the array
					array[r - 1] = tmp;

					// choose the greatest (or zero if all negative)
					tmp = max(ins, sub, del, 0);

					// keep track of the maximum score
					if (tmp > max_score)
						max_score = tmp;
				}

				// move the temp value to the array
				array[rows - 1] = tmp;
			}
		} else {
			// goes rowwise
			array = new int[cols];

			// initiate first row
			for (c = 0; c < cols; c++)
				array[c] = 0;

			// calculate the similarity matrix (keep current row only)
			for (r = 1; r < rows; r++) {
				// set first position to zero (tmp hold values
				// that will be later moved to the array)
				tmp = 0;

				for (c = 1; c < cols; c++) {
					ins = tmp + (int) scoreInsertion(seq2.get(c));
					sub = array[c - 1]
							+ (int) scoreSubstitution(seq1.get(r), seq2.get(c));
					del = array[c] + (int) scoreDeletion(seq1.get(r));

					// move the temp value to the array
					array[c - 1] = tmp;

					// choose the greatest (or zero if all negative)
					tmp = max(ins, sub, del, 0);

					// keep track of the maximum score
					if (tmp > max_score)
						max_score = tmp;
				}

				// move the temp value to the array
				array[cols - 1] = tmp;
			}
		}

		return max_score;
	}

}
