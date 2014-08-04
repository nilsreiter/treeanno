package de.uniheidelberg.cl.a10.patterns.main;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;

/**
 * This class can be used to convert an event-based alignment to a token-based
 * alignment.
 * 
 * @author reiter
 * 
 */
@Deprecated
public class EventTokenConverter {

	public Alignment<Token> convert(final Alignment<FrameTokenEvent> document) {
		Alignment<Token> ret = new Alignment_impl<Token>(document.getId());

		for (Link<FrameTokenEvent> aa : document.getAlignments()) {
			Set<Token> tokens = new HashSet<Token>();
			for (FrameTokenEvent f : aa.getElements()) {
				tokens.add(f.getTarget());
			}
			Link<Token> l = ret.addAlignment(aa.getId(), tokens);
			if (aa.getDescription() != null)
				l.setDescription(aa.getDescription());
			if (!Double.isNaN(aa.getScore())) l.setScore(aa.getScore());
		}

		return ret;
	}

}
