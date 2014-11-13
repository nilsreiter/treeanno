package de.uniheidelberg.cl.a10.opennlp;

import java.util.ArrayList;

import opennlp.tools.chunker.DefaultChunkerContextGenerator;

public class A10DefaultChunkerContextGenerator extends
		DefaultChunkerContextGenerator implements A10ChunkerContextGenerator {

	@Override
	public String[] getContext(final int index, final Object[] sequence,
			final String[] priorDecisions, final Object[] additionalContext) {
		String[] stringsequence = new String[sequence.length];
		for (int i = 0; i < sequence.length; i++) {
			stringsequence[i] = (String) sequence[i];
		}

		Object[][] addContext = (Object[][]) additionalContext;

		String[] scontext = new String[addContext[0].length];
		for (int i = 0; i < addContext[0].length; i++) {
			scontext[i] = (String) addContext[0][i];
		}

		return getContext(index, new String[][] { priorDecisions,
				(String[]) addContext[0], (String[]) addContext[1],
				(String[]) addContext[2], (String[]) addContext[3] });
	}

	private ArrayList<String> getDomainContext(final int i,
			final Object[] preds, final Object[] toks, final Object[] tags) {
		// Words in a 5-word window
		String w_2, w_1, w0, w1, w2;

		// Tags in a 5-word window
		String t_2, t_1, t0, t1, t2;

		// Previous predictions
		String p_2, p_1;

		if (i < 2) {
			w_2 = "w_2=bos";
			t_2 = "t_2=bos";
			p_2 = "p_2=bos";
		} else {
			w_2 = "w_2=" + toks[i - 2];
			t_2 = "t_2=" + tags[i - 2];
			p_2 = "p_2=" + preds[i - 2];
		}

		if (i < 1) {
			w_1 = "w_1=bos";
			t_1 = "t_1=bos";
			p_1 = "p_1=bos";
		} else {
			w_1 = "w_1=" + toks[i - 1];
			t_1 = "t_1=" + tags[i - 1];
			p_1 = "p_1=" + preds[i - 1];
		}

		w0 = "w0=" + toks[i];
		t0 = "t0=" + tags[i];

		if (i + 1 >= toks.length) {
			w1 = "w1=eos";
			t1 = "t1=eos";
		} else {
			w1 = "w1=" + toks[i + 1];
			t1 = "t1=" + tags[i + 1];
		}

		if (i + 2 >= toks.length) {
			w2 = "w2=eos";
			t2 = "t2=eos";
		} else {
			w2 = "w2=" + toks[i + 2];
			t2 = "t2=" + tags[i + 2];
		}

		String[] of = new String[] {
				// add word features
				w_2, w_1, w0, w1,
				w2,
				w_1 + w0,
				w0 + w1,

				// add tag features
				t_2, t_1, t0, t1, t2, t_2 + t_1, t_1 + t0, t0 + t1, t1 + t2,
				t_2 + t_1 + t0,
				t_1 + t0 + t1,
				t0 + t1 + t2,

				// add pred tags
				p_2,
				p_1,
				p_2 + p_1,

				// add pred and tag
				p_1 + t_2, p_1 + t_1, p_1 + t0, p_1 + t1, p_1 + t2,
				p_1 + t_2 + t_1, p_1 + t_1 + t0, p_1 + t0 + t1, p_1 + t1 + t2,
				p_1 + t_2 + t_1 + t0, p_1 + t_1 + t0 + t1, p_1 + t0 + t1 + t2,

				// add pred and word
				p_1 + w_2, p_1 + w_1, p_1 + w0, p_1 + w1, p_1 + w2,
				p_1 + w_1 + w0, p_1 + w0 + w1 };

		ArrayList<String> flist = new ArrayList<String>();
		for (int k = 0; k < of.length; k++) {
			flist.add(of[k]);
		}

		return flist;
	}

	@Override
	public String[] getContext(final int i, final String[][] features) {

		ArrayList<String> flist = new ArrayList<String>();

		flist.addAll(this.getDomainContext(i, features[0], features[1],
				features[2]));
		flist.addAll(this.getDomainContext(i, features[0], features[3],
				features[4]));

		return flist.toArray(new String[flist.size()]);
	}
}
