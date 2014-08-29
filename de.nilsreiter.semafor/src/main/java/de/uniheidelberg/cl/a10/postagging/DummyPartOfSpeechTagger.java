package de.uniheidelberg.cl.a10.postagging;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;

public class DummyPartOfSpeechTagger implements IPartOfSpeechTagger {

	public DummyPartOfSpeechTagger() {

	};

	@Override
	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle() {
		return PTB.class;
	}

	@Override
	public boolean tag(final BaseSentence sentence) {
		for (int i = 0; i < sentence.size(); i++) {
			sentence.get(i).setPartOfSpeech(PTB.NN);
		}
		return true;
	}

}
