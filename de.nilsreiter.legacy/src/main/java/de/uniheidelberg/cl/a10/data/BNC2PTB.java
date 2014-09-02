package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.reiter.pos.BNC;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;

public class BNC2PTB implements PartOfSpeechConverter {

	@Override
	public Class<? extends IPartOfSpeech> from() {
		return BNC.class;
	}

	@Override
	public Class<? extends IPartOfSpeech> to() {
		return PTB.class;
	}

	@Override
	public IPartOfSpeech convert(final IPartOfSpeech pos) {
		if (pos.getClass() != BNC.class)
			throw new IllegalArgumentException();
		return ((BNC) pos).asPTB();
	}

}
