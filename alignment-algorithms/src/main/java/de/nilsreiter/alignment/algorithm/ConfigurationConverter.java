package de.nilsreiter.alignment.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.Configuration;

import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;

public class ConfigurationConverter {
	public BMMConfiguration getBMMConfiguration(Configuration config) {
		return (BMMConfiguration) this.getConfiguration(BMMConfiguration.class,
				config);
	}

	public Object getConfiguration(
			Class<? extends AlgorithmConfiguration> beanClass,
			Configuration config) {
		Object bmmc;
		try {
			bmmc = beanClass.newInstance();
			String prefix =
					((AlgorithmConfiguration) bmmc).getAlgorithmClass()
							.getSimpleName();
			Iterator<String> keyIter = config.getKeys(prefix);
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				try {
					Object obj = config.getProperty(key);
					String property = key.substring(prefix.length() + 1);
					BeanUtils.setProperty(bmmc, property, obj);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			return bmmc;
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
}
