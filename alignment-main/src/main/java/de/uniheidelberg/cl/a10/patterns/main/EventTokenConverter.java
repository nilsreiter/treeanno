package de.uniheidelberg.cl.a10.patterns.main;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.patterns.Converter;

/**
 * This class can be used to convert an event-based alignment to a token-based
 * alignment.
 * 
 * @author reiter
 * 
 */
public class EventTokenConverter implements
		Converter<Alignment<Event>, Alignment<Token>> {

	@Override
	public Alignment<Token> convert(final Alignment<Event> document) {
		Alignment<Token> ret = new Alignment_impl<Token>(document.getId());

		for (Link<Event> aa : document.getAlignments()) {
			Set<Token> tokens = new HashSet<Token>();
			for (Event f : aa.getElements()) {
				tokens.add(f.getTarget());
			}
			Link<Token> l = ret.addAlignment(aa.getId(), tokens);
			if (aa.getDescription() != null)
				l.setDescription(aa.getDescription());
			if (!Double.isNaN(aa.getScore()))
				l.setScore(aa.getScore());
		}

		return ret;
	}

}
