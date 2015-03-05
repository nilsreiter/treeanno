package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Adds annotation type ClauseStart
 * 
 * @author reiterns
 *
 */
@TypeCapability(
		inputs = {
				"de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause",
				"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" },
		outputs = { "de.nilsreiter.pipeline.segmentation.clauselevel.type.ClauseStart" })
public class PrepareClauseStartAnnotations extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Clause clause : JCasUtil.select(jcas, Clause.class)) {
			if (clause.getExtent() != null && clause.getExtent().size() > 0) {
				FeatureStructure[] fsarray = clause.getExtent().toArray();
				Token[] array = new Token[fsarray.length];
				for (int i = 0; i < fsarray.length; i++)
					array[i] = (Token) fsarray[i];
				Arrays.sort(array, new Comparator<Token>() {
					public int compare(Token o1, Token o2) {
						return Integer.compare(o1.getBegin(), o2.getBegin());
					}
				});
				AnnotationFactory.createAnnotation(jcas, array[0].getBegin(),
						array[0].getEnd(), ClauseStart.class);
			}
		}
	}
}
