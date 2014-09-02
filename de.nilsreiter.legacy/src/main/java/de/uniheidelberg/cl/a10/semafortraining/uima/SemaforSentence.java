package de.uniheidelberg.cl.a10.semafortraining.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.a10.semafortraining.Constants;
import de.uniheidelberg.cl.reiter.pos.PTB;

/**
 * @model
 * @author reiter
 * 
 */
public class SemaforSentence {

	List<String> tokens;
	List<PTB> posTags;
	List<StanfordDependency> dependencyLabels;
	List<Integer> syntacticParents;
	List<String> lemmas;
	static Semaphore printSemaphore = new Semaphore(1);

	int sentenceNumber;

	Set<SemaforFrameAnnotation> frameAnnotations;

	public SemaforSentence(final int numbers) {
		init();
		sentenceNumber = numbers;
	}

	private void init() {
		tokens = new ArrayList<String>();
		posTags = new ArrayList<PTB>();
		dependencyLabels = new ArrayList<StanfordDependency>();
		lemmas = new ArrayList<String>();
		syntacticParents = new ArrayList<Integer>();
		frameAnnotations = new HashSet<SemaforFrameAnnotation>();
	}

	public void addToken(final String t, final PTB pos, final String lemma) {
		tokens.add(t);
		posTags.add(pos);
		lemmas.add(lemma);
		dependencyLabels.add(null);
		syntacticParents.add(null);
	}

	public void setDependency(final int tokenIndex,
			final StanfordDependency label, final int parent) {
		if (tokenIndex >= tokens.size()) {
			throw new ArrayIndexOutOfBoundsException(tokenIndex);
		}
		dependencyLabels.set(tokenIndex, label);
		syntacticParents.set(tokenIndex, parent + 1);
	}

	public void root() {
		int root = -1;
		for (int i = 0; i < tokens.size(); i++) {
			if (syntacticParents.get(i) == null && posTags.get(i) != PTB.SENT) {
				syntacticParents.set(i, 0);
				dependencyLabels.set(i, null);
				root = i;
			}
		}
		for (int i = 0; i < tokens.size(); i++) {
			if (syntacticParents.get(i) == null) {
				syntacticParents.set(i, root);
				dependencyLabels.set(i, StanfordDependency.PUNCT);
			}
		}
	}

	/**
	 * @model
	 * @param i
	 * @return
	 */
	public String getToken(final int i) {
		return tokens.get(i);
	}

	public boolean hasFrameAnnotations() {
		return !this.frameAnnotations.isEmpty();
	}

	public boolean wellFormed() {
		for (int i = 0; i < tokens.size(); i++) {
			if (!this.isRootReachable(i, new HashSet<Integer>())) {
				return false;
			}
		}
		return true;
	}

	protected boolean isRootReachable(final int i, final Set<Integer> history) {
		if (i == 0) return true;
		int parent = syntacticParents.get(i);
		if (history.contains(parent)) return false;
		history.add(parent);
		return this.isRootReachable(parent, history);
	}

	public void
			addFrameAnnotation(final SemaforFrameAnnotation frameAnnotation) {
		if (frameAnnotation.getTargetTokens() != null
				&& !frameAnnotation.getTargetTokens().isEmpty()) {
			int index = frameAnnotation.getTargetTokens().iterator().next();
			if (index > tokens.size()) {
				throw new ArrayIndexOutOfBoundsException(index);
			}
		}
		frameAnnotation.setSentence(this);
		frameAnnotations.add(frameAnnotation);

	}

	public int getSentenceNumber() {
		return sentenceNumber;
	}

	public String getSurface(final SortedSet<Integer> tokens) {
		StringBuffer buf = new StringBuffer();
		for (Integer i : tokens) {
			buf.append(this.getToken(i)).append(' ');
		}
		return buf.toString().trim();
	}

	public String toSyntaxString() {
		StringBuffer buf = new StringBuffer();
		buf.append(tokens.size()).append('\t');
		for (String s : tokens) {
			buf.append(s).append('\t');
		}
		for (PTB s : posTags) {
			buf.append(s.toShortString()).append('\t');
		}
		for (StanfordDependency s : dependencyLabels) {
			buf.append(s).append('\t');
		}
		for (Integer i : syntacticParents) {
			buf.append(i).append('\t');
		}
		for (int i = 0; i < tokens.size(); i++) {
			buf.append('O').append('\t');
		}
		for (String s : lemmas) {
			buf.append(s).append('\t');
		}
		return buf.toString().trim();
	}

	public String toTokenizedString() {
		StringBuffer buf = new StringBuffer();
		for (String s : tokens) {
			buf.append(s).append(' ');
		}
		return buf.toString().trim();
	}

	public void print(final File directory) throws IOException {
		if (!directory.exists()) {
			directory.mkdirs();
		}
		FileWriter[] fw = new FileWriter[3];
		fw[0] =
				FileWriterHandling.get(new File(directory,
						Constants.FILENAME_SENTENCES_TOKENIZED));
		fw[1] =
				FileWriterHandling.get(new File(directory,
						Constants.FILENAME_SENTENCES_FRAMES));
		fw[2] =
				FileWriterHandling.get(new File(directory,
						Constants.FILENAME_SENTENCES_SYNTAX));
		this.print(fw);

	}

	private void print(final FileWriter[] fileWriters) throws IOException {
		try {
			printSemaphore.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			for (SemaforFrameAnnotation sfa : frameAnnotations) {
				sfa.toString();
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}

		// 1. tokenized text
		fileWriters[0].write(this.toTokenizedString());
		fileWriters[0].write("\n");

		// 2. frame annotations
		for (SemaforFrameAnnotation sfa : frameAnnotations) {
			if (sfa.getTargetTokens() != null
					&& !sfa.getTargetTokens().isEmpty()) {
				fileWriters[1].write(sfa.toString());
				fileWriters[1].write("\n");
			}
		}

		// 3. syntax
		fileWriters[2].write(this.toSyntaxString());
		fileWriters[2].write("\n");

		for (FileWriter fw : fileWriters) {
			fw.flush();
		}
		printSemaphore.release();
	}
}
