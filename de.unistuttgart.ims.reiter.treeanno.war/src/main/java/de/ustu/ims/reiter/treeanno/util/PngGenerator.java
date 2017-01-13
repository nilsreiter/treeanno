package de.ustu.ims.reiter.treeanno.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;

public class PngGenerator implements Generator {

	String dotString;

	String dotCommand;

	public PngGenerator(String cmd) {
		dotCommand = cmd;
	}

	@Override
	public void setDotString(String s) {
		dotString = s;
	}

	@Override
	public InputStream generate() throws IOException {
		Process p = Runtime.getRuntime().exec(new String[] { dotCommand, "-Tpng" });
		p.getOutputStream().write(dotString.getBytes());
		p.getOutputStream().flush();
		p.getOutputStream().close();

		return p.getInputStream();

	}

	public static final void main(String args[]) throws FileNotFoundException, IOException {
		PngGenerator g = new PngGenerator("/usr/local/bin/dot");

		g.setDotString("digraph G{ a -> b }");
		InputStream is = g.generate();
		IOUtils.copy(is, new FileOutputStream("f.png"));

	}

	@Override
	public String getSuffix() {
		return "png";
	}
}
