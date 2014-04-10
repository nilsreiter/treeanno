package de.saar.coli.salsa.reiter.framenet;

import java.util.Calendar;

import de.uniheidelberg.cl.reiter.pos.FN;

public class InteractiveLexicalUnit extends LexicalUnit {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InteractiveLexicalUnit(final Frame frame) {
	this.frame = frame;
    };

    public void setStatus(final String theStatus) {
	this.status = theStatus;
    }

    public void setId(final Integer id) {
	this.id = id;
    }

    public void setDefinition(final String definition) {
	this.definition = definition.getBytes();
    }

    public void addLexeme(final Lexeme lexeme) {
	this.lexemes.add(lexeme);
    }

    public void setPartOfSpeech(final FN partOfSpeech) {
	this.partOfSpeech = partOfSpeech;
    }

    public void populate() {
	StringBuffer buf = new StringBuffer();
	for (Lexeme lexeme : lexemes) {
	    buf.append(lexeme.toString());
	    buf.append(' ');
	}
	this.name = buf.toString().trim();

	this.creationDate = Calendar.getInstance().getTime();
    }

}
