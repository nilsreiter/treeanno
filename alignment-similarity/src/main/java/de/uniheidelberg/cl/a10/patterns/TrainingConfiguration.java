package de.uniheidelberg.cl.a10.patterns;

import nu.xom.Element;

/**
 * An interface for sharing training configurations. Each configuration needs to
 * provide methods for XML reading and writing.
 */
public interface TrainingConfiguration {

	/**
	 * Returns a description of the configuration that can be used in a Wiki
	 * 
	 * @return
	 */
	String getWikiDescription();

	@Deprecated
	String getInfoDescription();

	/**
	 * Returns an XML representation of the configuration (for embedding in
	 * model files)
	 * 
	 * @return
	 */
	Element getXML();

	/**
	 * Reads an XML representation of the model from a given XML element.
	 * 
	 * @param element
	 * @return
	 */
	boolean fromXML(Element element);

	/**
	 * Returns a representation of a command line used to set up this
	 * configuration
	 * 
	 * @return
	 */
	String getCommandLine();
}
