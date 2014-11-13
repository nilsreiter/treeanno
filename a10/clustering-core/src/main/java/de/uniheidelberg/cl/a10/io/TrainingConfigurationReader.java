package de.uniheidelberg.cl.a10.io;

import java.io.IOException;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.patterns.TrainingConfiguration;

public class TrainingConfigurationReader extends
		AbstractXMLReader<TrainingConfiguration> {

	@Override
	protected TrainingConfiguration read(final Element confElement)
			throws IOException {
		String confType = confElement.getAttributeValue("type");
		TrainingConfiguration tc = null;
		try {
			tc = (TrainingConfiguration) Class.forName(confType).newInstance();
			tc.fromXML(confElement);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return tc;
	}

}
