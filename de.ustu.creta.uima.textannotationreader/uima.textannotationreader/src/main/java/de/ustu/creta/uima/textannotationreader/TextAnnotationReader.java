package de.ustu.creta.uima.textannotationreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import neobio.alignment.IncompatibleScoringSchemeException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.alignment.neobio.BasicScoringScheme;
import de.nilsreiter.alignment.neobio.NeedlemanWunsch;
import de.nilsreiter.alignment.neobio.PairwiseAlignment;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class TextAnnotationReader extends JCasAnnotator_ImplBase {
	public static final String PARAM_DIRECTORY_NAME = "Directory Name";
	public static final String PARAM_FILE_SUFFIX = "File Suffix";
	public static final String PARAM_ANNOTATION_TYPE = "Annotation Type";
	public static final String PARAM_ANNOTATION_MARK = "Annotation Mark";

	@ConfigurationParameter(name = PARAM_DIRECTORY_NAME)
	String directoryPathName;

	@ConfigurationParameter(name = PARAM_FILE_SUFFIX, mandatory = false)
	String fileSuffix = ".txt";

	@ConfigurationParameter(name = PARAM_ANNOTATION_TYPE)
	String annotationClassName = null;

	@ConfigurationParameter(name = PARAM_ANNOTATION_MARK)
	String annotationMark;

	File directory;

	Class<? extends Annotation> annoClass;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		directory = new File(this.directoryPathName);
		if (!directory.isDirectory())
			throw new ResourceInitializationException(new IOException(
					directory.getName() + " is not a directory."));
		try {
			Class<?> annoClassObj = Class.forName(annotationClassName);
			if (!Annotation.class.isAssignableFrom(annoClassObj)) {
				throw new ResourceInitializationException();
			}
			annoClass =
					(Class<? extends Annotation>) Class
					.forName(annotationClassName);
		} catch (ClassNotFoundException e) {
			throw new ResourceInitializationException(e);
		}

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

		getLogger().info("Processing document " + id);

		String text = jcas.getDocumentText();

		TokenSequence annoTokens = getTokens(contents);
		TokenSequence targetTokens = getTokens(text);

		NeedlemanWunsch<String> nw =
				new NeedlemanWunsch<String>(new BasicScoringScheme<String>(3,
						-30, -1));
		// nw.setGap("NULL");
		// nw.setGapTag(new IndividualAlignment(AlignmentType.Gap));
		nw.setSequences(annoTokens.getSurfaces(), targetTokens.getSurfaces());
		PairwiseAlignment<String> alignment = null;
		try {
			alignment = nw.computePairwiseAlignment();
		} catch (IncompatibleScoringSchemeException e) {
			throw new AnalysisEngineProcessException(e);
		}
		if (alignment != null) {
			Map<Integer, Integer> map = alignment.getIndexMap1();
			List<Integer> cellar = new LinkedList<Integer>();

			// Iteration over all tokens
			for (int i = 0; i < annoTokens.getSurfaces().size(); i++) {

				// character positions of token in anno-file
				Pair<Integer, Integer> annoCharPos =
						annoTokens.getCharacterPositions().get(i);
				try {
					// getting the context of the token
					String annoTokenWithContext =
							contents.substring(annoCharPos.getLeft() - 1,
									annoCharPos.getRight() + 1);

					if (map.containsKey(i)) {
						for (int c = 0; c < cellar.size(); c++) {
							Pair<Integer, Integer> tPos =
									targetTokens.getCharacterPositions().get(
											map.get(i));
							AnnotationFactory.createAnnotation(jcas,
									tPos.getLeft(), tPos.getRight(), annoClass);
						}
						cellar.clear();
					} else {
						// check if it matches the mark
						if (annoTokenWithContext.equals(annotationMark)) {
							cellar.add(i);
						}
					}
				} catch (StringIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}

			for (int c = 0; c < cellar.size(); c++) {
				AnnotationFactory.createAnnotation(jcas, text.length() - 1,
						text.length(), annoClass);
			}

		}

	}

	protected TokenSequence getTokens(String text) {
		List<Pair<Integer, Integer>> annoTokens =
				new LinkedList<Pair<Integer, Integer>>();
		List<String> annoTokensSurfaces = new LinkedList<String>();
		Pattern pattern =
				Pattern.compile("\\b[\\w\\p{Punct}]+\\b",
						Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			Pair<Integer, Integer> p =
					new ImmutablePair<Integer, Integer>(matcher.start(),
							matcher.end());
			annoTokens.add(p);
			annoTokensSurfaces.add(matcher.group());
		}
		TokenSequence ts = new TokenSequence();
		ts.setCharacterPositions(annoTokens);
		ts.setSurfaces(annoTokensSurfaces);
		return ts;
	}
}
