package de.uniheidelberg.cl.a10.data.io;

import java.io.IOException;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;

public class CoNLL09Writer implements Writer {
	java.io.Writer writer;

	public static char TAB = '\t';

	public CoNLL09Writer(final java.io.Writer wr) {
		this.writer = wr;
	}

	public boolean write(final BaseSentence sentence) throws IOException {
		StringBuilder b = new StringBuilder();

		int i = 1;
		for (BaseToken token : sentence) {
			b.append(i).append(TAB);
			b.append(token.getWord()).append(TAB);
			b.append(token.getLemma()).append(TAB);
			b.append(token.getLemma()).append(TAB);
			b.append(token.getPartOfSpeech().toShortString()).append(TAB);
			b.append(token.getPartOfSpeech().toShortString()).append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			if (sentence.hasDependencyAnnotation()) {
				if (token.getGovernor() != null) {
					b.append(sentence.indexOf(token.getGovernor()) + 1).append(
							TAB);
					b.append(sentence.indexOf(token.getGovernor()) + 1).append(
							TAB);
				} else {
					b.append('0').append(TAB);
					b.append('0').append(TAB);
				}
				if (token.getDependencyRelation() != null) {
					b.append(token.getDependencyRelation().toString()).append(
							TAB);
					b.append(token.getDependencyRelation().toString()).append(
							TAB);
				} else {
					b.append("ROOT").append(TAB);
					b.append("ROOT").append(TAB);
				}
			} else {
				b.append('_').append(TAB);
				b.append('_').append(TAB);
				b.append('_').append(TAB);
				b.append('_').append(TAB);
			}
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('_').append(TAB);
			b.append('\n').append('\n');
		}

		writer.write(b.toString());

		return true;

	}

	public void close() throws IOException {
		this.writer.close();
	}
}
