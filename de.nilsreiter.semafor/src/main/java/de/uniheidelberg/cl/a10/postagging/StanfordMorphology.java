package de.uniheidelberg.cl.a10.postagging;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.reiter.pos.BNC;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.process.Morphology;

public class StanfordMorphology implements ILemmatizer {
	Morphology morphology;

	public StanfordMorphology() {
		morphology = new Morphology();

	}

	@Override
	public boolean lemmatize(final BaseSentence sentence) {
		for (BaseToken token : sentence) {
			token.setLemma(morphology.lemmatize(
					new WordTag(token.getWord(), token.getPartOfSpeech()
							.toShortString())).lemma());
		}
		return true;
	}

	@Override
	public boolean lemmatize(final BaseToken token) {
		if (token.getPartOfSpeech() == null
				|| token.getPartOfSpeechStyle() == BNC.class) {
			token.setLemma(token.getWord());
			return false;
		} else {
			token.setLemma(morphology.lemmatize(
					new WordTag(token.getWord(), token.getPartOfSpeech()
							.toShortString())).lemma());
		}
		return true;
	}

}
