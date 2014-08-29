package de.uniheidelberg.cl.a10.parser;

import is2.data.SentenceData09;
import is2.io.CONLLReader09;

import java.util.Iterator;
import java.util.List;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.IHasDependencyStyle;
import de.uniheidelberg.cl.a10.parser.dep.IDependency;

public class Evaluator implements IHasDependencyStyle {
	Class<? extends IDependency> dependencyStyle;

	int limit = 0;

	public static class Results {

		public int total;
		public int corr;
		public float las;
		public float ula;

	}

	/**
	 * This is based on the class is2.parser.Evaluator from mate-tools.
	 * 
	 * @param gold
	 * @param silver
	 * @return
	 */
	public Results run(final List<BaseSentence> gold,
			final List<BaseSentence> silver) {

		int total = 0, corr = 0, corrL = 0;
		int numsent = 0, corrsent = 0, corrsentL = 0;
		Iterator<BaseSentence> goldIterator = gold.iterator();
		Iterator<BaseSentence> silverIterator = silver.iterator();
		SentenceData09 goldInstance = goldIterator.next().getSentenceData09();
		SentenceData09 silverInstance =
				silverIterator.next().getSentenceData09();

		while (goldIterator.hasNext() && (limit == 0 || numsent < limit)) {

			int instanceLength = goldInstance.length();

			if (instanceLength != silverInstance.length())
				System.out.println("Lengths do not match on sentence "
						+ numsent);

			int[] goldHeads = goldInstance.heads;
			String[] goldLabels = goldInstance.labels;
			int[] predHeads = silverInstance.heads;
			String[] predLabels = silverInstance.labels;

			boolean whole = true;
			boolean wholeL = true;

			// NOTE: the first item is the root info added during
			// nextInstance(), so we skip it.

			int punc = 0;
			for (int i = 1; i < instanceLength; i++) {
				if (predHeads[i] == goldHeads[i]) {
					corr++;

					if (goldLabels[i].equals(predLabels[i]))
						corrL++;
					else {
						// System.out.println(numsent+" error gold "+goldLabels[i]+" "+predLabels[i]+" head "+goldHeads[i]+" child "+i);
						wholeL = false;
					}
				} else {
					// System.out.println(numsent+"error gold "+goldLabels[i]+" "+predLabels[i]+" head "+goldHeads[i]+" child "+i);
					whole = false;
					wholeL = false;
				}
			}
			total += ((instanceLength - 1) - punc); // Subtract one to not score
			// fake root token

			if (whole) corrsent++;
			if (wholeL) corrsentL++;
			numsent++;

			goldInstance = goldIterator.next().getSentenceData09();
			silverInstance = silverIterator.next().getSentenceData09();
		}

		Results r = new Results();

		r.total = total;
		r.corr = corr;
		r.las = (float) Math.round(((double) corrL / total) * 100000) / 1000;
		r.ula = (float) Math.round(((double) corr / total) * 100000) / 1000;
		System.out.print("Total: " + total + " \tCorrect: " + corr + " ");
		System.out.println("LAS: "
				+ (double) Math.round(((double) corrL / total) * 100000) / 1000
				+ " \tTotal: "
				+ (double) Math.round(((double) corrsentL / numsent) * 100000)
				/ 1000 + " \tULA: "
				+ (double) Math.round(((double) corr / total) * 100000) / 1000
				+ " \tTotal: "
				+ (double) Math.round(((double) corrsent / numsent) * 100000)
				/ 1000);

		return r;
	}

	public Results run(final CONLLReader09 goldReader,
			final CONLLReader09 silverReader) throws Exception {

		int total = 0, corr = 0, corrL = 0;
		int numsent = 0, corrsent = 0, corrsentL = 0;

		SentenceData09 goldInstance = goldReader.getNext();
		SentenceData09 predInstance = silverReader.getNext();

		while (goldInstance != null && (limit == 0 || numsent < limit)) {

			int instanceLength = goldInstance.length();

			if (instanceLength != predInstance.length())
				System.out.println("Lengths do not match on sentence "
						+ numsent);

			int[] goldHeads = goldInstance.heads;
			String[] goldLabels = goldInstance.labels;
			int[] predHeads = predInstance.heads;
			String[] predLabels = predInstance.labels;

			boolean whole = true;
			boolean wholeL = true;

			// NOTE: the first item is the root info added during
			// nextInstance(), so we skip it.

			int punc = 0;
			for (int i = 1; i < instanceLength; i++) {
				if (predHeads[i] == goldHeads[i]) {
					corr++;

					if (goldLabels[i].equals(predLabels[i]))
						corrL++;
					else {
						// System.out.println(numsent+" error gold "+goldLabels[i]+" "+predLabels[i]+" head "+goldHeads[i]+" child "+i);
						wholeL = false;
					}
				} else {
					// System.out.println(numsent+"error gold "+goldLabels[i]+" "+predLabels[i]+" head "+goldHeads[i]+" child "+i);
					whole = false;
					wholeL = false;
				}
			}
			total += ((instanceLength - 1) - punc); // Subtract one to not score
			// fake root token

			if (whole) corrsent++;
			if (wholeL) corrsentL++;
			numsent++;

			goldInstance = goldReader.getNext();
			predInstance = silverReader.getNext();
		}

		Results r = new Results();

		r.total = total;
		r.corr = corr;
		r.las = (float) Math.round(((double) corrL / total) * 100000) / 1000;
		r.ula = (float) Math.round(((double) corr / total) * 100000) / 1000;
		System.out.print("Total: " + total + " \tCorrect: " + corr + " ");
		System.out.println("LAS: "
				+ (double) Math.round(((double) corrL / total) * 100000) / 1000
				+ " \tTotal: "
				+ (double) Math.round(((double) corrsentL / numsent) * 100000)
				/ 1000 + " \tULA: "
				+ (double) Math.round(((double) corr / total) * 100000) / 1000
				+ " \tTotal: "
				+ (double) Math.round(((double) corrsent / numsent) * 100000)
				/ 1000);

		return r;
	}

	@Override
	public Class<? extends IDependency> getDependencyStyle() {
		return this.dependencyStyle;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(final int limit) {
		this.limit = limit;
	}

}
