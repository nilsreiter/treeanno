package de.saar.coli.salsa.reiter.framenet;

import java.util.Date;
import java.util.Set;

public class InteractiveFrame extends Frame {

    private static int idCounter = 0;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InteractiveFrame(final String frameName, final Set<String> feNames) {
	this.name = frameName;
	this.definition = "".getBytes();
	this.creationDate = new Date();
	this.id = String.valueOf(idCounter++);

	for (String feName : feNames) {
	    FrameElement fe = new InteractiveFrameElement(feName);
	    this.frameElements.put(feName, fe);
	}

    };
}
