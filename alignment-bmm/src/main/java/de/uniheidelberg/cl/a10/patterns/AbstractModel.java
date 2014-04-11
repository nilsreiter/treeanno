package de.uniheidelberg.cl.a10.patterns;

import java.util.Properties;

/**
 * This class provides an abstract implementation of the {@link Model}
 * -interface.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public abstract class AbstractModel<T> implements Model<T> {

	Properties properties = new Properties();

	TrainingConfiguration trainingConfiguration = null;

	/**
	 * @param key
	 * @return
	 * @see java.util.Hashtable#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsProperty(final String key) {
		return properties.containsKey(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getProperty(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public Object setProperty(final String key, final String value) {
		return properties.setProperty(key, value);
	}

	/**
	 * @return the properties
	 */
	@Override
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the trainingConfiguration
	 */
	@Override
	public TrainingConfiguration getTrainingConfiguration() {
		return trainingConfiguration;
	}

	/**
	 * @param trainingConfiguration
	 *            the trainingConfiguration to set
	 */
	public void setTrainingConfiguration(
			final TrainingConfiguration trainingConfiguration) {
		this.trainingConfiguration = trainingConfiguration;
	}

}
