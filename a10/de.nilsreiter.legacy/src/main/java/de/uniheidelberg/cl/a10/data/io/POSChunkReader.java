package de.uniheidelberg.cl.a10.data.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseText;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.data.Constants;
import de.uniheidelberg.cl.reiter.pos.PTB;

public class POSChunkReader implements Reader {
	File file;

	public POSChunkReader(final File file) {
		this.file = file;
	}

	@Override
	public BaseText getSentences() throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(this.file));

		String line = null;
		BaseSentence currentSentence = new BaseSentence();
		BaseText text = new BaseText();
		while ((line = fr.readLine()) != null) {
			if (line.equals("")) {
				currentSentence.toSurfaceString();
				text.add(currentSentence);
				currentSentence = new BaseSentence();
			} else {
				String[] lp = line.split(" ");
				BaseToken bt = new BaseToken();
				bt.setPartOfSpeech(PTB.fromString(lp[2]));

				bt.setWord(lp[1]);
				bt.data.put(Constants.DATA_KEY_UIMAID, lp[0]);

				currentSentence.add(bt);
			}

		}
		fr.close();
		return text;
	}

	protected int getSentenceId(final String tokenId) {
		String[] p = tokenId.split("_");
		int r = Integer.valueOf(p[1]);
		return r;
	}
}
