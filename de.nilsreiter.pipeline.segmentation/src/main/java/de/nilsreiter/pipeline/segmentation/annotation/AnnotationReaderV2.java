package de.nilsreiter.pipeline.segmentation.annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel2;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel3;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class AnnotationReaderV2 extends JCasAnnotator_ImplBase {
	public static final String PARAM_DIRECTORY_NAME = "Directory Name";
	public static final String PARAM_FILE_SUFFIX = "File Suffix";

	@ConfigurationParameter(name = PARAM_DIRECTORY_NAME)
	String directoryPathName;

	@ConfigurationParameter(name = PARAM_FILE_SUFFIX, mandatory = false)
	String fileSuffix = ".txt";

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		DocumentMetaData dmd =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class);
		String id = dmd.getDocumentId();
		id = id.substring(0, id.indexOf("."));
		File dir = new File(this.directoryPathName);
		File file = new File(dir, id + fileSuffix);

		String contents = null;
		if (file.exists() && file.canRead()) {
			try {
				contents = IOUtils.toString(new FileInputStream(file));
			} catch (IOException e) {
				throw new AnalysisEngineProcessException(e);
			}
		} else {
			getLogger().info(file.getName() + " can not be read. Skipping.");
			return;
			// throw new AnalysisEngineProcessException();
		}

		contents = contents.replaceAll("<b1>\\s*<b2>", "<b1>");
		contents = contents.replaceAll("<b2>\\s*<b1>", "<b1>");
		contents = contents.replaceAll("<b1>\\s*<b3>", "<b1>");
		contents = contents.replaceAll("<b3>\\s*<b1>", "<b1>");

		contents = contents.replaceAll("<b3>\\s*<b2>", "<b2>");
		contents = contents.replaceAll("<b2>\\s*<b3>", "<b2>");

		contents = contents.replaceAll("<b1>\\s*<b1>", "<b1>");
		contents = contents.replaceAll("<b2>\\s*<b2>", "<b2>");
		contents = contents.replaceAll("<b3>\\s*<b3>", "<b3>");

		contents = contents.replaceAll("<b4>", "");

		int pos = 0;
		int annoIndex;
		int l = 15;
		String text = jcas.getDocumentText();
		int textIndex = 0;
		int annotationLength = 1;
		do {
			annoIndex = contents.indexOf("<b", pos);
			char lev;
			if (annoIndex > 0) {
				lev = contents.charAt(annoIndex + 2);
				String post =
						contents.substring(annoIndex + 4, annoIndex + l).trim()
						.replaceAll("(<[^>]*>?)", "");
				int beg = (textIndex + 1);
				textIndex = text.indexOf(post, beg);
				int level = Character.getNumericValue(lev);
				if (textIndex > 0)
					switch (lev) {
					case '1':
						AnnotationFactory.createAnnotation(jcas, textIndex,
								textIndex + annotationLength,
								SegmentBoundaryLevel1.class).setLevel(level);
						break;
					case '2':
						AnnotationFactory.createAnnotation(jcas, textIndex,
								textIndex + annotationLength,
								SegmentBoundaryLevel2.class).setLevel(level);
						break;
					case '3':
						AnnotationFactory.createAnnotation(jcas, textIndex,
								textIndex + annotationLength,
								SegmentBoundaryLevel3.class).setLevel(level);
						break;
					}
				else {
					System.err.println(file.getName() + ": text not found: "
							+ post + " (text = "
							+ text.substring(beg, beg + 10));
				}
				pos = annoIndex + 1;
			}
		} while (annoIndex > 0);
	}
}
