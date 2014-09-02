package de.uniheidelberg.cl.a10.parser;

import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.parser.dep.StringDependency;

public class Dummy extends AbstractDependencyParser {

	public Dummy() {
		super(StringDependency.class);
	}

	@Override
	public boolean parse(final BaseSentence sentence) {
		for (int i = 0; i < sentence.size(); i++) {
			BaseToken pt = sentence.get(i);
			if (i == 0) {
				pt.setGovernor(null);
				pt.setDependencyRelation(null);
			} else {
				pt.setGovernor(sentence.get(i - 1));
				pt.setDependencyRelation(this.fromString("root"));
			}
		}
		return true;
	}

}
