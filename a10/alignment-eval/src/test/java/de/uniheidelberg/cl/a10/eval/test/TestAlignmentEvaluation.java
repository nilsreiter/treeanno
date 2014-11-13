package de.uniheidelberg.cl.a10.eval.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentReader;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluation;
import de.uniheidelberg.cl.a10.eval.Evaluation;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.eval.Style;

public class TestAlignmentEvaluation {
	AlignmentReader<Token> realAReader;

	@Before
	public void setUp() throws Exception {
		realAReader = new AlignmentReader<Token>(new File("data2/gold"));
	}

	@Test
	public void testRealEvaluation() throws ParserConfigurationException,
			SAXException, IOException {
		Alignment<Token> gold =
				realAReader.read(new File("tasks/alignment/gold-3-9-16.xml"));
		Alignment<Token> silver =
				realAReader.read(new File("tasks/alignment/gold-3-9-16.xml"));
		AlignmentEvaluation<Token> eval =
				Evaluation.getAlignmentEvaluation(Style.REITER);
		SingleResult ss = eval.evaluate(gold, silver, null);
		assertEquals(1.0, ss.p(), 0.001);
		assertEquals(1.0, ss.r(), 0.001);
	}

	@Test
	public void testNAryGold() throws IOException,
			ParserConfigurationException, SAXException {
		Alignment<Token> gold =
				realAReader.read(new File("tasks/alignment/gold-3-9-16.xml"));
		AlignmentEvaluation<Token> eval =
				Evaluation.getAlignmentEvaluation(Style.REITER);
		SingleResult ss = eval.evaluate(gold, gold, null);
		assertEquals(1.0, ss.p(), 0.001);
		assertEquals(1.0, ss.r(), 0.001);

	}

}
