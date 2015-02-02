package de.nilsreiter.segmentation.evaluation;

import java.io.IOException;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.uniheidelberg.cl.a10.Main;

public class EvaluateSegmentation extends Main {

	EvaluationOptions options = new EvaluationOptions();

	public static void main(String[] args) throws UIMAException, IOException {
		EvaluateSegmentation es = new EvaluateSegmentation();
		es.processArguments(args, es.options);
		es.run();
	}

	public void run() throws UIMAException, IOException {
		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory.createTypeSystemDescription();
		JCas silverJCas =
				JCasFactory.createJCas(options.getSilverFile()
						.getAbsolutePath(), tsd);
		JCas goldJCas =
				JCasFactory.createJCas(options.getGoldFile().getAbsolutePath(),
						tsd);

		Class<? extends Annotation> annoType = SegmentBoundary.class;
		Metric metric = null;
		switch (options.getMetric()) {
		case BreakDiff:
			metric = MetricFactory.getMetric(BreakDifference.class, annoType);
			break;
		default:
			metric = MetricFactory.getMetric(WindowDifference.class, annoType);
		}
		Map<String, Double> score = null;
		if (metric.init(goldJCas)) score = metric.score(goldJCas, silverJCas);
		System.out.println(score.toString());

	}
}
