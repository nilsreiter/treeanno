package de.uniheidelberg.cl.a10.patterns;

import java.util.Properties;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * This interface represents generic models.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface Model<T> {

	/**
	 * Sets a property.
	 * 
	 * @param name
	 *            The name of the property
	 * @param value
	 *            The value of the property
	 * @return
	 */
	public Object setProperty(final String name, final String value);

	/**
	 * Returns the value of a named property
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(final String name);

	/**
	 * Returns the value of a property given the key. If the property does not
	 * exist, the default value is returned.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(final String key, final String defaultValue);

	/**
	 * Returns true if the model contains the property with the key.
	 * 
	 * @param key
	 *            The key of the property
	 * @return
	 */
	public boolean containsProperty(final String key);

	/**
	 * Returns the properties of the model
	 * 
	 * @return
	 */
	public Properties getProperties();

	/**
	 * Returns the propability of <code>item</code>.
	 * 
	 * @param item
	 * @return
	 */
	public Probability p(T item);

	/**
	 * Returns the configuration that was used to train this model. If the model
	 * has just been initialized (and not trained), the method should return
	 * null.
	 * 
	 * @return
	 */
	public TrainingConfiguration getTrainingConfiguration();
}
