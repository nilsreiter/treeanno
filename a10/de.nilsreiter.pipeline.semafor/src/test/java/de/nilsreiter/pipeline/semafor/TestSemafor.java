package de.nilsreiter.pipeline.semafor;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ART;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PR;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PUNC;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.V;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.DET;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.DOBJ;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.NSUBJ;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.PUNCT;

public class TestSemafor {

	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barked. It chased the mother.");
		jcas.setDocumentLanguage("en");

		Sentence sentence = new Sentence(jcas);
		sentence.setBegin(0);
		sentence.setEnd(15);
		sentence.addToIndexes();

		Token[] token = new Token[4];
		token[0] = getToken(0, 3, "DT", "the", ART.class);
		token[1] = getToken(4, 7, "NN", "dog", NN.class);
		token[2] = getToken(8, 14, "VBD", "bark", V.class);
		token[3] = getToken(14, 15, ".", ".", PUNC.class);

		Dependency dep;
		dep = createAnnotation(jcas, 0, 3, DET.class);
		dep.setDependencyType("det");
		dep.setGovernor(token[1]);

		dep = createAnnotation(jcas, 4, 7, NSUBJ.class);
		dep.setDependencyType("nsubj");
		dep.setGovernor(token[2]);

		dep = createAnnotation(jcas, 14, 15, PUNCT.class);
		dep.setDependencyType("punct");
		dep.setGovernor(token[2]);

		sentence = createAnnotation(jcas, 16, 37, Sentence.class);

		token = new Token[5];
		token[0] = getToken(16, 18, "PRP", "it", PR.class);
		token[1] = getToken(19, 25, "VBD", "chase", V.class);
		token[2] = getToken(26, 29, "DT", "the", ART.class);
		token[3] = getToken(30, 36, "NN", "mother", NN.class);
		token[4] = getToken(36, 37, ".", ".", PUNC.class);

		dep = createAnnotation(jcas, 16, 18, NSUBJ.class);
		dep.setDependencyType("nsubj");
		dep.setGovernor(token[1]);

		dep = createAnnotation(jcas, 26, 29, DET.class);
		dep.setDependencyType("det");
		dep.setGovernor(token[3]);

		dep = createAnnotation(jcas, 30, 36, DOBJ.class);
		dep.setDependencyType("dobj");
		dep.setGovernor(token[1]);

		dep = createAnnotation(jcas, 36, 37, PUNCT.class);
		dep.setDependencyType("punct");
		dep.setGovernor(token[1]);
	}

	protected Token getToken(int begin, int end, String posValue,
			String lemmaString, Class<? extends POS> posClass) {
		Token token;
		POS pos;
		Lemma lemma;

		pos = createAnnotation(jcas, begin, end, posClass);
		pos.setPosValue(posValue);

		lemma = createAnnotation(jcas, begin, end, Lemma.class);
		lemma.setValue(lemmaString);

		token = createAnnotation(jcas, begin, end, Token.class);
		token.setPos(pos);
		token.setLemma(lemma);

		return token;
	}

	@Test
	public void testInitialJCas() {
		Token token;

		token = JCasUtil.selectByIndex(jcas, Token.class, 0);
		assertEquals("The", token.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 1);
		assertEquals("dog", token.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 2);
		assertEquals("barked", token.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 3);
		assertEquals(".", token.getCoveredText());

		Sentence sentence;
		sentence = JCasUtil.selectByIndex(jcas, Sentence.class, 0);
		assertEquals("The dog barked.", sentence.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 4);
		assertEquals("It", token.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 5);
		assertEquals("chased", token.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 6);
		assertEquals("the", token.getCoveredText());

		token = JCasUtil.selectByIndex(jcas, Token.class, 7);
		assertEquals("mother", token.getCoveredText());
		token = JCasUtil.selectByIndex(jcas, Token.class, 8);
		assertEquals(".", token.getCoveredText());

		sentence = JCasUtil.selectByIndex(jcas, Sentence.class, 1);
		assertEquals("It chased the mother.", sentence.getCoveredText());

	}

	@Test
	public void testSemafor() throws ResourceInitializationException,
	AnalysisEngineProcessException {
		AnalysisEngineDescription aed =
				createEngineDescription(Semafor.class, Semafor.PARAM_MODEL,
						"/Users/reiterns/Documents/Resources/semafor-model/0");
		SimplePipeline.runPipeline(jcas, aed);
		assertEquals(3, JCasUtil.select(jcas, SemanticPredicate.class).size());
		SemanticPredicate predicate;
		predicate = JCasUtil.selectByIndex(jcas, SemanticPredicate.class, 0);
		assertEquals("Communication_noise", predicate.getCategory());
		assertEquals(8, predicate.getBegin());
		assertEquals(14, predicate.getEnd());

		predicate = JCasUtil.selectByIndex(jcas, SemanticPredicate.class, 1);
		assertEquals("Cotheme", predicate.getCategory());
		assertEquals(19, predicate.getBegin());
		assertEquals(25, predicate.getEnd());

		predicate = JCasUtil.selectByIndex(jcas, SemanticPredicate.class, 2);
		assertEquals("Kinship", predicate.getCategory());
		assertEquals(30, predicate.getBegin());
		assertEquals(36, predicate.getEnd());
	}
}
