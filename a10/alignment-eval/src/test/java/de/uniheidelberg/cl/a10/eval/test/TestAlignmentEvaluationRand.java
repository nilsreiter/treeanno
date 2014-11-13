package de.uniheidelberg.cl.a10.eval.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluation;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.eval.Style;

public class TestAlignmentEvaluationRand {
	Token[] t;

	@Before
	public void setUp() throws Exception {
		t =
				new Token[] { mock(Token.class), mock(Token.class),
				mock(Token.class), mock(Token.class),
				mock(Token.class), mock(Token.class) };
	}

	@Test
	public void testEverythingAligned() {

		Alignment<Token> silver = new Alignment_impl<Token>("silver");
		Alignment<Token> gold = new Alignment_impl<Token>("gold");
		gold.addAlignment("a0", Arrays.asList(t[0], t[1]));
		gold.addAlignment("a1", Arrays.asList(t[2], t[3]));
		gold.addAlignment("a2", Arrays.asList(t[4], t[5]));

		silver.addAlignment("a0", Arrays.asList(t));

		AlignmentEvaluation<Token> eval =
				Evaluation.getAlignmentEvaluation(Style.RAND);
		SingleResult res = eval.evaluate(gold, silver);
		assertEquals(0.2, res.p(), 0.0);

	}

	@Test
	public void testNothingAligned() {

		Alignment<Token> silver = new Alignment_impl<Token>("silver");
		Alignment<Token> gold = new Alignment_impl<Token>("gold");
		gold.addAlignment("a0", Arrays.asList(t[0], t[1]));
		gold.addAlignment("a1", Arrays.asList(t[2], t[3]));
		gold.addAlignment("a2", Arrays.asList(t[4], t[5]));

		AlignmentEvaluation<Token> eval =
				Evaluation.getAlignmentEvaluation(Style.RAND);

		SingleResult res = eval.evaluate(gold, silver);
		assertEquals(0.8, res.p(), 0.0);

	}
}
