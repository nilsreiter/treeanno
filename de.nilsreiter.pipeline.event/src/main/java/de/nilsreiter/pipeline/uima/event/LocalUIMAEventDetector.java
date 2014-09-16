package de.nilsreiter.pipeline.uima.event;

import de.tudarmstadt.ukp.dkpro.core.api.semantics.type.SemanticPredicate;

public interface LocalUIMAEventDetector {
	public boolean isEvent(SemanticPredicate anchor);

}
