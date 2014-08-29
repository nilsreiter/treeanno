package de.uniheidelberg.cl.a10.postagging;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.lang.english.PosTagger;

import org.dom4j.DocumentException;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.opennlp.A10POSDictionary;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;

public class OpenNLPPartOfSpeechTagger implements IPartOfSpeechTagger {
	Class<? extends IPartOfSpeech> partOfSpeechStyle;
	PosTagger tagger;

	public OpenNLPPartOfSpeechTagger() throws IOException, DocumentException {
		this.partOfSpeechStyle = PTB.class;
		this.tagger = new PosTagger(new File(
				"uima/resources/OpenNLP_Model_tag.bin.gz").getCanonicalPath(),
				new A10POSDictionary(new File("lib/dictionary.xml")
						.getAbsolutePath()));
	}

	public OpenNLPPartOfSpeechTagger(
			final Class<? extends IPartOfSpeech> partOfSpeechStyle,
			final File model, final File dictionaryFile) throws IOException,
			DocumentException {
		this.partOfSpeechStyle = partOfSpeechStyle;

		this.tagger = new PosTagger(model.getCanonicalPath(),
				new A10POSDictionary(dictionaryFile.getAbsolutePath()));
	}

	public OpenNLPPartOfSpeechTagger(
			final Class<? extends IPartOfSpeech> partOfSpeechStyle,
			final File model) throws IOException, DocumentException {
		this.partOfSpeechStyle = partOfSpeechStyle;

		this.tagger = new PosTagger(model.getCanonicalPath(), (Dictionary) null);
	}

	@Override
	public boolean tag(final BaseSentence sentence) {

		String[][] tags = tagger.tag(1, sentence.toWordArray());
		for (int i = 0; i < tags[0].length; i++) {
			BaseToken token = sentence.get(i);
			String posTag = tags[0][i];
			try {
				token.setPartOfSpeech((IPartOfSpeech) partOfSpeechStyle
						.getMethod("fromString", String.class).invoke(null,
								posTag));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			} catch (SecurityException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return false;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return false;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle() {
		return this.partOfSpeechStyle;
	}

}
