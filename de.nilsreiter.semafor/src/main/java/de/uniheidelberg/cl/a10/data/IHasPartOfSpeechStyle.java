package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

public interface IHasPartOfSpeechStyle {
	Class<? extends IPartOfSpeech> getPartOfSpeechStyle();

}
