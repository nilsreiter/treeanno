package de.saar.coli.salsa.reiter.framenet.fncorpus.export;

import java.util.LinkedList;

import de.uniheidelberg.cl.reiter.util.Pair;

public class Colors extends LinkedList<Pair<String, String>> {

    static Pair<String, String> basicPair = new Pair<String, String>("FFFFFF",
	    "000000");

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Colors cols = null;

    public static Colors getColors() {
	if (cols == null) {
	    cols = new Colors();

	    cols.add(new Pair<String, String>("FFFFFF", "0000FF"));
	    cols.add(new Pair<String, String>("FFFFFF", "FF0000"));
	    cols.add(new Pair<String, String>("FFFFFF", "9400D3"));
	    cols.add(new Pair<String, String>("000000", "FFFF00"));
	    cols.add(new Pair<String, String>("FFFFFF", "008000"));
	    cols.add(new Pair<String, String>("FFFFFF", "FF69B4"));
	    cols.add(new Pair<String, String>("FFFFFF", "00BFFF"));
	    cols.add(new Pair<String, String>("FFFFFF", "800080"));
	    cols.add(new Pair<String, String>("FFFFFF", "9F79EE"));
	    cols.add(new Pair<String, String>("FFFFFF", "A52A2A"));
	    cols.add(new Pair<String, String>("FFFFFF", "006400"));
	    cols.add(new Pair<String, String>("FFFFFF", "00008B"));
	    cols.add(new Pair<String, String>("FFFFFF", "1E90FF"));
	    cols.add(new Pair<String, String>("FFFFFF", "FF00FF"));
	    cols.add(new Pair<String, String>("FFFFFF", "808080"));
	}
	return cols;
    }

    @Override
    public Pair<String, String> get(final int i) {
	try {
	    return super.get(i);
	} catch (java.lang.IndexOutOfBoundsException e) {
	    return basicPair;
	}

    }
}
