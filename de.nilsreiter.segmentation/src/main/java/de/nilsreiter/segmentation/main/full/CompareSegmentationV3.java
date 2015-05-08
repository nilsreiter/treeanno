package de.nilsreiter.segmentation.main.full;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import webanno.custom.Zusammenfassung;
import de.nilsreiter.pipeline.PipelineBuilder;
import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.ustu.creta.segmentation.agreement.CohensKappa;
import de.ustu.creta.segmentation.agreement.impl.CohensKappa_impl;
import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;
import de.ustu.creta.segmentation.evaluation.FournierMetric;
import de.ustu.creta.segmentation.evaluation.FournierMetric.Transposition;
import de.ustu.creta.segmentation.evaluation.FournierMetric.TranspositionWeightingFunction;
import de.ustu.creta.segmentation.evaluation.MetricFactory;

public class CompareSegmentationV3 {

	static Class<? extends Annotation> boundaryType =
			de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1.class;

	static AnalysisEngineDescription[] pipeline;

	static FournierMetric metric;
	static CohensKappa agr;
	static boolean level1 = true, level2 = false, level3 = false;

	public static void main(String[] args) throws UIMAException, IOException {
		File inputDirectory1 =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_2/DW/xmi");
		File inputDirectory2 =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_2/HF/xmi");

		metric =
				MetricFactory.getMetric(BoundarySimilarity.class,
						SegmentBoundary.class);
		metric.setWindowSize(44);
		metric.setTranspositionPenaltyFunction(new TranspositionWeightingFunction() {

			public double getWeight(Transposition tp) {
				return 0;
			}

		});
		agr = new CohensKappa_impl();
		agr.setObservedAgreementMetric(metric);
		List<AnalysisEngineDescription> pl =
				new LinkedList<AnalysisEngineDescription>();
		pl.add(AnalysisEngineFactory.createEngineDescription(
				SegmentationUnitAnnotator.class,
				SegmentationUnitAnnotator.PARAM_BASE_TYPE,
				"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token"));

		pipeline = PipelineBuilder.array(pl);

		for (File file1 : inputDirectory1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		})) {
			String name = file1.getName();
			File file2 = new File(inputDirectory2, name);

			if (file2.exists()) {
				try {
					processFiles(file1, file2);
				} catch (java.lang.ArrayIndexOutOfBoundsException e) {

				}
			}
		}

	}

	public static void processFiles(File file1, File file2)
			throws UIMAException, IOException {
		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
						.createTypeSystemDescriptionFromPath(new File(file1
								.getParentFile(), "typesystem.xml").toURI()
								.toString());

		JCas jcas1 = JCasFactory.createJCas(file1.getAbsolutePath(), tsd);
		JCas jcas2 = JCasFactory.createJCas(file2.getAbsolutePath(), tsd);

		if (false) {
			for (Zusammenfassung zus : JCasUtil.select(jcas1,
					Zusammenfassung.class)) {
				System.out.println(zus.getBegin() + "," + zus.getEnd() + ": "
						+ zus.getZusammenfassung());
			}

			for (Zusammenfassung zus : JCasUtil.select(jcas2,
					Zusammenfassung.class)) {
				System.out.println(zus.getBegin() + "," + zus.getEnd() + ": "
						+ zus.getZusammenfassung());
			}
		}

		SimplePipeline.runPipeline(jcas1, pipeline);
		SimplePipeline.runPipeline(jcas2, pipeline);

		// double d = metric.score(jcas1, jcas2);
		double a = agr.agr(jcas1, jcas2);
		// double cha = agr.getChanceAgreement(jcas1, jcas2);

		Formatter pf = new Formatter();
		pf.format("%1$s %2$7.5f ",
				file1.getName().replaceAll(".xml.txt.xmi", ""), a);
		System.out.println(pf.toString());
		pf.close();

	}
}