package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import neobio.alignment.IncompatibleScoringSchemeException;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class BasicScoringScheme<T> extends ScoringScheme<T> {

	/**
	 * The reward for a match (a substitution of equal characters).
	 */
	protected int match_reward;

	/**
	 * The penalty for a mismatch (a substitution of different characters).
	 */
	protected int mismatch_penalty;

	/**
	 * The cost of a gap (an insertion or deletion of a character).
	 */
	protected int gap_cost;

	/**
	 * Creates a new instance of a basic scoring scheme with the specified
	 * values of match reward, mismatch penalty and gap cost. The case of
	 * characters is significant when subsequently computing their score.
	 * 
	 * @param match_reward
	 *            reward for a substitution of equal characters
	 * @param mismatch_penalty
	 *            penalty for a substitution of different characters
	 * @param gap_cost
	 *            cost of an insertion or deletion of any character
	 */
	public BasicScoringScheme(final int match_reward,
			final int mismatch_penalty, final int gap_cost) {

		this.match_reward = match_reward;
		this.mismatch_penalty = mismatch_penalty;
		this.gap_cost = gap_cost;
	}

	@Override
	public int maxAbsoluteScore() {
		return Math.max(Math.abs(gap_cost),
				Math.max(Math.abs(match_reward), Math.abs(mismatch_penalty)));
	}

	@Override
	public double scoreSubstitution(final T a, final T b)
			throws IncompatibleScoringSchemeException {
		if (a == null || b == null)
			return gap_cost;
		if (a.equals(b))
			return match_reward;
		else
			return mismatch_penalty;
	}

	@Override
	public double scoreInsertion(final T a)
			throws IncompatibleScoringSchemeException {
		return this.gap_cost;
	}

	@Override
	public double scoreDeletion(final T a)
			throws IncompatibleScoringSchemeException {
		return this.gap_cost;
	}

	@Override
	public boolean isPartialMatchSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("match_reward     = " + this.match_reward).append('\n');
		b.append("mismatch_penalty = " + this.mismatch_penalty).append('\n');
		b.append("gap_cost         = " + this.gap_cost).append('\n');
		return b.toString();
	}

	@Override
	public Probability sim(final T a, final T b) {
		try {
			return Probability.fromProbability(this.scoreSubstitution(a, b));
		} catch (IncompatibleScoringSchemeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
