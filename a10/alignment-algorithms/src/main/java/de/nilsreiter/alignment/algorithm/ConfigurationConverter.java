package de.nilsreiter.alignment.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.configuration.Configuration;

import de.uniheidelberg.cl.a10.patterns.train.BMMConfiguration;

public class ConfigurationConverter {
	public BMMConfiguration getBMMConfiguration(Configuration config) {
		return (BMMConfiguration) this.getConfiguration(BMMConfiguration.class,
				config);
	}

	public AlgorithmConfiguration getConfiguration(
			Class<? extends AlgorithmConfiguration> beanClass,
			Configuration config) {
		AlgorithmConfiguration bmmc;

		BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum()) {
					return Enum.valueOf(clazz, value);
				} else {
					return super.convert(value, clazz);
				}
			}
		});

		try {
			bmmc = beanClass.newInstance();
			String prefix = bmmc.getAlgorithmClass().getSimpleName();
			Iterator<String> keyIter = config.getKeys(prefix);
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				try {
					Object obj = config.getProperty(key);
					String property = key.substring(prefix.length() + 1);
					beanUtilsBean.setProperty(bmmc, property, obj);
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
