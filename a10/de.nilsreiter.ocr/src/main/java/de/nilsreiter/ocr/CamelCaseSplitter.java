package de.nilsreiter.ocr;

import java.util.ArrayList;

import de.nilsreiter.util.StringUtil;

public class CamelCaseSplitter {
	public String splitAtCaseChange(String str, String sep) {
		return StringUtil.join(this.splitAtCaseChange(str), sep, 0);
	}

	public String[] splitAtCaseChange(String str) {
		ArrayList<String> ret = new ArrayList<String>();

		char[] string = str.toCharArray();
		StringBuilder b = new StringBuilder();
		Case lastCase = null;
		for (int i = 0; i < string.length; i++) {
			Case currentCase = getCase(string[i]);

			if (lastCase != null) {
				if (lastCase == Case.lower && currentCase == Case.upper) {
					ret.add(b.toString());
					b = new StringBuilder();
				}

			}
			b.append(string[i]);
			lastCase = currentCase;
		}
		ret.add(b.toString());

		return ret.toArray(new String[ret.size()]);
	}

	public boolean isCamelCase(String s) {
		char[] string = s.toCharArray();
		boolean candidate = false;
		for (int i = 0; i < string.length; i++) {
			if (Character.isLowerCase(string[i])) {
				candidate = true;
			}
			if (!Character.isLetter(string[i])) {
				candidate = false;
			}
			if (Character.isUpperCase(string[i]) && candidate) {
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
