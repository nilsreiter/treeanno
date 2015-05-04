package de.nilsreiter.segmentation.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.io.SegEvalExporter;

public class ExportSegmentation {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException,
	UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);

		Class<? extends Annotation> boundaryType =
				(Class<? extends Annotation>) Class
				.forName("de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel"
						+ options.getBoundaryLevel());

		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(options
						.getInputDir1(), "typesystem.xml").toURI()
								.toString());

		SegEvalExporter sve = new SegEvalExporter();
		AnalysisEngineDescription segBoundAnno =
				AnalysisEngineFactory.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						boundaryType.getCanonicalName());
		AnalysisEngineDescription addSegUnits =
				AnalysisEngineFactory
				.createEngineDescription(
						SegmentationUnitAnnotator.class,
						SegmentationUnitAnnotator.PARAM_BASE_TYPE,
						"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token");

		for (File file1 : options.getInputDir1().listFiles(
				new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".xmi");
					}
				})) {
			JCas jcas1 = JCasFactory.createJCas(file1.getAbsolutePath(), tsd);
			String fname = file1.getName();
			JCas jcas2 =
					JCasFactory.createJCas(new File(options.getInputDir2(),
							fname).getAbsolutePath(), tsd);
			SimplePipeline.runPipeline(jcas1, segBoundAnno, addSegUnits);
			SimplePipeline.runPipeline(jcas2, segBoundAnno, addSegUnits);

			int units1 = JCasUtil.select(jcas1, SegmentationUnit.class).size();
			int units2 = JCasUtil.select(jcas2, SegmentationUnit.class).size();
			if (units1 != units2) {
				System.err
				.println("Different number of potential boundaries. Exiting.");
				System.exit(-1);
			}
			sve.collect(jcas1, jcas2);
			System.err.println("Processed document pair.");

		}

		System.out.println(sve.exportToJSON().toString(3));
	}

	public interface Options {

		@Option
		File getInputDir1();

		@Option
		File getInputDir2();

		@Option(shortName = "bl")
		int getBoundaryLevel();

		@Option(shortName = "pb")
		boolean getPotentialBoundaries();

		@Option
		int getNearMissWindow();

	}

	public static class SegmentBoundaryAnnotator extends JCasAnnotator_ImplBase {

		public static final String PARAM_ANNOTATION_TYPE = "Annotation Type";

		@ConfigurationParameter(name = PARAM_ANNOTATION_TYPE)
		String annotationTypeName = "";
		Class<? extends Annotation> annotationType;

		@Override
		public void process(JCas jcas) throws AnalysisEngineProcessException {
			try {
				Class<?> clazz = Class.forName(annotationTypeName);
				annotationType = (Class<? extends Annotation>) clazz;
			} catch (ClassNotFoundException e) {
				throw new AnalysisEngineProcessException(e);
			}
			for (Annotation anno : JCasUtil.select(jcas, annotationType)) {
				int b = anno.getBegin();
				AnnotationFactory.createAnnotation(jcas, b, b,
						SegmentBoundary.class);
			}
		}

	}
}
