package de.uniheidelberg.cl.a10.data;

import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.parser.dep.IDependency;
import de.uniheidelberg.cl.reiter.util.Pair;

public class Comparator implements IHasDependencyStyle {
	public static class BaseTokenPair extends Pair<BaseToken, BaseToken> {

		public BaseTokenPair(final BaseToken e1, final BaseToken e2) {
			super(e1, e2);
		}

		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer();
			BaseToken e1 = this.getElement1();
			BaseToken e2 = this.getElement2();
			buf.append(e1.getDependencyRelation())
					.append('(')
					.append(e1.getWord())
					.append(',')
					.append((e1.getGovernor() == null ? "null" : e1
							.getGovernor().getWord()))
					.append(')')
					.append(" vs. ")
					.append(e2.getDependencyRelation())
					.append('(')
					.append(e2.getWord())
					.append(',')
					.append((e2.getGovernor() == null ? "null" : e2
							.getGovernor().getWord())).append(')');

			return buf.toString();
		}

	}

	Class<? extends IDependency> dependencyStyle;

	public Comparator(final Class<? extends IDependency> style) {
		this.dependencyStyle = style;
	}

	@Override
	public Class<? extends IDependency> getDependencyStyle() {
		return this.dependencyStyle;
	}

	public List<BaseTokenPair> compareSentences(final BaseSentence sentence1,
			final BaseSentence sentence2) {
		if (sentence1.size() != sentence2.size()) {
			return null;
		}
		List<BaseTokenPair> ret = new LinkedList<BaseTokenPair>();
		for (int i = 0; i < sentence1.size(); i++) {
			BaseToken token1 = sentence1.get(i);
			BaseToken token2 = sentence2.get(i);
			if (token1.getDependencyRelation() == token2
					.getDependencyRelation()) {
				if (sentence1.indexOf(token1.getGovernor()) != sentence2
						.indexOf(token2.getGovernor())) {
					ret.add(new BaseTokenPair(token1, token2));
				}
			} else {
				ret.add(new BaseTokenPair(token1, token2));
			}
		}
		return ret;

	}

}
