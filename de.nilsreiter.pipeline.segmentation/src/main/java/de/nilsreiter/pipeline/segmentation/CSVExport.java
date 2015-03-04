package de.nilsreiter.pipeline.segmentation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class CSVExport extends JCasAnnotator_ImplBase {

	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";
	public static final String PARAM_PREFIX_SIZE = "Prefix Size";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputDirectoryName;

	@ConfigurationParameter(name = PARAM_PREFIX_SIZE)
	int prefixSize = 10;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		LinkedList<Sentence> window = new LinkedList<Sentence>();
		LinkedList<Sentence> pref = new LinkedList<Sentence>();
		LinkedList<Sentence> post = new LinkedList<Sentence>();
		File oFile =
				new File(new File(outputDirectoryName), JCasUtil.selectSingle(
						jcas, DocumentMetaData.class).getDocumentId()
						+ ".csv");
		Writer writer;
		int id = 0;
		try {
			writer = new FileWriter(oFile);
			writer.write("Id,Prefix,Sentence,Postfix\n");
			for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {

				window.add(sentence);

				if (id > (prefixSize * 2 - 1) && window.size() > prefixSize + 1)
					processWindow(window, writer, id);
				if (window.size() > prefixSize * 2 + 1) window.pop();
				id++;
			}

			while (!window.isEmpty()) {
				if (id > prefixSize * 2 && window.size() > prefixSize + 1)
					processWindow(window, writer, id);
				window.pop();
				id++;
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}

	}

	protected void processWindow(List<Sentence> window, Writer os, int id)
			throws IOException {
		int i = 0;
		os.write(String.valueOf(id));
		os.write(",");
		StringBuilder b = new StringBuilder();
		for (; i < prefixSize && i < window.size(); i++) {
			b.append(window.get(i).getCoveredText());
			b.append(" ");
		}
		StringEscapeUtils.escapeCsv(os, clean(b.toString()));
		os.write(",");
		if (i < window.size())
			StringEscapeUtils.escapeCsv(os, clean(window.get(i)
					.getCoveredText()));
		os.write(",");
		b = new StringBuilder();
		for (i++; i < window.size(); i++) {
			b.append(window.get(i).getCoveredText());
			b.append(" ");
		}
		StringEscapeUtils.escapeCsv(os, clean(b.toString()));
		os.write("\n");
	}

	protected String clean(String s) {
		return s.replaceAll("[\\p{Space}]", " ");
	}
}
