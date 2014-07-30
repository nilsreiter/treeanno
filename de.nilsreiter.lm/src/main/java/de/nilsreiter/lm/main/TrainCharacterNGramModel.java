package de.nilsreiter.lm.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.args4j.Option;

import de.nilsreiter.lm.NGramModel;
import de.nilsreiter.lm.NGramModelTrainer;
import de.nilsreiter.lm.impl.CharIndexer;
import de.nilsreiter.lm.impl.NGramModelTrainer_impl;
import de.nilsreiter.lm.io.ModelWriter;
import de.uniheidelberg.cl.a10.MainWithIO;

public class TrainCharacterNGramModel extends MainWithIO {
	@Option(name = "-n", usage = "The length of the n-grams")
	int n = 3;

	public static void main(String[] args) throws IOException {
		TrainCharacterNGramModel tcm = new TrainCharacterNGramModel();
		tcm.processArguments(args);
		tcm.run();
	}

	public void run() throws IOException {
		List<Character> inputList = new LinkedList<Character>();
		InputStream is = this.getInputStream();
		while (is.available() > 0) {
			char ch = (char) is.read();
			inputList.add(ch);
		}

		NGramModelTrainer<Character> trainer =
				new NGramModelTrainer_impl<Character>();
		trainer.setIndexer(new CharIndexer());
		trainer.setN(n);

		NGramModel<Character> model = trainer.train(inputList);

		ModelWriter<Character> mw =
				new ModelWriter<Character>(this.getOutputStream());
		mw.write(model);
		mw.close();
	}
}
