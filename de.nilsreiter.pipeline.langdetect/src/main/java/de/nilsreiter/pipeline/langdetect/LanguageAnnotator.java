package de.nilsreiter.pipeline.langdetect;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import de.nilsreiter.pipeline.langdetect.type.Language;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class LanguageAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_PROFILES_DIRECTORY = "Profiles Directory";

	@ConfigurationParameter(name = PARAM_PROFILES_DIRECTORY, mandatory = true)
	String profileDirectory = null;

	Set<String> languageCandidates = new HashSet<String>();

	Detector detector;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		languageCandidates.add("de");
		languageCandidates.add("en");
		try {
			DetectorFactory.loadProfile(profileDirectory);
		} catch (LangDetectException e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Token token : JCasUtil.select(jcas, Token.class)) {
			try {
				detector = DetectorFactory.create();
				detector.append(token.getCoveredText());

				com.cybozu.labs.langdetect.Language lang =
						detector.getProbabilities().get(0);

				Language languageAnnotation =
						AnnotationFactory.createAnnotation(jcas,
								token.getBegin(), token.getEnd(),
								Language.class);
				languageAnnotation.setLanguage(lang.lang);
				languageAnnotation.setConfidence(lang.prob);

			} catch (LangDetectException e) {

			}
		}
	}
}
