package de.nilsreiter.pipeline.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.pdfbox.io.IOUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class OCRCorrectedExport extends JCasConsumer_ImplBase {

	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	File outputDirectory;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		StringBuilder text = new StringBuilder(jcas.getDocumentText());

		TreeSet<OCRCorrection> corrs =
				new TreeSet<OCRCorrection>(new Comparator<OCRCorrection>() {

					@Override
					public int compare(OCRCorrection o1, OCRCorrection o2) {
						return -1 * Integer.compare(o1.getEnd(), o2.getEnd());
					}
				});
		for (OCRCorrection correction : JCasUtil.select(jcas,
				OCRCorrection.class)) {
			corrs.add(correction);
		}

		for (OCRCorrection correction : corrs) {
			text = process(jcas, correction, text);
		}

		String docId =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class)
						.getDocumentId();

		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(outputDirectory, docId + ".txt"));
			fw.write(text.toString());
			fw.close();
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		} finally {
			IOUtils.closeQuietly(fw);
		}

	}

	protected StringBuilder process(JCas jcas, OCRCorrection correction,
			StringBuilder text) {
		Collection<OCRCorrection> covering =
				JCasUtil.selectCovering(jcas, OCRCorrection.class, correction);
		return text.replace(correction.getBegin(), correction.getEnd(),
				correction.getCorrection());
	};
}
