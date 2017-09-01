package de.ustu.ims.reiter.treeanno.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TxtGenerator implements Generator<String> {

	String inp;
	String suffix;

	public TxtGenerator(String suffix) {
		super();
		this.suffix = suffix;
	}

	@Override
	public void setInput(String input) {
		inp = input;
	}

	@Override
	public InputStream generate() throws IOException {
		return IOUtils.toInputStream(inp, "UTF-8");
	}

	@Override
	public String getSuffix() {
		return suffix;
	}

}
