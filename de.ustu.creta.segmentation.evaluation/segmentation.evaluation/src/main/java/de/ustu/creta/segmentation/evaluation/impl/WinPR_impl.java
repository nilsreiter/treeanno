package de.ustu.creta.segmentation.evaluation.impl;

import java.util.LinkedList;

class WinPR_impl {
	int window_size = 1;

	double tp = 0.0;
	double fp = 0.0;
	double fn = 0.0;
	double tn = 0.0;

	/*
	 * This is a window diff like measure, though it differ that it counts
	 * boundaries instead of windows and that it has the start and end position
	 * correction included
	 */
	public double winDiffPrime() {
		return (fp + fn) / (tp + fp + fn + (tn / window_size / 2));
	}

	public double precision() {
		return (tp) / (tp + fp);
	}

	public double recall() {
		return (tp) / (tp + fn);
	}

	/**
	 * This method will produce the WinPR confusion matrix for a reference and
	 * computed segmentation. Please pay close attention to the segmentation
	 * format.
	 * 
	 * Segmentations are represented as list of integers. Integers in the list
	 * represent boundary positions. An integer of 0 is interpreted as a
	 * boundary between the first(0) and second(1) content units, or as a
	 * boundary in the first possible boundary position, but NOT as before
	 * content.
	 * 
	 * The list should ALWAYS contain at least one element. The LAST element in
	 * the list represents the length segmentation. If there are 101 content
	 * units, the last element should be 100 (zero based index thus content
	 * units are numbered from 0-100); don't forget 101 content units means 100
	 * possible boundary positions.
	 * 
	 * @param gold
	 *            - this represents the gold standard or reference segmentation
	 *            - as described above
	 * @param hypothesis
	 *            - this represents the hypothesis or computed segmentation to
	 *            be evaluated- as described above
	 * @return a WinPR object representing the resulting confusion matrix
	 */
	static public WinPR_impl calculateWinPR(LinkedList<Integer> gold,
			LinkedList<Integer> hypothesis) {
		assert (gold.getLast() == hypothesis.getLast());
		int window_size = windowSize(gold);
		return calculateWinPR(gold, hypothesis, window_size);
	}

	static public WinPR_impl calculateWinPR(LinkedList<Integer> gold,
			LinkedList<Integer> hypothesis, int window_size) {
		assert (gold.getLast() == hypothesis.getLast());

		WinPR_impl winDiff = new WinPR_impl();
		winDiff.window_size = window_size = Math.max(1, window_size);

		int start = 1 - window_size;
		int last = gold.getLast();
		int end = last + window_size;

		winDiff.tn = -window_size * (window_size - 1);

		for (int i = start; i + window_size < end; i++) {
			int gold_count = 0;
			for (Integer s : gold) {
				if (s < last) // the end last boundary is not actually a
								// boundary but represents the end of the data
								// (nothing exist after it, including it self)
				{
					if (s >= i && s < i + window_size) {
						gold_count++;
					}
				}
			}

			int hypo_count = 0;
			for (Integer s : hypothesis) {
				if (s < last) // the end last boundary is not actually a
								// boundary but represents the end of the data
								// (nothing exist after it, including it self)
				{
					if (s >= i && s < i + window_size) {
						hypo_count++;
					}
				}
			}

			updateWinPR(gold_count, hypo_count, window_size, winDiff);
		}

		return winDiff;
	}

	static private void updateWinPR(int gold_count, int hypo_count,
			int window_size, WinPR_impl winDiff) {
		winDiff.tp += Math.min(gold_count, hypo_count);
		winDiff.tn +=
				Math.max(0, window_size - Math.max(gold_count, hypo_count));

		if (hypo_count - gold_count > 0)
			winDiff.fp += hypo_count - gold_count;
		else
			winDiff.fn += gold_count - hypo_count;
	}

	static public int windowSize(LinkedList<Integer> gold) {
		int last = gold.getLast();
		int window_size = (int) (last / (2.0 * gold.size()));
		return Math.max(1, window_size);
	}

