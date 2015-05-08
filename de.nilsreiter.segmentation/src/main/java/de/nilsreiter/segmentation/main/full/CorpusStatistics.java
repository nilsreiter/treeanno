package de.nilsreiter.segmentation.main.full;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
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
import org.apache.uima.resource.metadata.TypeSystemDescription;

import de.nilsreiter.pipeline.PipelineBuilder;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.ustu.creta.segmentation.evaluation.util.SegmentBoundaryAnnotator;

public class CorpusStatistics {
	static TypeSystemDescription tsd;
	static AnalysisEngineDescription[] pipeline;
	static List<AnalysisEngineDescription> pl;

	public static void main(String[] args) throws UIMAException, IOException {
		File inputDirectory1 =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_1/DW/xmi");
		File inputDirectory2 =
				new File(
						"/Users/reiterns/Documents/SegNarr/Annotationspaket_1/HF/xmi");

		tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(
						inputDirectory1, "typesystem.xml").toURI()
						.toString());
		pl = new LinkedList<AnalysisEngineDescription>();

		pl.add(AnalysisEngineFactory
				.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1.class
						.getCanonicalName()));
		pl.add(AnalysisEngineFactory
				.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel2.class
						.getCanonicalName()));
		pl.add(AnalysisEngineFactory
				.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_ANNOTATION_TYPE,
						de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel3.class
						.getCanonicalName()));
		// pl.add(AnalysisEngineFactory
		// .createEngineDescription(CorpusStatistics.class));
		pipeline = PipelineBuilder.array(pl);

		int boundaryOnSentence[] = new int[2];
		int boundaries[] = new int[2];
		for (File file1 : inputDirectory1.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		})) {
			String name = file1.getName();
			File file2 = new File(inputDirectory2, name);
			JCas[] jcas =
					new JCas[] {
					JCasFactory
					.createJCas(file1.getAbsolutePath(), tsd),
					JCasFactory
					.createJCas(file2.getAbsolutePath(), tsd) };
			Formatter pf = new Formatter();
			int tokens = JCasUtil.select(jcas[0], Token.class).size();
			int numberOfSentences =
					JCasUtil.select(jcas[0], Sentence.class).size();
			pf.format("%1$s\t%2$d\t%3$d\t%4$4.1f",
					file1.getName().replaceAll(".xml.txt.xmi", ""), tokens,
					numberOfSentences,
					((double) tokens / (double) numberOfSentences));
			System.out.println(pf.toString());
			pf.close();

			SimplePipeline.runPipeline(jcas[0], pipeline);

			for (SegmentBoundary sb : JCasUtil.select(jcas[0],
					SegmentBoundary.class)) {
				boundaries[0]++;
				Collection<Sentence> sentences =
						JCasUtil.selectCovering(jcas[0], Sentence.class, sb);
				for (Sentence sentence : sentences) {
					if (sb.getBegin() == sentence.getBegin())
						boundaryOnSentence[0]++;
				}
			}

			SimplePipeline.runPipeline(jcas[1], pipeline);
			for (SegmentBoundary sb : JCasUtil.select(jcas[1],
					SegmentBoundary.class)) {
				boundaries[1]++;
				Collection<Sentence> sentences =
						JCasUtil.selectCovering(jcas[1], Sentence.class, sb);
				for (Sentence sentence : sentences) {
					if (sb.getBegin() == sentence.getBegin())
						boundaryOnSentence[1]++;
				}
			}

		}
		System.out.println("boundaryOnSentence[0] = " + boundaryOnSentence[0]);
		System.out.println("boundaries[0] = " + boundaries[0]);
		System.out.println("boundaryOnSentence[1] = " + boundaryOnSentence[1]);
		System.out.println("boundaries[1] = " + boundaries[1]);
	}
}
