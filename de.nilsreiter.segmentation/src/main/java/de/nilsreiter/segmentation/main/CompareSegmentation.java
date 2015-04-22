package de.nilsreiter.segmentation.main;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.segmentation.evaluation.Metric;
import de.nilsreiter.segmentation.evaluation.MetricFactory;

public class CompareSegmentation {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException,
	UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);

		Class<? extends Metric> metricClass;
		Class<?> clazz;
		try {
			clazz = Class.forName(options.getMetric());
		} catch (ClassNotFoundException e) {
			clazz =
					Class.forName("de.nilsreiter.segmentation.evaluation."
							+ options.getMetric());
		}
		metricClass = (Class<? extends Metric>) clazz;
		Metric metric =
				MetricFactory.getMetric(metricClass, SegmentBoundary.class);

		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(new File(options
						.getInputFile1().getParentFile(),
						"typesystem.xml").toURI().toString());

		JCas jcas1 =
				JCasFactory.createJCas(options.getInputFile1()
						.getAbsolutePath(), tsd);

		JCas jcas2 =
				JCasFactory.createJCas(options.getInputFile2()
						.getAbsolutePath(), tsd);

		metric.init(jcas1);
		System.out.println(metric.score(jcas1, jcas2).get(
				metric.getClass().getSimpleName()));
	}

	public interface Options {
		@Option(defaultValue = "FleissKappaBoundarySimilarity")
		String getMetric();

		@Option
		File getInputFile1();

		@Option
		File getInputFile2();
	}
}
