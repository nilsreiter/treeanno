package de.uniheidelberg.cl.a10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TabFormat extends File implements Iterable<TabFormat.Line> {

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

	public TabFormat(final String pathname, final String sep)
			throws IOException {
		super(pathname);
		this.sep = sep;
		this.lines = new ArrayList<Line>();
		process();
	}

	public TabFormat(final File file, final String sep) throws IOException {
		super(file.getAbsolutePath());
		this.sep = sep;
		this.lines = new ArrayList<Line>();
		this.process();
	}

	public void process() throws IOException {
		java.io.FileReader fr = new java.io.FileReader(this);
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
	public Iterator<de.uniheidelberg.cl.a10.TabFormat.Line> iterator() {
		return this.getLines().iterator();
	}

}
