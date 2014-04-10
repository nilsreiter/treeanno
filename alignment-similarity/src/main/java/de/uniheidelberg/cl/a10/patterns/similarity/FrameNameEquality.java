package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class FrameNameEquality implements SimilarityFunction<Event> {

	@Override
	public Probability sim(final Event arg0, final Event arg1)
			throws IncompatibleException {
		if (arg0.source() == Event.Source.Frame
				&& arg1.source() == Event.Source.Frame) {
			String n1 = arg0.getFrame().getFrameName();
			String n2 = arg1.getFrame().getFrameName();
			if (n1.equals(n2))
				return Probability.ONE;
			return Probability.NULL;
		}
		throw new IncompatibleException();
	}

	@Override
	public void readConfiguration(final SimilarityConfiguration tc) {
	}

}
