package de.nilsreiter.lm.impl;

import java.util.LinkedList;
import java.util.List;

import de.nilsreiter.lm.Indexer;
import de.nilsreiter.lm.NGramModel;
import de.nilsreiter.lm.NGramModelTrainer;
import de.uniheidelberg.cl.a10.patterns.data.LimitedQueue;
import de.uniheidelberg.cl.reiter.util.Counter;

public class NGramModelTrainer_impl<S> extends
		AbstractTrainer_impl<NGramModel<S>, S> implements NGramModelTrainer<S> {

	Indexer<S> indexer;
	int n;

	@Override
	public NGramModel<S> train(List<S> inputList) {
		NGramModel_impl<S> model = new NGramModel_impl<S>();
		model.size = inputList.size();
		model.n = n;

		List<S> input = new LinkedList<S>(inputList);
		// prepend dummy symbols
		for (int i = 0; i < n - 1; i++) {
			input.add(0, indexer.getDummySymbol());
		}

		// count n-grams
		model.counter = new Counter<List<S>>();
		LimitedQueue<S> limited_n = new LimitedQueue<S>(n);
		LimitedQueue<S> limited_n1 = new LimitedQueue<S>(n - 1);
		for (int i = 0; i < input.size(); i++) {
			limited_n.add(input.get(i));
			limited_n1.add(input.get(i));
			model.counter.add(limited_n.getList());
			model.counter.add(limited_n1.getList());
		}

		return model;
	}

	@Override
	public Indexer<S> getIndexer() {
		return indexer;
	}

	@Override
	public void setIndexer(Indexer<S> indexer) {
		this.indexer = indexer;
	}

	@Override
	public int getN() {
		return n;
	}

	@Override
	public void setN(int n) {
		this.n = n;
	}

}
