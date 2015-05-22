package de.ustu.ims.reiter.tense.annotator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.ruta.engine.Ruta;
import org.apache.uima.util.InvalidXMLException;

@TypeCapability(inputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS",
		"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" },
		outputs = { "de.nilsreiter.pipeline.tense.type.Tense" })
public class TenseAnnotator extends JCasAnnotator_ImplBase {

	public static final String RUTA_RULES_PARA = "RutaRules";

	@ExternalResource(key = RUTA_RULES_PARA, mandatory = false)
	File rutaRulesF = new File(
			"src/main/resources/de/ustu/ims/reiter/tense/annotator/rules.ruta");
	String rutaRules;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		rutaRulesF =
				new File(
						"src/main/resources/de/ustu/ims/reiter/tense/annotator/rules.ruta");
		try {
			rutaRules =
					org.apache.commons.io.FileUtils.readFileToString(
							rutaRulesF, "UTF-8");
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		try {
			Ruta.apply(jcas.getCas(), rutaRules);
		} catch (InvalidXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}