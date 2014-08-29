package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

public interface IHasPartOfSpeech extends IHasPartOfSpeechStyle {
	IPartOfSpeech getPartOfSpeech();

}
