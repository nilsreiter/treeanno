package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class FrameNameEquality implements SimilarityFunction<FrameTokenEvent> {

	@Override
	public Probability sim(final FrameTokenEvent arg0, final FrameTokenEvent arg1)
			throws IncompatibleException {
		if (arg0.source() == FrameTokenEvent.Source.Frame
				&& arg1.source() == FrameTokenEvent.Source.Frame) {
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
