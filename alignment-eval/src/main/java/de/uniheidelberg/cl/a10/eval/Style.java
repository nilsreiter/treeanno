package de.uniheidelberg.cl.a10.eval;

public enum Style {
	/**
	 * evaluation as used in <code>
	 * Alexander Fraser and Daniel Marcu. Measuring word alignment quality 
	 * for statistical machine translation. Computational Linguistics, 
	 * 33(3):293–303, 2007.
	 *  </code>
	 */
	FRASER,

	/**
	 * evaluation based on <code>
	 * J ̈org Tiedemann. 
	 * Recycling Translations – Extraction of Lexical Data from Parallel 
	 * Corpora and their Application in Natural Language Processing. 
	 * PhD thesis, Uppsala University, Uppsala, Sweden, 2003. 
	 * </code>
	 */
	TIEDEMANN,

	/**
	 * I think we should use this algorithm
	 */
	TIEDEMANN_REFINED,

	/**
	 * Evaluation using RAND index as precision. Recall and F-score are zero.
	 */
	RAND,

	/** Evaluation style as discussed in colloquium */
	REITER, BLANC, ALL, ADJUSTED_RAND
}