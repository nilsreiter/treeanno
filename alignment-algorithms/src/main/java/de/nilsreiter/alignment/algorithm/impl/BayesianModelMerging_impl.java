package de.nilsreiter.alignment.algorithm.impl;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import de.nilsreiter.alignment.algorithm.BayesianModelMerging;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.FullAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.FullAlignment_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityCalculationException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;
import de.uniheidelberg.cl.a10.patterns.train.BMMFactory;

public class BayesianModelMerging_impl<T extends HasDocument> extends
AbstractAlignmentAlgorithm_impl<T> implements BayesianModelMerging<T> {
	de.uniheidelberg.cl.a10.patterns.train.BayesianModelMerging<T> bmm;

	public BayesianModelMerging_impl(SimilarityFunction<T> sf,
			BMMConfiguration config) throws FileNotFoundException,
			SecurityException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		this.function = sf;
		BMMFactory<T> trainingFactory = new BMMFactory<T>(sf);
		BMMConfiguration bmmc = config;
		bmm = trainingFactory.getTrainer(bmmc);

	}

	public void setLogLevel(Level lvl) {
		bmm.setLogLevel(lvl);
	}

	@Override
	public Alignment<T> align(List<T> list1, List<T> list2) {
		try {
			this.function.sim(list1.get(0), list2.get(0));
		} catch (SimilarityCalculationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<List<T>> input = new LinkedList<List<T>>();
		input.add(list1);
		input.add(list2);
		SEHiddenMarkovModel_impl<T> model = bmm.train(input.iterator());
		return getAlignmentFromHMM(model);
	}

	public Alignment<T>
			getAlignmentFromHMM(final HiddenMarkovModel_impl<T> hmm) {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		FullAlignment<T> doc = new FullAlignment_impl<T>(null);

		for (Integer state : hmm.getStates()) {
			if (hmm.getEventsForState(state).size() > 1)
				doc.addAlignment(idp.getNextAlignmentId(),
						hmm.getEventsForState(state));
		}

		for (T evt : hmm.getEvents()) {
			doc.addSingleton(idp.getNextAlignmentId(), evt);
		}

		return doc;
	}

	@Override
	public Class<?> getConfigurationBean() {
		return BMMConfiguration.class;
	}
}
