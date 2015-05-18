package de.nilsreiter.segmentation.main.full;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.ustu.creta.segmentation.evaluation.util.SegmentBoundaryAnnotator;

public class GenerateTikzFigure {
	static TypeSystemDescription tsd;

	public static void main(String[] args) throws UIMAException, IOException {
		File inputFile =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_2/HF/xmi/1009.xml.txt.xmi");
		tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(inputFile
						.getParentFile(), "typesystem.xml").toURI()
						.toString());

		JCas jcas = JCasFactory.createJCas(inputFile.getAbsolutePath(), tsd);

		SimplePipeline.runPipeline(jcas,
				AnalysisEngineFactory
						.createEngineDescription(
								SegmentBoundaryAnnotator.class,
								SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
								webanno.custom.Zusammenfassung.class
										.getCanonicalName()));

		double scale = 0.0003;
		double yvalue = -1.75;
		int length = jcas.getDocumentText().length();

		double ylength = 0.1;

		StringBuilder b = new StringBuilder();
		Formatter formatter = new Formatter(b);
		b.append("\\draw (0,").append(yvalue).append(") -- (")
		.append(length * scale).append(",").append(yvalue)
		.append(");\n");
		for (SegmentBoundary bd : JCasUtil.select(jcas, SegmentBoundary.class)) {
			b.append("\\draw ");
			formatter.format("(%1$1.3f,%2$1.3f)", bd.getBegin() * scale, yvalue
					- ylength);
			b.append(" -- ");
			formatter.format("(%1$1.3f,%2$1.3f)", bd.getBegin() * scale, yvalue
					+ ylength);
			b.append(";\n");

		}

		System.out.println(b.toString());
		formatter.close();
	}
}
