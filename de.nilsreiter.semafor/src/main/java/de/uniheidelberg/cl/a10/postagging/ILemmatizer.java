package de.uniheidelberg.cl.a10.postagging;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;

public interface ILemmatizer {
	public boolean lemmatize(BaseSentence sentence);

	public boolean lemmatize(BaseToken token);

}
