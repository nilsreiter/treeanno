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

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
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

		int pos = 0;
		int ind;
		int l = 15;
		String text = jcas.getDocumentText();
		do {
			ind = contents.indexOf("<b", pos);
			char lev;
			if (ind > 0) {
				lev = contents.charAt(ind + 2);
				// String pre =
				// contents.substring((ind - l < 0 ? 0 : ind - l), ind)
				// .trim();
				String post = contents.substring(ind + 4, ind + l).trim();
				// int textPre = text.indexOf(pre);
				int textPost = text.indexOf(post);
				SegmentBoundary sb =
						AnnotationFactory.createAnnotation(jcas, textPost,
								textPost + 1, SegmentBoundary.class);
				if (Character.isDigit(lev))
					sb.setLevel(Character.getNumericValue(lev));
				pos = ind + 1;
			}
		} while (ind > 0);
	}
}
