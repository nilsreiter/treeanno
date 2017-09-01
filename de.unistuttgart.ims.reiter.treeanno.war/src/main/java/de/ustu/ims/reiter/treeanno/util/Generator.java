package de.ustu.ims.reiter.treeanno.util;

import java.io.IOException;
import java.io.InputStream;

public interface Generator<T> {

	void setInput(T input);


	InputStream generate() throws IOException;

	String getSuffix();

}
