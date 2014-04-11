package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PairwiseAlignment<T> {
	/**
	 * First gapped sequence.
	 * 
	 * @serial
	 */
	protected List<T> gapped_seq1;

	protected List<T> originalSeq1;

	/**
	 * The score tag line.
	 * 
	 * @serial
	 */
	protected List<IndividualAlignment> score_tag_line;

	/**
	 * Second gapped sequence.
	 * 
	 * @serial
	 */
	protected List<T> gapped_seq2;

	protected List<T> originalSeq2;

	protected Map<Integer, Integer> indexMap1 = null;

	protected Map<Integer, Integer> indexMap2 = null;

	int seq1_start;
	int seq2_start;

	int seq1_end;
	int seq2_end;

	/**
	 * The overall score value for this alignment.
	 * 
	 * @serial
	 */
	protected Number score;

	/**
	 * Creates a <CODE>PairwiseAlignment</CODE> instance with the specified
	 * gapped sequences, score tag line and score value.
	 * 
	 * @param gapped_seq1
	 *            the first gapped sequence
	 * @param score_tag_line
	 *            the score tag line
	 * @param gapped_seq2
	 *            the second gapped sequence
	 * @param score
	 *            the overall score value for this alignment
	 */
	protected PairwiseAlignment(final List<T> gapped_seq1,
			final List<IndividualAlignment> score_tag_line,
			final List<T> gapped_seq2, final Number score) {
		this.gapped_seq1 = gapped_seq1;
		this.score_tag_line = score_tag_line;
		this.gapped_seq2 = gapped_seq2;
		this.score = score;
	}

	/**
	 * Returns the first gapped sequence.
	 * 
	 * @return first gapped sequence
	 */
	public List<T> getGappedSequence1() {
		return gapped_seq1;
	}

	/**
	 * Returns the score tag line.
	 * 
	 * @return score tag line
	 */
	public List<IndividualAlignment> getScoreTagLine() {
		return score_tag_line;
	}

	/**
	 * Returns the second gapped sequence.
	 * 
	 * @return second gapped sequence
	 */
	public List<T> getGappedSequence2() {
		return gapped_seq2;
	}

	/**
	 * Returns the score for this alignment.
	 * 
	 * @return overall score for this alignment
	 */
	public Number getScore() {
		return score;
	}

	/**
	 * Returns a four-line String representation of this alignment in the
	 * following order: first gapped sequence, score tag line, second gapped
	 * sequence and the score value.
	 * 
	 * @return a String representation of this scoring matrix
	 */
	@Override
	public String toString() {
		return "Gapped sequence 1: " + gapped_seq1 + "\nScore tag line: "
				+ score_tag_line + "\nGapped sequence 2: " + gapped_seq2
				+ "\nScore: " + score;
	}

	/**
	 * Compares this object to the specified object. The result is
	 * <CODE>true</CODE> if and only if the argument is not <CODE>null</CODE>
	 * and is an <CODE>PairwiseAlignment</CODE> object that contains the same
	 * values as this object, i.e. the same gapped sequences, the same score tag
	 * line and the same score.
	 * 
	 * @param obj
	 *            the object to compare with
	 * @return <CODE>true</CODE> if objects are the same, <CODE>false</CODE>
	 *         otherwise
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof PairwiseAlignment))
			return false;

		@SuppressWarnings("unchecked")
		PairwiseAlignment<T> another_pa = (PairwiseAlignment<T>) obj;

		if (this.score != another_pa.score)
			return false;

		if (!this.gapped_seq1.equals(another_pa.gapped_seq1))
			return false;

		if (!this.score_tag_line.equals(another_pa.score_tag_line))
			return false;

		if (!this.gapped_seq2.equals(another_pa.gapped_seq2))
			return false;

		return true;
	}

	/**
	 * @return the originalSeq1
	 */
	public List<T> getOriginalSeq1() {
		return originalSeq1;
	}

	/**
	 * @return the originalSeq2
	 */
	public List<T> getOriginalSeq2() {
		return originalSeq2;
	}

	/**
	 * @param originalSeq1
	 *            the originalSeq1 to set
	 */
	protected void setOriginalSeq1(final List<T> originalSeq1) {
		this.originalSeq1 = originalSeq1;
	}

	/**
	 * @param originalSeq2
	 *            the originalSeq2 to set
	 */
	protected void setOriginalSeq2(final List<T> originalSeq2) {
		this.originalSeq2 = originalSeq2;
	}

	/**
	 * Returns an index mapping based on the original sequences
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getIndexMap1() {
		if (this.indexMap1 == null) {
			this.indexMap1 = new HashMap<Integer, Integer>();

			int okey1 = 1;
			int okey2 = 1;
			for (int i = 1; i < this.getGappedSequence1().size() + 1; i++) {
				if (this.getGappedSequence1().get(i) == null) {
					okey1--;
				} else {
					if (this.getGappedSequence2().get(i) == null) {
						okey2--;
					} else {
						indexMap1.put(okey1 - 1, okey2 - 1);
					}
				}
				okey1++;
				okey2++;
			}
		}
		return this.indexMap1;
	}

	public Map<Integer, Integer> getIndexMap2() {
		if (this.indexMap2 == null) {
			this.indexMap2 = new HashMap<Integer, Integer>();

			int okey1 = 1;
			int okey2 = 1;
			for (int i = 1; i < this.getGappedSequence2().size() + 1; i++) {
				if (this.getGappedSequence2().get(i) == null) {
					okey1--;
				} else {
					if (this.getGappedSequence1().get(i) == null) {
						okey2--;
					} else {
						indexMap2.put(okey1 - 1, okey2 - 1);
					}
				}
				okey1++;
				okey2++;
			}
		}
		return this.indexMap2;
	}

	public String getVerticalAlignmentTable(final int cellwidth) {
		StringBuilder b = new StringBuilder();
		String formatString = "%1$" + cellwidth + "." + cellwidth
				+ "s %2$-4.4s %3$-" + cellwidth + "." + cellwidth + "s\n";
		Formatter formatter = new Formatter(b, Locale.US);
		formatter.format(formatString, "Sequence 1", "  ", "Sequence 2");
		for (int i = 0; i < score_tag_line.size(); i++) {
			formatter.format(formatString, this.gapped_seq1.get(i + 1),
					this.score_tag_line.get(i), this.gapped_seq2.get(i + 1));
		}
		formatter.close();
		return b.toString();
	}

	public String getAlignmentTable(final int cellwidth) {
		StringBuilder b = new StringBuilder();
		Formatter formatter = new Formatter(b, Locale.US);

		// Gapped sequence 1
		for (T element : gapped_seq1) {
			formatter.format("%1$-" + cellwidth + "." + cellwidth + "s ",
					element);
		}
		b.append('\n');

		// Gapped sequence 1
		for (IndividualAlignment element : score_tag_line) {
			formatter.format("%1$-" + cellwidth + "." + cellwidth + "s ",
					element);
		}
		b.append('\n');

		// Gapped sequence 2
		for (T element : gapped_seq2) {
			formatter.format("%1$-" + cellwidth + "." + cellwidth + "s ",
					element);
		}
		b.append('\n');
		b.append("Score: ").append(score);
		formatter.close();

		return b.toString();
	}
}
