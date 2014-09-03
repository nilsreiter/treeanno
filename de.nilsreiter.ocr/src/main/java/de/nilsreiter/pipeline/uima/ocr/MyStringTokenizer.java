package de.nilsreiter.pipeline.uima.ocr;

import java.util.Iterator;

public class MyStringTokenizer implements Iterator<String> {
	char[] string;
	char[] delim;
	char[] halfdelim = new char[] { ',', '"', '\'', ';', '.' };

	int position = 0;

	int begin = -1;
	int end = -1;

	public MyStringTokenizer(String string, String delim) {
		super();
		this.string = string.toCharArray();
		this.delim = delim.toCharArray();
	}

	public MyStringTokenizer(String string) {
		super();
		this.string = string.toCharArray();
		this.delim = " \t\r\f\n".toCharArray();
	}

	@Override
	public boolean hasNext() {
		for (int i = position; i < string.length; i++) {
			if (!delimiter(string[i])) return true;
		}
		return false;
	}

	@Override
	public String next() {

		StringBuilder b = new StringBuilder();
		while (isWhitespace()) {
			consumeWhitespace();
		}

		if (hdelimiter(string[position])) {
			return consumePunctuation();
		}
		begin = position;
		do {
			b.append(string[position++]);
		} while (!delimiter(string[position]) && !hdelimiter(string[position]));

		end = position;
		return b.toString();

	}

	protected void consumeWhitespace() {
		position++;
	}

	protected String consumePunctuation() {
		begin = position;
		char c = string[position++];
		end = position;
		return String.valueOf(c);

	}

	protected boolean isWhitespace() {
		return delimiter(string[position]);
	}

	protected boolean isPunctuation() {
		return hdelimiter(string[position]);
	}

	protected boolean delimiter(char ch) {
		for (int i = 0; i < delim.length; i++) {
			if (delim[i] == ch) return true;
		}
		return false;
	}

	protected boolean hdelimiter(char ch) {
		for (int i = 0; i < halfdelim.length; i++) {
			if (halfdelim[i] == ch) return true;
		}
		return false;
	}

	public char[] getDelim() {
		return delim;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}
}
