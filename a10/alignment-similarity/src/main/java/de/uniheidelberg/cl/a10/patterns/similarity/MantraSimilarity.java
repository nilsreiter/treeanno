package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class MantraSimilarity extends
		AbstractSimilarityFunction<FrameTokenEvent> {

	@Override
	public Probability sim(final FrameTokenEvent arg0,
			final FrameTokenEvent arg1) throws SimilarityCalculationException {
		if (arg0.source() == FrameTokenEvent.Source.Frame
				&& arg1.source() == FrameTokenEvent.Source.Frame) {
			Frame f0 = arg0.getFrame();
			Frame f1 = arg1.getFrame();
			if (f0.getFrameName().equalsIgnoreCase("Text_creation")
					&& f1.getFrameName().equalsIgnoreCase("Text_creation")) {
				if (f0.getFrameElm("Text") != null
						&& f1.getFrameElm("Text") != null) {
					String m0 = f0.getFrameElm("Text").getHead().getSurface();
					String m1 = f1.getFrameElm("Text").getHead().getSurface();
					if (m0.equalsIgnoreCase(m1)) {
						return Probability.ONE;
					} else {
						return Probability.NULL;
					}
				}
			}
		}
		throw new SimilarityCalculationException();
	}
}
