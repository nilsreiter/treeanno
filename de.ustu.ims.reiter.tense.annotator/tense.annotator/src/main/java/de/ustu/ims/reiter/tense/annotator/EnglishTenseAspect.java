package de.ustu.ims.reiter.tense.annotator;

/**
 * source: http://www.englisch-hilfen.de/en/grammar/english_tenses.htm
 * 
 * @author reiterns
 *
 */
public enum EnglishTenseAspect {
	/**
	 * base form
	 */
	Simple_Present,
	/**
	 * to be + infinitive-ing
	 */
	Present_Progressive,
	/**
	 * to have + past participle
	 */
	Present_Perfect,
	/**
	 * to have + been + infinitive-ing
	 */
	Present_Perfect_Progressive,

	/**
	 * infinitive-ed
	 */
	Simple_Past,
	/**
	 * had + part participle
	 */
	Simple_Past_Perfect, Past_Progressive, Past_Perfect_Progressive,
	Will_Future, going_to_Future, Unknown,
	/**
	 * will + be + infinitive + ing
	 */
	Future_Progressive,
	/**
	 * will + have + past participle
	 */
	Simple_Future_Perfect,
	/**
	 * will + have + been + infinitive + ing
	 */
	Future_Perfect_Progressive
}