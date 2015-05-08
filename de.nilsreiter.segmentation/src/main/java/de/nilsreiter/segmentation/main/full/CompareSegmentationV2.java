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

import de.nilsreiter.pipeline.PipelineBuilder;
import de.nilsreiter.pipeline.segmentation.SegmentationUnitAnnotator;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.ustu.creta.segmentation.agreement.CohensKappa;
import de.ustu.creta.segmentation.agreement.impl.CohensKappa_impl;
import de.ustu.creta.segmentation.evaluation.BoundarySimilarity;
import de.ustu.creta.segmentation.evaluation.FournierMetric;
import de.ustu.creta.segmentation.evaluation.FournierMetric.Transposition;
import de.ustu.creta.segmentation.evaluation.FournierMetric.TranspositionWeightingFunction;
import de.ustu.creta.segmentation.evaluation.MetricFactory;
import de.ustu.creta.segmentation.evaluation.util.SegmentBoundaryAnnotator;

public class CompareSegmentationV2 {

	static Class<? extends Annotation> boundaryType =
			de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1.class;

	static AnalysisEngineDescription[][] pipeline;
	static List<AnalysisEngineDescription> pl;
	static FournierMetric metric;
	static CohensKappa agr;

	static TypeSystemDescription tsd;

	// static boolean level1 = true, level2 = true, level3 = true;

	public static void main(String[] args) throws UIMAException, IOException {
		File inputDirectory1 =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_1/DW/xmi");
		File inputDirectory2 =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_1/HF/xmi");

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
		pl = new LinkedList<AnalysisEngineDescription>();
		pl.add(AnalysisEngineFactory.createEngineDescription(
				SegmentationUnitAnnotator.class,
				SegmentationUnitAnnotator.PARAM_BASE_TYPE,
				"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token"));
		pl.add(AnalysisEngineFactory
				.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1.class
						.getCanonicalName()));
		pipeline = new AnalysisEngineDescription[3][];
		pipeline[0] = PipelineBuilder.array(pl);
		pl.add(AnalysisEngineFactory
				.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel2.class
						.getCanonicalName()));
		pipeline[1] = PipelineBuilder.array(pl);
		pl.add(AnalysisEngineFactory
				.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel3.class
						.getCanonicalName()));
		// pl.add(AnalysisEngineFactory
		// .createEngineDescription(CorpusStatistics.class));
		pipeline[2] = PipelineBuilder.array(pl);

		tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(
						inputDirectory1, "typesystem.xml").toURI()
						.toString());

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
		System.out.println("--------");
		for (File file1 : inputDirectory1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		})) {
			JCas jcas1 = JCasFactory.createJCas(file1.getAbsolutePath(), tsd);
			Formatter pf = new Formatter();
			int tokens = JCasUtil.select(jcas1, Token.class).size();
			int sentences = JCasUtil.select(jcas1, Sentence.class).size();
			pf.format("%1$s\t%2$d\t%3$d\t%4$4.1f",
					file1.getName().replaceAll(".xml.txt.xmi", ""), tokens,
					sentences, ((double) tokens / (double) sentences));
			System.out.println(pf.toString());
			pf.close();
		}

	}

	public static void processFiles(File file1, File file2)
			throws UIMAException, IOException {

		double[] agre = new double[3];
		JCas jcas1 = JCasFactory.createJCas(file1.getAbsolutePath(), tsd);
		JCas jcas2 = JCasFactory.createJCas(file2.getAbsolutePath(), tsd);

		SimplePipeline.runPipeline(jcas1, pipeline[0]);
		SimplePipeline.runPipeline(jcas2, pipeline[0]);
		agre[0] = agr.agr(jcas1, jcas2);

		SimplePipeline.runPipeline(jcas1, pipeline[1]);
		SimplePipeline.runPipeline(jcas2, pipeline[1]);
		agre[1] = agr.agr(jcas1, jcas2);

		SimplePipeline.runPipeline(jcas1, pipeline[2]);
		SimplePipeline.runPipeline(jcas2, pipeline[2]);
		agre[2] = agr.agr(jcas1, jcas2);

		Formatter pf = new Formatter();

		pf.format("%1$s\t%2$7.5f\t%3$7.5f\t%4$7.5f", file1.getName()
				.replaceAll(".xml.txt.xmi", ""), agre[0], agre[1], agre[2]);
		System.out.println(pf.toString());
		pf.close();

	}
}
