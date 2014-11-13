package de.uniheidelberg.cl.a10.postagging;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

public interface IPartOfSpeechTagger {

	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle();

	public boolean tag(BaseSentence sentence);
}
