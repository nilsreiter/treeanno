package de.saar.coli.salsa.reiter.framenet;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

public class InteractiveLexeme extends Lexeme {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void setPartOfSpeech(final IPartOfSpeech pos) {
	this.partOfSpeech = pos;
    }

    public void setValue(final String value) {
	this.value = value;
    }

    public void setId(final String id) {
	this.id = id;
    }

    public void setBreakBefore(final boolean bf) {
	this.breakBefore = bf;
    }

    public void setHeadword(final boolean headword) {
	this.headword = headword;
    }
}
