package de.uniheidelberg.cl.a10.data;

import java.util.Iterator;
import java.util.List;

import de.uniheidelberg.cl.a10.parser.dep.IDependency;

public class MatrixFactory {

	public static short[][] getLabeledMatrix(final List<BaseSentence> list1,
			final List<BaseSentence> list2) {
		if (list1.size() != list2.size())
			return null;

		Iterator<BaseSentence> sentenceIter1 = list1.iterator();
		Iterator<BaseSentence> sentenceIter2 = list2.iterator();
		int numberOfTokens = 0;
		int maximalNumberOfTokens = Integer.MIN_VALUE;
		IDependency[] dependencies = null;
		while (sentenceIter1.hasNext()) {
			BaseSentence sentence = sentenceIter1.next();
			maximalNumberOfTokens = Math.max(maximalNumberOfTokens,
					sentence.size());
			if (dependencies == null) {
				dependencies = sentence.get(0).getDependencyRelation()
						.getCategories();
			}
			numberOfTokens += (sentence.size() * sentence.size() * dependencies.length);
		}

		sentenceIter1 = list1.iterator();

		short[][] ret = new short[numberOfTokens][];
		int i = 0;
		while (sentenceIter1.hasNext()) {
			BaseSentence sentence1 = sentenceIter1.next();
			BaseSentence sentence2 = sentenceIter2.next();

			for (int j = 0; j < sentence1.size(); j++) {
				for (int k = 0; k < sentence2.size(); k++) {
					for (int l = 0; l < dependencies.length; l++) {
						ret[i] = new short[] { 0, 0 };
						if (sentence1.get(j).getGovernor() == sentence1.get(k)
								&& sentence1.get(j).getDependencyRelation() == dependencies[k]) {
							ret[i][0] += 1;
						} else {
							ret[i][1] += 1;
						}
						if (sentence2.get(j).getGovernor() == sentence2.get(k)
								&& sentence2.get(j).getDependencyRelation() == dependencies[k]) {
							ret[i][0] += 1;
						} else {
							ret[i][1] += 1;
						}
						i++;
					}

				}
			}

		}

		return ret;

	}

	public static short[][] getMatrix(final List<BaseSentence> list1,
			final List<BaseSentence> list2) {
		if (list1.size() != list2.size())
			return null;

		Iterator<BaseSentence> sentenceIter1 = list1.iterator();
		Iterator<BaseSentence> sentenceIter2 = list2.iterator();

		int numberOfTokens = 0;
		int maximalNumberOfTokens = Integer.MIN_VALUE;
		while (sentenceIter1.hasNext()) {
			BaseSentence sentence = sentenceIter1.next();
			maximalNumberOfTokens = Math.max(maximalNumberOfTokens,
					sentence.size());
			numberOfTokens += (sentence.size() * sentence.size());
		}

		sentenceIter1 = list1.iterator();

		short[][] ret = new short[numberOfTokens][];
		int i = 0;

		while (sentenceIter1.hasNext()) {
			BaseSentence sentence1 = sentenceIter1.next();
			BaseSentence sentence2 = sentenceIter2.next();

			for (int j = 0; j < sentence1.size(); j++) {
				for (int k = 0; k < sentence2.size(); k++) {
					ret[i] = new short[] { 0, 0 };
					if (sentence1.get(j).getGovernor() == sentence1.get(k)) {
						ret[i][0] += 1;
					} else {
						ret[i][1] += 1;
					}
					if (sentence2.get(j).getGovernor() == sentence2.get(k)) {
						ret[i][0] += 1;
					} else {
						ret[i][1] += 1;
					}
					i++;

				}
			}

		}

		return ret;

	}
}
