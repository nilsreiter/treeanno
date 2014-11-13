package de.saar.coli.salsa.reiter.framenet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class can be used to programmatically create a FrameNet database.
 * 
 * @author reiter
 * 
 */
public class InteractiveDatabaseReader extends DatabaseReader {

    SortedMap<String, Set<String>> framePrototypes =
	    new TreeMap<String, Set<String>>();

    /**
     * 
     * @param frameName
     *            The name of the frame to be added
     * @param frameElements
     *            An open array of names for frame elements
     */
    public void addFrame(final String frameName, final String... frameElements) {
	Set<String> elements =
		new HashSet<String>(Arrays.asList(frameElements));
	if (!this.framePrototypes.containsKey(frameName)) {
	    framePrototypes.put(frameName, elements);
	} else {
	    framePrototypes.get(frameName).addAll(elements);
	}
    }

    /**
     * 
     * @param frameName
     *            The name of the frame for which we want to add a new FE
     * @param frameElementName
     *            The name of the new frame element
     */
    public void addFrameElement(final String frameName,
	    final String frameElementName) {
	if (!this.framePrototypes.containsKey(frameName)) {
	    this.framePrototypes.put(frameName, new HashSet<String>());
	}
	this.framePrototypes.get(frameName).add(frameElementName);
    }

    @Override
    protected boolean read(final FrameNet fn) {

	for (String frameName : framePrototypes.keySet()) {
	    Frame frame =
		    new InteractiveFrame(frameName,
			    framePrototypes.get(frameName));
	    frame.linkFrameNet(fn);
	}

	return true;
    }

    @Override
    protected void cleanup() {
	this.framePrototypes.clear();

    }

}
