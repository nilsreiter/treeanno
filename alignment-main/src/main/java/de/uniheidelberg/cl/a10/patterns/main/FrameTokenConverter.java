package de.uniheidelberg.cl.a10.patterns.main;

import java.util.HashSet;
import java.util.Set;

import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.patterns.Converter;

/**
 * This class can be used to convert a frame-based alignment to a token-based
 * alignment.
 * 
 * @author reiter
 * 
 */
public class FrameTokenConverter implements
		Converter<Alignment<Frame>, Alignment<Token>> {

	@Override
	public Alignment<Token> convert(final Alignment<Frame> document) {
		Alignment<Token> ret = new Alignment_impl<Token>(document.getId());

		for (Link<Frame> aa : document.getAlignments()) {
			Set<Token> tokens = new HashSet<Token>();
			for (Frame f : aa.getElements()) {
				tokens.add(f.getTarget());
			}
			ret.addAlignment(aa.getId(), tokens);
		}

		return ret;
	}

}
