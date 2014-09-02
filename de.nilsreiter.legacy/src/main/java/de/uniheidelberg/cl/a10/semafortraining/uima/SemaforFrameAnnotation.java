package de.uniheidelberg.cl.a10.semafortraining.uima;

import java.util.HashMap;
import java.util.Map;

import de.uniheidelberg.cl.reiter.util.Range;

public class SemaforFrameAnnotation extends SemaforAnnotation {
	SemaforSentence sentence;

	String frameName;

	String lexicalUnitName;

	Map<String, SemaforFrameElementAnnotation> fes;

	public SemaforFrameAnnotation(final SemaforSentence sentence) {
		this.sentence = sentence;
		this.fes = new HashMap<String, SemaforFrameElementAnnotation>();
	}

	public void addFrameElementAnnotation(final String frameElementName,
			final Integer targetTokenId) {
		if (!fes.containsKey(frameElementName)) {
			fes.put(frameElementName, new SemaforFrameElementAnnotation(
					frameElementName));
		}
		fes.get(frameElementName).addTargetToken(targetTokenId);
	}

	public void addFrameElementAnnotation(final String frameElementName,
			final Range targetTokenIds) {
		if (!fes.containsKey(frameElementName)) {
			fes.put(frameElementName, new SemaforFrameElementAnnotation(
					frameElementName));
		}
		for (int i = targetTokenIds.getElement1(); i <= targetTokenIds
				.getElement2(); i++) {
			fes.get(frameElementName).addTargetToken(i);
		}
	}

	@Override
	public String getTargetIdString() {
		StringBuffer buf = new StringBuffer();
		for (Integer i : targetTokens) {
			buf.append(String.valueOf(i)).append('_');
		}
		String s = buf.toString();
		return s.substring(0, s.length() - 1);
	};

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(1 + fes.size()).append('\t');
		buf.append(frameName).append('\t');
		buf.append(lexicalUnitName.toLowerCase()).append('\t');
		buf.append(this.getTargetIdString()).append('\t');
		buf.append(sentence.getSurface(targetTokens)).append('\t');
		buf.append(sentence.getSentenceNumber()).append('\t');
		for (SemaforFrameElementAnnotation fea : fes.values()) {
			buf.append(fea.getFrameElementName()).append('\t');
			try {
				buf.append(fea.getTargetIdString()).append('\t');
			} catch (Exception e) {
				System.err.println(sentence.getSentenceNumber());
				System.err.println(sentence.toTokenizedString());
				e.printStackTrace();
			}
		}
		return buf.toString().trim();
	}

	/**
	 * @return the sentence
	 */
	public SemaforSentence getSentence() {
		return sentence;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(final SemaforSentence sentence) {
		this.sentence = sentence;
	}

	/**
	 * @return the frameName
	 */
	public String getFrameName() {
		return frameName;
	}

	/**
	 * @param frameName
	 *            the frameName to set
	 */
	public void setFrameName(final String frameName) {
		this.frameName = frameName;
	}

	/**
	 * @return the lexicalUnitName
	 */
	public String getLexicalUnitName() {
		return lexicalUnitName;
	}

	/**
	 * @param lexicalUnitName
	 *            the lexicalUnitName to set
	 */
	public void setLexicalUnitName(final String lexicalUnitName) {
		this.lexicalUnitName = lexicalUnitName;
	};

}
