package de.nilsreiter.pipeline.uima.entitydetection;

import java.util.Collection;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.entitydetection.type.Entity;
import de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class EntityAnnotator2 extends JCasAnnotator_ImplBase {

	boolean useNamedEntities = false;
	boolean useCoreferenceChains = true;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		if (useNamedEntities)
			for (NamedEntity ne : JCasUtil.select(jcas, NamedEntity.class)) {
				Token headToken =
						RelationUtil.getHighestToken(jcas,
								JCasUtil.selectCovered(jcas, Token.class, ne));
				EntityMention entity =
						AnnotationFactory.createAnnotation(jcas, ne.getBegin(),
								ne.getEnd(), EntityMention.class);
				entity.setIdentifier(ne.getCoveredText());
				entity.setSource(ne);
				entity.setHead(headToken);
			}

		// processing coreference chains
		int chainNumber = 0;
		for (CoreferenceChain chain : JCasUtil.select(jcas,
				CoreferenceChain.class)) {
			CoreferenceLink ne = chain.getFirst();
			Entity ent = new Entity(jcas);
			ent.setIdentifier("entity" + chainNumber);
			// String entityLabel = "";
			do {
				EntityMention entityMention =
						AnnotationFactory.createAnnotation(jcas, ne.getBegin(),
								ne.getEnd(), EntityMention.class);
				entityMention.setIdentifier("chain" + chainNumber);
				entityMention.setSource(chain);
				entityMention.setHead(RelationUtil.getHighestToken(jcas,
						JCasUtil.selectCovered(jcas, Token.class, ne)));
				Collection<NamedEntity> nes =
						JCasUtil.selectCovered(jcas, NamedEntity.class, ne);

				if (nes.size() > 0 && ent.getEntityType() == null) {
					ent.setEntityType(nes.iterator().next().getValue());
					ent.setName(nes.iterator().next().getCoveredText());
				}
				entityMention.setEntity(ent);
				// if (entityMention.getCoveredText().length() > entityLabel
				// .length())
				// / entityLabel = entityMention.getCoveredText();

			} while ((ne = ne.getNext()) != null);
			// ent.setName(entityLabel);
			ent.addToIndexes();
			chainNumber++;
		}

	}

}
