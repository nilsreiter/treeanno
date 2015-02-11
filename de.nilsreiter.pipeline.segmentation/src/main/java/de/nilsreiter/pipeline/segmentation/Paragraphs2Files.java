package de.nilsreiter.pipeline.segmentation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

public class Paragraphs2Files extends JCasConsumer_ImplBase {
	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	String outputDirectory;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		int i = 0;
		File outputDir = new File(outputDirectory);
		if (!outputDir.exists()) outputDir.mkdirs();
		for (Paragraph paragraph : JCasUtil.select(jcas, Paragraph.class)) {
			File oFile = new File(outputDir, String.valueOf(i++));
			try {
				FileWriter fw = new FileWriter(oFile);
				fw.write(paragraph.getCoveredText());
				fw.close();
			} catch (IOException e) {
				throw new AnalysisEngineProcessException(e);
			}
		}
	}

}
