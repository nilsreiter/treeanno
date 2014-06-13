package de.nilsreiter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class TabFormat implements Iterable<TabFormat.Line> {

	public static class Line extends ArrayList<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Line(final String l, final String sep) {
			if (l.length() > 0) {
				for (String s : l.split(sep)) {
					this.add(s);
				}
			}
		}

	}

	String sep = null;

	ArrayList<Line> lines = null;

	int skip = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	InputStream inputStream;

	public TabFormat(final String pathname, final String sep)
			throws IOException {
		this.sep = sep;
		this.lines = new ArrayList<Line>();
		this.inputStream = new FileInputStream(new File(pathname));
		process();
	}

	public TabFormat(final File file, final String sep) throws IOException {
		this.sep = sep;
		this.lines = new ArrayList<Line>();
		this.inputStream = new FileInputStream(file);
		this.process();
	}

	public TabFormat(final InputStream is, final String sep) throws IOException {
		this.sep = sep;
		this.lines = new ArrayList<Line>();
		this.inputStream = is;
		this.process();
	}

	public void process() throws IOException {
		Reader fr = new InputStreamReader(inputStream);
		StringBuffer line = new StringBuffer();
		while (fr.ready()) {
			char c = (char) fr.read();
			if (c == '\n') {
				if (skip == 0) {
					lines.add(new Line(line.toString(), sep));
				} else {
					skip--;
				}
				line = new StringBuffer();
			} else {
				line.append(c);
			}
		}
		fr.close();
	}

	public Line getLine(final int l) {
		return this.lines.get(l);
	}

	public ArrayList<Line> getLines() {
		return this.lines;
	}

	/**
	 * @return the skip
	 */
	public int getSkip() {
		return skip;
	}

	/**
	 * @param skip
	 *            the skip to set
	 */
	public void setSkip(final int skip) {
		this.skip = skip;
	}

	@Override
	public Iterator<de.nilsreiter.util.TabFormat.Line> iterator() {
		return this.getLines().iterator();
	}

}
