package de.nilsreiter.pipeline.segmentation;

import java.io.File;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class GoldSegmentAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_ANNOTATIONS_DIRECTORY =
			"Annotations Directory";

	@ConfigurationParameter(name = PARAM_ANNOTATIONS_DIRECTORY)
	File annotationDirectory;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		DocumentMetaData meta =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class);
		File annoFile =
				new File(annotationDirectory, meta.getDocumentId() + ".xml");
		if (annoFile.exists() && annoFile.canRead()) {
			Builder xBuilder = new Builder();

			try {
				Document doc;
				doc = xBuilder.build(annoFile);

				Elements breakElements =
						doc.getRootElement().getChildElements("b");
				JCas goldView = jcas.createView("gold");
				goldView.setSofaDataString(jcas.getDocumentText(), "text/plain");
				for (int i = 0; i < breakElements.size(); i++) {
					Element elem = breakElements.get(i);
					int pos =
							Integer.valueOf(elem.getAttributeValue("position"));
					AnnotationFactory.createAnnotation(goldView, pos, pos + 1,
							SegmentBoundary.class);
				}

			} catch (CASException e) {
				e.printStackTrace();
			} catch (ValidityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
