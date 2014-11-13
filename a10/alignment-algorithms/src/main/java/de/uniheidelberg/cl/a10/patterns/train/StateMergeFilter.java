package de.uniheidelberg.cl.a10.patterns.train;

import org.apache.commons.math3.util.Pair;

import de.uniheidelberg.cl.a10.Filter;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.patterns.models.HiddenMarkovModel;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

public class StateMergeFilter implements Filter<Pair<Integer, Integer>> {

	HiddenMarkovModel<? extends HasDocument> hmm;

	public StateMergeFilter(
			final HiddenMarkovModel<? extends HasDocument> hiddenMarkovModel) {
		hmm = hiddenMarkovModel;
	}

	@Override
	public boolean check(final Pair<Integer, Integer> pair) {
		// if (HasDocument.class.isAssignableFrom(hmm.getEvents().iterator()
		// .next().getClass())) {
		int s1 = pair.getFirst();
		int s2 = pair.getSecond();

		if (hmm instanceof SEHiddenMarkovModel_impl) {
			SEHiddenMarkovModel_impl<? extends HasDocument> sehmm =
					(SEHiddenMarkovModel_impl<? extends HasDocument>) hmm;
			return (sehmm.getStartState() != pair.getFirst()
					&& pair.getFirst() != SEHiddenMarkovModel_impl.END
					&& sehmm.getStartState() != pair.getSecond()
					&& pair.getSecond() > pair.getFirst() && pair.getSecond() != SEHiddenMarkovModel_impl.END);
		}

		if (hmm instanceof HiddenMarkovModel_impl) {
			HiddenMarkovModel_impl<? extends HasDocument> hmmp =
					(HiddenMarkovModel_impl<? extends HasDocument>) hmm;
			if (hmmp.getEventsForState(s1).size() == 1
					&& hmmp.getEventsForState(s2).size() == 1) {
				HasDocument ev1 =
						hmmp.getEventsForState(pair.getFirst()).iterator()
								.next();
				HasDocument ev2 =
						hmmp.getEventsForState(pair.getSecond()).iterator()
								.next();
				if (ev1.getRitualDocument() != null
						&& ev2.getRitualDocument() != null
						&& ev1.getRitualDocument().equals(
								ev2.getRitualDocument())) return false;
			}
		}
		// }

		return pair.getSecond() > pair.getFirst();
	}

}
