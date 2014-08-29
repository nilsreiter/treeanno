package de.uniheidelberg.cl.a10.postagging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;

import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.Constants;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

public class FoldingPartOfSpeechTagger implements IPartOfSpeechTagger {
	Class<? extends IPartOfSpeech> partOfSpeechStyle;

	File dictionary;

	Map<String, Integer> index;

	int folds;

	IPartOfSpeechTagger[] tagger;

	public FoldingPartOfSpeechTagger(
			final Class<? extends IPartOfSpeech> partOfSpeechStyle,
			final File model, final int folds, final File dictionary)
			throws IOException, DocumentException {
		this.partOfSpeechStyle = partOfSpeechStyle;
		this.dictionary = dictionary;
		this.folds = folds;
		this.tagger = new IPartOfSpeechTagger[folds + 1];
		File[] models = model.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File arg0, final String arg1) {
				return arg1.endsWith(".bin.gz");
			}
		});

		for (int i = 0; i < folds; i++) {
			tagger[i] =
					new OpenNLPPartOfSpeechTagger(partOfSpeechStyle, models[i],
							dictionary);
		}
		tagger[folds] =
				new OpenNLPPartOfSpeechTagger(partOfSpeechStyle, models[folds],
						dictionary);

		this.index = readIndex(new File(model, "index"));
	}

	protected Map<String, Integer> readIndex(final File file)
			throws IOException {
		Map<String, Integer> ret = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while (line != null) {
			String[] l = line.split(" ");
			if (l.length == 2) {
				ret.put(l[0], Integer.parseInt(l[1]));
			}
			line = br.readLine();
		}
		br.close();
		return ret;
	}

	@Override
	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle() {
		return this.partOfSpeechStyle;
	}

	@Override
	public boolean tag(final BaseSentence sentence) {
		String idKey = Constants.DATA_KEY_ID;
		if (sentence.data.containsKey(idKey)) {
			String sentenceId = sentence.data.get(idKey);
			if (index.containsKey(sentenceId)) {
				return tagger[index.get(sentenceId)].tag(sentence);
			} else {
				return tagger[folds].tag(sentence);
			}
		}
		idKey = Constants.DATA_KEY_UIMAID;
		if (sentence.data.containsKey(idKey)) {
			String sentenceId = sentence.data.get(idKey);
			if (index.containsKey(sentenceId)) {
				return tagger[index.get(sentenceId)].tag(sentence);
			} else {
				return tagger[folds].tag(sentence);
			}
		}
		return false;
	}

}
