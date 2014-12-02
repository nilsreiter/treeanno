package de.nilsreiter.pipeline.uima.entitydetection;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.entitydetection.type.Entity;
import de.nilsreiter.pipeline.uima.entitydetection.type.EntityMention;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class EntityAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_USE_NAMED_ENTITIES = "USE_NAMED_ENTITIES";
	public static final String PARAM_USE_COREFERENCE_CHAINS =
			"USE_COREFERENCE_CHAINS";

	@ConfigurationParameter(name = PARAM_USE_NAMED_ENTITIES,
			defaultValue = "false")
	boolean useNamedEntities = false;

	@ConfigurationParameter(name = PARAM_USE_COREFERENCE_CHAINS,
			defaultValue = "true")
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

		if (useCoreferenceChains) {
			// processing coreference chains
			int chainNumber = 0;
			for (CoreferenceChain chain : JCasUtil.select(jcas,
					CoreferenceChain.class)) {
				CoreferenceLink ne = chain.getFirst();
				Entity ent = new Entity(jcas);
				String entityLabel = "";
				do {
					EntityMention entity =
							AnnotationFactory.createAnnotation(jcas,
									ne.getBegin(), ne.getEnd(),
									EntityMention.class);
					entity.setIdentifier("chain" + chainNumber);
					entity.setSource(chain);
					entity.setHead(RelationUtil.getHighestToken(jcas,
							JCasUtil.selectCovered(jcas, Token.class, ne)));
					entity.setEntity(ent);
					if (entity.getCoveredText().length() > entityLabel.length())
						entityLabel = entity.getCoveredText();

				} while ((ne = ne.getNext()) != null);
				ent.setName(entityLabel);
				ent.addToIndexes();
				chainNumber++;
			}
		}
	}

}
