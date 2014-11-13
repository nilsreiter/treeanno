package de.nilsreiter.lm;

public interface NGramModelTrainer<S> extends Trainer<NGramModel<S>, S> {

	Indexer<S> getIndexer();

	void setIndexer(Indexer<S> indexer);

	int getN();

	void setN(int n);

}