	/**
	 * 
	 * This method to calculate WindowDiff is provided untested for assistance
	 * 
	 * Segmentation format is the same as above.
	 * 
	 * @param gold
	 *            - goal standard segmenetation
	 * @param hypothesis
	 *            - hypothesis segmentation
	 * @param window_size
	 *            - window size
	 * @return
	 */
	static public double calculateWindowDiff(LinkedList<Integer> gold,
			LinkedList<Integer> hypothesis, int window_size) {
		int start = 0;
		int last = gold.getLast();
		int end = last + 1;

		int incorrect = 0;
		int total = 0;

		for (int i = start; i + window_size < end; i++) {
			int gold_count = 0;
			for (Integer s : gold) {
				if (s < last) // the end last boundary is not actually a
								// boundary but represents the end of the data
								// (nothing exist after it, including it self)
				{
					if (s >= i && s < i + window_size) {
						gold_count++;
					}
				}
			}

			int hypo_count = 0;
			for (Integer s : hypothesis) {
				if (s < last) // the end last boundary is not actually a
								// boundary but represents the end of the data
								// (nothing exist after it, including it self)
				{
					if (s >= i && s < i + window_size) {
						hypo_count++;
					}
				}
			}

			if (gold_count != hypo_count) incorrect++;
			total++;
		}
		return (double) incorrect / (double) total;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WinPR\n");
		builder.append("k = ");
		builder.append(this.window_size);
		builder.append("\n");

		builder.append("TP = ");
		builder.append(this.tp);
		builder.append("\n");

		builder.append("TN = ");
		builder.append(this.tn);
		builder.append("\n");

		builder.append("FP = ");
		builder.append(this.fp);
		builder.append("\n");

		builder.append("FN = ");
		builder.append(this.fn);
		builder.append("\n");

		builder.append("precision = ");
		builder.append(this.precision());
		builder.append("\n");

		builder.append("recall = ");
		builder.append(this.recall());
		builder.append("\n");

		return builder.toString();
	}

	public static void main(String args[]) {
		System.out
				.println("Hypothetical segmentation of A B C + D E F G H + I J");
		// Hypothetical segmentation of A B C + D E F G H + I J where + is a
		// boundary and letters are content units (sentences)
		LinkedList<Integer> hypothetical_segmentation =
				new LinkedList<Integer>();
		hypothetical_segmentation.add(2); // between CD
		hypothetical_segmentation.add(7); // between HI
		hypothetical_segmentation.add(9); // End of segmentation

		System.out.println("True segmentation: A B C D E + F G H + I J");
		// True segmentation: A B C D E + F G H + I J
		LinkedList<Integer> true_segmentation = new LinkedList<Integer>();
		true_segmentation.add(4); // between CD
		true_segmentation.add(7); // between HI
		true_segmentation.add(9); // End of segmentation

		double WindowDiff =
				WinPR_impl.calculateWindowDiff(true_segmentation,
						hypothetical_segmentation, 1);
		System.out.println("k = 1\nWindowDiff = " + WindowDiff);

		WinPR_impl winPR =
				WinPR_impl.calculateWinPR(true_segmentation,
						hypothetical_segmentation, 1);
		System.out.println(winPR.toString());

		// For k = 2

		WindowDiff =
				WinPR_impl.calculateWindowDiff(true_segmentation,
						hypothetical_segmentation, 2);
		System.out.println("k = 2\nWindowDiff = " + WindowDiff);

		winPR =
				WinPR_impl.calculateWinPR(true_segmentation,
						hypothetical_segmentation, 2);
		System.out.println(winPR.toString());

		// Another example
		System.out.println("");
		System.out.println("True segmentation: A B C D E + F G H + I J");
		System.out
				.println("Hypothetical segmentation of A B C + D E + F G H + I + J ");
		// Hypothetical segmentation of A B C + D E + F G H + I + J
		hypothetical_segmentation = new LinkedList<Integer>();
		hypothetical_segmentation.add(2); // between CD
		hypothetical_segmentation.add(4); // between EF
		hypothetical_segmentation.add(7); // between HI
		hypothetical_segmentation.add(8); // between IJ
		hypothetical_segmentation.add(9); // End of segmentation

		WindowDiff =
				WinPR_impl.calculateWindowDiff(true_segmentation,
						hypothetical_segmentation, 2);
		System.out.println("k = 2\nWindowDiff = " + WindowDiff);

		winPR =
				WinPR_impl.calculateWinPR(true_segmentation,
						hypothetical_segmentation, 2);
		System.out.println(winPR.toString());
	}
}