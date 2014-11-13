package de.nilsreiter.lm.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.lm.NGramModel;
import de.nilsreiter.lm.impl.NGramModelTrainer_impl;
import de.nilsreiter.lm.impl.StringIndexer;

public class TestNGramModelIO {
	NGramModel<String> lm;
	ModelWriter<String> mw;
	ModelReader<String> mr;

	@Before
	public void setUp() throws Exception {
		NGramModelTrainer_impl<String> trainer =
				new NGramModelTrainer_impl<String>();
		trainer.setIndexer(new StringIndexer());
		trainer.setN(2);
		lm =
				trainer.train("the dog Charles chases the cat and the mouse ."
						.split(" "));

	}

	@Test
	public void test() throws IOException {
		File tempfile = File.createTempFile("ngrammodel", "");
		mw = new ModelWriter<String>(new FileOutputStream(tempfile));
		mr = new ModelReader<String>();

		mw.write(lm);
		NGramModel<String> lm2 = mr.read(tempfile);
		assertEquals(lm.getProbability(Arrays.asList("the")),
				lm2.getProbability(Arrays.asList("the")), 1e-3);
		assertEquals(lm.getProbability(Arrays.asList("the", "dog")),
				lm2.getProbability(Arrays.asList("the", "dog")), 1e-3);
		assertEquals(lm.getSize(), lm2.getSize());

	}
}
