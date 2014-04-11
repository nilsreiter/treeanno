package de.uniheidelberg.cl.a10.patterns.sequencealignment;

public class IndividualAlignment {
	/**
	 * @param type
	 * @param score
	 */
	public IndividualAlignment(final AlignmentType type, final double score) {
		super();
		this.type = type;
		this.score = score;
	}

	public IndividualAlignment(final AlignmentType type) {
		super();
		this.type = type;
	}

	AlignmentType type;
	double score = Double.NaN;

	public AlignmentType getAlignmentType() {
		return type;
	}

	public double getScore() {
		return score;
	}

	@Override
	public String toString() {
		switch (type) {
		case Partial:
			return String.valueOf(getScore());
		default:
			return type.toString();
		}
	}

}
