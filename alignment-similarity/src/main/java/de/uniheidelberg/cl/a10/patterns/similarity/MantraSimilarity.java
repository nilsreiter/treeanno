package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class MantraSimilarity extends AbstractSimilarityFunction<Event> {

	@Override
	public Probability sim(final Event arg0, final Event arg1)
			throws IncompatibleException {
		if (arg0.source() == Event.Source.Frame
				&& arg1.source() == Event.Source.Frame) {
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
		throw new IncompatibleException();
	}
}
