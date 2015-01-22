package de.nilsreiter.pipeline.segmentation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentationSubUnit;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

@TypeCapability(
		inputs = {},
		outputs = { "de.nilsreiter.pipeline.segmentation.type.SegmentationSubUnit" })
public class SegmentationSubUnitAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_BASE_TYPE = "Annotation Base Type";

	@ConfigurationParameter(
			name = PARAM_BASE_TYPE,
			mandatory = false,
			defaultValue = "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token")
	String baseAnnotation =
	"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token";

	@SuppressWarnings("unchecked")
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Class<? extends Annotation> annotationType = Token.class;
		try {
			annotationType =
					(Class<? extends Annotation>) Class.forName(baseAnnotation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}
		for (Annotation anno : JCasUtil.select(jcas, annotationType)) {
			AnnotationFactory.createAnnotation(jcas, anno.getBegin(),
					anno.getEnd(), SegmentationSubUnit.class);
		}
	}
}
