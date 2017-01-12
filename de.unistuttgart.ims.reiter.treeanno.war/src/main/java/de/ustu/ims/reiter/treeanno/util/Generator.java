package de.ustu.ims.reiter.treeanno.util;

import java.io.IOException;
import java.io.InputStream;

public interface Generator {

	void setDotString(String s);

	InputStream generate() throws IOException;

	String getSuffix();

}
