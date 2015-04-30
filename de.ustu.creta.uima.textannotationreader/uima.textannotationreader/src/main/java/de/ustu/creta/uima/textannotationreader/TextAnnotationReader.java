package de.ustu.creta.uima.textannotationreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class TextAnnotationReader extends JCasAnnotator_ImplBase {
	public static final String PARAM_DIRECTORY_NAME = "Directory Name";
	public static final String PARAM_FILE_SUFFIX = "File Suffix";
	public static final String PARAM_ANNOTATION_TYPE = "Annotation Type";

	@ConfigurationParameter(name = PARAM_DIRECTORY_NAME)
	String directoryPathName;

	@ConfigurationParameter(name = PARAM_FILE_SUFFIX, mandatory = false)
	String fileSuffix = ".txt";

	@ConfigurationParameter(name = PARAM_ANNOTATION_TYPE)
	String annotationClass = null;

	File directory;

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		directory = new File(this.directoryPathName);
		if (!directory.isDirectory())
			throw new ResourceInitializationException(new IOException(
					directory.getName() + " is not a directory."));

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		DocumentMetaData dmd =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class);
		String id = dmd.getDocumentId();
		// id = id.substring(0, id.indexOf("."));
		File file = new File(directory, id + fileSuffix);

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

		List<String> annoTokens = new LinkedList<String>();
		Pattern pattern = Pattern.compile("\\b[\\w]\\b");

		Matcher matcher = pattern.matcher(contents);
		while (matcher.find()) {
			annoTokens.add(matcher.group(0));
		}
	}

}
