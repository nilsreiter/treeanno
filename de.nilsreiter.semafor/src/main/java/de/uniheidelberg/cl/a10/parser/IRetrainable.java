package de.uniheidelberg.cl.a10.parser;

import java.util.List;

import de.uniheidelberg.cl.a10.data.BaseSentence;

public interface IRetrainable {
	public boolean retrain(List<BaseSentence> corpus, boolean saveModel,
			Object... options);

	public boolean retrain(BaseSentence sentence, boolean saveModel,
			Object... options);
}
