package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

public interface PartOfSpeechConverter {
	public Class<? extends IPartOfSpeech> from();

	public Class<? extends IPartOfSpeech> to();

	public IPartOfSpeech convert(IPartOfSpeech pos);

}
