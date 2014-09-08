package de.nilsreiter.ocr.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.pdfbox.io.IOUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class OCRCorrectedExport extends JCasConsumer_ImplBase {

	public static final String PARAM_OUTPUT_DIRECTORY = "Output Directory";

	@ConfigurationParameter(name = PARAM_OUTPUT_DIRECTORY)
	File outputDirectory;

	public String getExportText(JCas jcas) {
		StringBuilder text = new StringBuilder(jcas.getDocumentText());

		// Make a reverse-ordered set of corrections
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
		return text.toString();
	}

	public String getExportText2(JCas jcas) {
		StringBuilder text = new StringBuilder(jcas.getDocumentText());

		Annotation endAnno = new Annotation(jcas);
		endAnno.setBegin(jcas.size());
		endAnno.setEnd(jcas.size());
		// JCasUtil.selectPreceding(jcas, OCRCorrection.class, endAnno, 1);

		OCRCorrection current;

		boolean end = false;
		do {
			try {
				current =
						JCasUtil.selectSingleRelative(jcas,
								OCRCorrection.class, endAnno, -1);
				endAnno = current;

				text = process(jcas, current, text);
			} catch (IndexOutOfBoundsException e) {
				end = true;
			}

		} while (!end);

		return text.toString();
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = this.getExportText(jcas);

		String docId =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class)
						.getDocumentId();

		FileWriter fw = null;
		try {
			if (!outputDirectory.exists()) outputDirectory.mkdirs();

			File oFile = new File(outputDirectory, docId);
			if (oFile.exists()) {
				oFile.delete();
			}
			fw = new FileWriter(oFile);
			fw.write(text);
			fw.close();
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		} finally {
			IOUtils.closeQuietly(fw);
		}

	}

	protected StringBuilder process(JCas jcas, OCRCorrection correction,
			StringBuilder text) {
		return text.replace(correction.getBegin(), correction.getEnd(),
				correction.getCorrection());
	};
}
