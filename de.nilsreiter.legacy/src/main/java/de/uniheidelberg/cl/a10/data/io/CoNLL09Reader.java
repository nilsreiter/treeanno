package de.uniheidelberg.cl.a10.data.io;

import is2.data.SentenceData09;
import is2.io.CONLLReader09;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseText;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.data.IHasDependencyStyle;
import de.uniheidelberg.cl.a10.data.IHasPartOfSpeechStyle;
import de.uniheidelberg.cl.a10.parser.dep.IDependency;
import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;

public class CoNLL09Reader implements Reader, IHasPartOfSpeechStyle,
IHasDependencyStyle {
	Class<? extends IDependency> dependencyStyle = null;
	Class<? extends IPartOfSpeech> partOfSpeechStyle = null;
	File file;
	java.io.Reader reader;

	public CoNLL09Reader(final File file,
			final Class<? extends IDependency> dependencyStyle,
			final Class<? extends IPartOfSpeech> partOfSpeechStyle) {
		this.dependencyStyle = dependencyStyle;
		this.partOfSpeechStyle = partOfSpeechStyle;
		try {
			this.reader = new FileReader(file);
		} catch (FileNotFoundException e) {}
	}

	public CoNLL09Reader(final java.io.Reader reader,
			final Class<? extends IDependency> dependencyStyle,
			final Class<? extends IPartOfSpeech> partOfSpeechStyle) {
		this.dependencyStyle = dependencyStyle;
		this.partOfSpeechStyle = partOfSpeechStyle;
		this.reader = reader;
	}

	@Override
	public BaseText getSentences() throws IOException {
		BufferedReader br = new BufferedReader(this.reader);
		return this.getSentences(br);
	}

	public BaseText getSentencesViaMate() throws Exception {
		CONLLReader09 cnl = new CONLLReader09(this.file.getAbsolutePath());
		BaseText baseText = new BaseText();

		SentenceData09 sd;
		while ((sd = cnl.getNext()) != null) {
			baseText.add(BaseSentence.fromSentenceData09(sd));
		}

		return baseText;
	}

	public BaseText getSentences(final BufferedReader reader)
			throws IOException {
		BaseText baseText = new BaseText();

		BaseSentence sentence = new BaseSentence();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.matches("^\\p{Space}*$")) {
				if (!sentence.isEmpty()) {
					baseText.add(sentence);
					sentence = new BaseSentence();
				}
			} else {
				sentence.add(this.readLine(sentence, line));
			}
		}

		return baseText;
	}

	protected BaseToken
			readLine(final BaseSentence sentence, final String line) {

		BaseToken token = new BaseToken();
		String[] parts = line.split("[\t ]");
		token.setSentence(sentence);
		token.setWord(parts[1]);
		token.setLemma(parts[2]);
		token.setPartOfSpeech(PTB.fromString(parts[4]));
		token.setDependencyRelation(StanfordDependency.fromString(parts[10]));
		token.setGovernor(Integer.valueOf(parts[8]) - 1);
		return token;
	}

	@Override
	public Class<? extends IDependency> getDependencyStyle() {
		return this.dependencyStyle;
	}

	@Override
	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle() {
		return this.partOfSpeechStyle;
	}

}
