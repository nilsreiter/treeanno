package de.uniheidelberg.cl.a10.data.io;

import java.io.IOException;

import de.uniheidelberg.cl.a10.data.BaseText;

public interface Reader {
	public BaseText getSentences() throws IOException;
}
