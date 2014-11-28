package de.nilsreiter.pipeline.uima.entitydetection;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.entitydetection.type.Entity;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class EntityAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (NamedEntity ne : JCasUtil.select(jcas, NamedEntity.class)) {
			Entity entity =
					AnnotationFactory.createAnnotation(jcas, ne.getBegin(),
							ne.getEnd(), Entity.class);
			entity.setIdentifier(ne.getCoveredText());
			entity.setSource(ne);
			entity.setHead(RelationUtil.getHighestToken(jcas,
					JCasUtil.selectCovered(jcas, Token.class, ne)));
		}

		// processing coreference chains
		int chainNumber = 0;
		for (CoreferenceChain chain : JCasUtil.select(jcas,
				CoreferenceChain.class)) {
			CoreferenceLink ne = chain.getFirst();
			do {
				Entity entity =
						AnnotationFactory.createAnnotation(jcas, ne.getBegin(),
								ne.getEnd(), Entity.class);
				entity.setIdentifier("chain" + chainNumber);
				entity.setSource(chain);
				entity.setHead(RelationUtil.getHighestToken(jcas,
						JCasUtil.selectCovered(jcas, Token.class, ne)));

			} while ((ne = ne.getNext()) != null);
			chainNumber++;
		}
	}
}
