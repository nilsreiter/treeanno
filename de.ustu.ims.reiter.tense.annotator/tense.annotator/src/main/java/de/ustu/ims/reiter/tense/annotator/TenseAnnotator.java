package de.ustu.ims.reiter.tense.annotator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.ExternalResourceAware;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.ruta.engine.Ruta;
import org.apache.uima.util.InvalidXMLException;

@TypeCapability(inputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS",
"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" },
outputs = { "de.ustu.ims.reiter.tense.api.type.Tense",
"de.ustu.ims.reiter.tense.api.type.Aspect" })
public class TenseAnnotator extends JCasAnnotator_ImplBase {

	public static final String RUTA_RULES_PARA = "RutaRules";

	@ExternalResource(key = RUTA_RULES_PARA, mandatory = false)
	RutaRules rr = null;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		if (rr == null) {
			rr = new RutaRules();
			try {
				rr.loadFromStream(getClass().getResourceAsStream("rules.ruta"));
			} catch (IOException e) {
				throw new ResourceInitializationException(e);
			}
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		try {
			Ruta.apply(jcas.getCas(), rr.rules);
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

	public static class RutaRules implements SharedResourceObject,
			ExternalResourceAware {
		String rules;

		public String getResourceName() {
			return "RutaRules";
		}

		public void afterResourcesInitialized()
				throws ResourceInitializationException {}

		public void load(DataResource aData)
				throws ResourceInitializationException {
			try {
				loadFromStream(aData.getInputStream());
			} catch (IOException e) {
				throw new ResourceInitializationException(e);
			}
		}

		public void loadFromStream(InputStream is) throws IOException {
			rules = IOUtils.toString(is, "UTF-8");
		}

	}

}