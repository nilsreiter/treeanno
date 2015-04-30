package de.nilsreiter.pipeline.segmentation.annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

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

		String text = jcas.getDocumentText();

		List<String> annoTokens = new LinkedList<String>();
		Pattern pattern = Pattern.compile("\\b[\\w]\\b");

		Matcher matcher = pattern.matcher(contents);
		while (matcher.find()) {
			annoTokens.add(matcher.group(0));
		}

		char[] chars = contents.toCharArray();
		int mode = 0;
		StringBuilder b = new StringBuilder();
		Map<Integer, Integer> breaks = new HashMap<Integer, Integer>();
		int currentAnnoStart = -1;
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			switch (ch) {
			case '<':
				mode = 1;
				currentAnnoStart = b.length();
				break;
			case '>':
				mode = 0;
				break;
			case '1':
			case '2':
			case '3':
				if (mode == 1) {
					breaks.put(currentAnnoStart,
							(Character.getNumericValue(ch)));
					break;
				}
			default:
				if (mode == 0) b.append(ch);

			}
		}
		String s = b.toString();
		for (Integer br : breaks.keySet()) {
			String annoText = s.substring(br, br + 10);
			String targetText = text.substring(br, br + 10);
			System.err.println(annoText + " - " + targetText);
		}

		int annoIndex = 0;
		int numberOfMatches = 0;
		int length = 4;
		List<Pair<Integer, Integer>> matches =
				new LinkedList<Pair<Integer, Integer>>();
		do {
			annoIndex = contents.indexOf("<b", annoIndex + 1);
			if (annoIndex > 0) {
				int begin = annoIndex, end = annoIndex + length;
				matches.add(new ImmutablePair<Integer, Integer>(begin, end));
				numberOfMatches++;
			}
		} while (annoIndex > 0);

		String contentsWithoutAnnotation = contents.replaceAll("<b.>", "");
		for (Pair<Integer, Integer> p : matches) {
			System.err.println(p + " "
					+ contents.substring(p.getLeft(), p.getRight() + 4));
		}

	}
}
