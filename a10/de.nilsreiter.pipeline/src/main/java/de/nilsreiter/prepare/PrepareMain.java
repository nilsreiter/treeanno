package de.nilsreiter.prepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.reiter.util.Counter;

public class PrepareMain extends MainWithIO {

	@Option(name = "--wordlist", usage = "A word list with frequencies")
	File wordListFile =
			new File(
					"/Users/reiterns/Documents/A10/captivity/Rowlandson_Captivity_1770.words.txt");

	public static void main(String[] args) throws IOException {
		PrepareMain pm = new PrepareMain();
		pm.processArguments(args);
		pm.run();
	}

	public void run() throws IOException {
		InputStream in = new FileInputStream(wordListFile);
		Set<String> wordList = new HashSet<String>();
		try {
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(inR);
			String line;
			while ((line = buf.readLine()) != null) {
				wordList.add(line.trim());
			}
		} finally {
			in.close();
		}

		PDDocument pdf = PDDocument.load(this.getInputStream());
		PDFTextStripper stripper = new PDFTextStripper();
		// stripper.setStartPage();
		String text = stripper.getText(pdf);
		StringTokenizer st = new StringTokenizer(text);
		Counter<String> tokenCounter = new Counter<String>();
		Map<String, Set<String>> candidates =
				new HashMap<String, Set<String>>();
		while (st.hasMoreTokens()) {
			StringTokenizer subTokenizer =
					new StringTokenizer(st.nextToken(), ".\"',;*", true);
			while (subTokenizer.hasMoreTokens()) {
				tokenCounter.add(subTokenizer.nextToken());
			}
		}

		for (String key : tokenCounter.keySet()) {
			if (key.length() > 3) {
				Set<String> candidatesForToken = new HashSet<String>();
				for (String key2 : tokenCounter.keySet()) {
					int dist = levenshtein(key, key2);
					if (dist < 2) candidatesForToken.add(key2);
				}
				candidates.put(key, candidatesForToken);
			}
		}
		for (String key : candidates.keySet()) {
			if (candidates.get(key).size() > 1 && !wordList.contains(key)) {
				System.out.println(key);
				System.out.println("  " + candidates.get(key));
			}
		}
	}

	public int levenshtein(String s, String t) {
		int n = s.length(); // length of s
		int m = t.length(); // length of t

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left
				// and up +cost
				d[i] =
						Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
								+ cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}
}
