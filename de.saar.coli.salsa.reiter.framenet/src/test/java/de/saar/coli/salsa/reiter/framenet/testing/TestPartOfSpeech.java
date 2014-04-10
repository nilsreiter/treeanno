package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.uniheidelberg.cl.reiter.pos.BNC;

public class TestPartOfSpeech {
    @Test
    public void testMappingBNC2PTB() {

	for (BNC bncTag : BNC.values()) {
	    assertNotNull(bncTag);
	    assertNotNull(bncTag.toString(), bncTag.asPTB());
	}
    }
}
