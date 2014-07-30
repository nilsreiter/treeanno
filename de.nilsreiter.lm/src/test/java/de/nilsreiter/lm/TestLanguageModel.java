package de.nilsreiter.lm;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import de.nilsreiter.lm.impl.NGramModelTrainer_impl;
import de.nilsreiter.lm.impl.StringIndexer;

public class TestLanguageModel {

	NGramModel<String> lm;
	NGramModelTrainer_impl<String> trainer;

	@Test
	public void testStringModel() {
		trainer = new NGramModelTrainer_impl<String>();
		trainer.setIndexer(new StringIndexer());
		trainer.setN(2);
		lm =
				trainer.train("the dog Charles chases the cat and the mouse ."
						.split(" "));

		assertEquals(0.3, lm.getProbability(Arrays.asList("the")), 1e-3);
		assertEquals(0.3, lm.getProbability(new String[] { "the" }), 1e-3);
		assertEquals(1.0, lm.getProbability("the", Arrays.asList("chases")),
				1e-3);
		assertEquals(0.0, lm.getProbability("cat", Arrays.asList("chases")),
				1e-3);
		assertEquals(0.333, lm.getProbability("cat", Arrays.asList("the")),
				1e-3);

	}
}
