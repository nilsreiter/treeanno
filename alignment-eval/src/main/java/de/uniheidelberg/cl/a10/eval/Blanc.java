package de.uniheidelberg.cl.a10.eval;


public interface Blanc<T> extends AlignmentEvaluation<T> {

	public static final String NC_PRECISION = "noncoref: "
			+ Evaluation.PRECISION;
	public static final String NC_RECALL = "noncoref: " + Evaluation.RECALL;
	public static final String NC_FSCORE = "noncoref: " + Evaluation.FSCORE;
	public static final String C_PRECISION = "coref: " + Evaluation.PRECISION;
	public static final String C_RECALL = "coref: " + Evaluation.RECALL;
	public static final String C_FSCORE = "coref: " + Evaluation.FSCORE;

}
