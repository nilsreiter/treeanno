package de.nilsreiter.pipeline.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class FilteredTxtExporter extends JCasConsumer_ImplBase {
	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputDirectory;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String documentId =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class)
						.getDocumentId();

		StringBuilder b = new StringBuilder();

		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			if (JCasUtil.selectCovered(NamedEntity.class, sentence).size() > 0) {
				b.append(sentence.getCoveredText());
				b.append("\n\n");
			}
		}
		try {

			FileWriter fw =
					new FileWriter(new File(outputDirectory, documentId
							+ ".txt"));
			fw.write(b.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
}
