package de.nilsreiter.pipeline.uima.ocr;

public class CamelCaseSplitter {
	public String splitAtCaseChange(String str) {
		char[] string = str.toCharArray();
		StringBuilder b = new StringBuilder();
		Case lastCase = null;
		for (int i = 0; i < string.length; i++) {
			Case currentCase = getCase(string[i]);

			if (lastCase != null) {
				if (lastCase == Case.lower && currentCase == Case.upper)
					b.append(' ');
			}
			b.append(string[i]);
			lastCase = currentCase;
		}
		return b.toString();
	}

	public boolean isCamelCase(String s) {
		char[] string = s.toCharArray();
		boolean lower = false;
		for (int i = 0; i < string.length; i++) {
			if (Character.isLowerCase(string[i])) {
				lower = true;
			}
			if (Character.isUpperCase(string[i]) && lower) {
				return true;
			}
		}
		return false;
	}

	protected Case getCase(char ch) {
		if (Character.isLowerCase(ch)) return Case.lower;
		if (Character.isUpperCase(ch)) return Case.upper;
		return Case.other;
	}

	public static enum Case {
		lower, upper, other
	}

}
