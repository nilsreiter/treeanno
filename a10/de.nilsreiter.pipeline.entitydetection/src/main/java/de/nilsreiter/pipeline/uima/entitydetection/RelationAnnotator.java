package de.nilsreiter.pipeline.uima.entitydetection;

import java.util.Collection;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention;
import de.nilsreiter.pipeline.uima.entitydetection.type.Relation;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class RelationAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			Collection<EntityMention> entities =
					JCasUtil.selectCovered(jcas, EntityMention.class, sentence);
			if (entities.size() > 1) {
				for (EntityMention ent1 : entities) {
					for (EntityMention ent2 : entities) {
						processPair(jcas, ent1, ent2);
					}
				}
			}
		}
	}

	protected void processPair(JCas jcas, EntityMention entity1,
			EntityMention entity2) {
		if (entity1.getBegin() == entity2.getBegin()
				&& entity1.getEnd() == entity2.getEnd()) return;

		Relation rel = new Relation(jcas);
		rel.setArguments(new FSArray(jcas, 2));
		rel.setArguments(0, entity1);
		rel.setArguments(1, entity2);
		try {
			Token relationToken =
					RelationUtil.getLCS(jcas, entity1.getHead(),
							entity2.getHead());
			rel.setName(relationToken.getLemma().getValue());
		} catch (NullPointerException e) {
			rel.setName("UNKNOWN");
		}
		rel.addToIndexes();

	}

}
