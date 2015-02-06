package de.nilsreiter.pipeline.segmentation.topicmodeling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicAssignment;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;
import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;

public class NarrativeDisentanglement extends JCasAnnotator_ImplBase {

	int k = 3;

	int l = 100;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new CharSequenceLowercase());
		pipeList.add(new CharSequence2TokenSequence(Pattern
				.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequence2FeatureSequence());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		instances.addThruPipe(new SegmentationUnitIterator(aJCas, JCasUtil
				.iterator(aJCas, SegmentationUnit.class)));

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		int numTopics = 3;
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		getLogger().info("Adding " + instances.size() + " instances.");
		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only,
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(100);
		try {
			getLogger().log(Level.INFO, "Now estimating parameters.");
			model.estimate();

			// The data alphabet maps word IDs to strings
			// Alphabet dataAlphabet = instances.getDataAlphabet();

			/*
			 * for (int j = 0; j < 10; j++) { FeatureSequence tokens =
			 * (FeatureSequence) model.getData().get(j).instance .getData();
			 * LabelSequence topics = model.getData().get(j).topicSequence;
			 * Formatter out = new Formatter(new StringBuilder(), Locale.US);
			 * for (int position = 0; position < tokens.getLength(); position++)
			 * { out.format("%s-%d ", dataAlphabet.lookupObject(tokens
			 * .getIndexAtPosition(position)), topics
			 * .getIndexAtPosition(position)); }
			 * 
			 * System.out.println(out); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}
		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();
		Iterator<SegmentationUnit> iterator =
				JCasUtil.iterator(aJCas, SegmentationUnit.class);
		Object[][] topWords = model.getTopWords(l);
		for (int i = 0; i < model.getData().size(); i++) {
			TopicAssignment ta = model.getData().get(i);

			SegmentationUnit current = iterator.next();
			FeatureSequence tokens = (FeatureSequence) ta.instance.getData();
			LabelSequence topics = ta.topicSequence;

			int topic = -1;
			double val = Double.NEGATIVE_INFINITY;
			for (int j = 0; j < topics.getLength(); j++) {
				if (topics.getLabelAtPosition(j).getBestValue() > val) {
					val = topics.getLabelAtPosition(j).getBestValue();
					topic = j;
				}
			}

			if (val > 1.0 / k) {
				boolean contained = false;
				for (int position = 0; position < tokens.getLength(); position++) {
					String w =
							(String) dataAlphabet.lookupObject(tokens
									.getIndexAtPosition(position));
					if (ArrayUtils.contains(topWords[topic], w)) {
						contained = true;
					}
				}
				if (contained) {
					AnnotationFactory
							.createAnnotation(aJCas, current.getBegin(),
									current.getEnd(), Segment.class).setValue(
									String.valueOf(topic));
					System.err.println("Assigning topic " + topic);
				}
			}
		}

	}
}
