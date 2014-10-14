package de.nilsreiter.pipeline.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.langdetect.type.Language;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class TableExporter extends JCasConsumer_ImplBase {

	public static final String PARAM_OUTPUTDIR = "Output Directory";

	public static final String PARAM_SENTENCES = "Mark Sentences";

	@ConfigurationParameter(name = PARAM_OUTPUTDIR)
	File outputDir;

	@ConfigurationParameter(name = PARAM_SENTENCES)
	boolean markSentences = true;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		DocumentMetaData dmd =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class);
		try {
			FileWriter fw =
					new FileWriter(
							new File(this.outputDir, dmd.getDocumentId()));
			for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
				if (markSentences) fw.write("<s>\n");
				for (Token token : JCasUtil.selectCovered(jcas, Token.class,
						sentence)) {
					fw.write(token.getCoveredText());
					fw.write("\t");
					List<Language> langAnnos =
							JCasUtil.selectCovered(jcas, Language.class, token);
					if (langAnnos.size() > 0) {
						fw.write(langAnnos.get(0).getLanguage());
						fw.write("\t");
						fw.write(String.valueOf(langAnnos.get(0)
								.getConfidence()));
					}
					fw.write("\n");
				}
				if (markSentences) fw.write("</s>");
			}
			fw.close();
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
}
