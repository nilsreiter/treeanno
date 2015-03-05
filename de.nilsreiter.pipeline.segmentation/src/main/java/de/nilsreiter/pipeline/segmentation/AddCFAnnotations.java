package de.nilsreiter.pipeline.segmentation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

@TypeCapability(
		inputs = {
				"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence",
				"de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData" },
		outputs = { "de.nilsreiter.pipeline.segmentation.type.SegmentBoundary" })
public class AddCFAnnotations extends JCasAnnotator_ImplBase {

	public static final String PARAM_INPUT_DIRECTORY = "Input Directory";
	public static final String PARAM_FIXATION = "Fixation";
	public static final String PARAM_THRESHOLD = "Threshold";

	@ConfigurationParameter(name = PARAM_INPUT_DIRECTORY)
	String inputDirectory;

	@ConfigurationParameter(name = PARAM_FIXATION, mandatory = false)
	int fixation = -11;

	@ConfigurationParameter(name = PARAM_THRESHOLD, mandatory = false)
	float threshold = 0.0f;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String docId =
				JCasUtil.selectSingle(jcas, DocumentMetaData.class)
						.getDocumentId();
		if (docId.endsWith(".txt"))
			docId = docId.substring(0, docId.length() - 4);

		File iFile = new File(inputDirectory, docId + ".csv");
		if (!(iFile.exists() && iFile.canRead())) return;

		List<Sentence> sentences =
				new LinkedList<Sentence>(JCasUtil.select(jcas, Sentence.class));
		try {
			CSVParser parser =
					new CSVParser(new FileReader(iFile), CSVFormat.RFC4180);
			for (CSVRecord csvRecord : parser) {
				if (csvRecord.getRecordNumber() > 1) {
					int id = Integer.valueOf(csvRecord.get(7));
					String sentenceSurface = csvRecord.get(10);
					Double confidence = Double.valueOf(csvRecord.get(6));
					String clazz = csvRecord.get(5);

					if (clazz.equalsIgnoreCase("starts a new new unit")
							&& confidence > threshold) {
						Sentence sent = sentences.get(id + fixation);
						getLogger().info(
								"UIMA sentence: " + sent.getCoveredText()
										+ "\nCSV sentence: " + sentenceSurface);

						AnnotationFactory.createAnnotation(jcas,
								sent.getBegin(), sent.getEnd(),
								SegmentBoundary.class)
								.setConfidence(confidence);
					}
				}
			}
			parser.close();
		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
}
