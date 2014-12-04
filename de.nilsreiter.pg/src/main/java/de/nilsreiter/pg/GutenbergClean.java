package de.nilsreiter.pg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import de.uniheidelberg.cl.a10.MainWithIO;

public class GutenbergClean extends MainWithIO {

	public static final String PG_MARK_START =
			"*** START OF THIS PROJECT GUTENBERG EBOOK";
	public static final String PG_MARK_END =
			"*** END OF THIS PROJECT GUTENBERG EBOOK";

	char lineSeparator = '\n';

	public static void main(String[] args) throws IOException {
		GutenbergClean gc = new GutenbergClean('\n');
		gc.processArguments(args);
		gc.cleanText(gc.getInputStream(), gc.getOutputStream());
	}

	public GutenbergClean(char sep) {
		lineSeparator = sep;
	}

	public void cleanText(InputStream is, OutputStream os) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(os));
		String line;
		boolean content = false;
		while ((line = br.readLine()) != null) {
			if (content) {
				if (line.startsWith(PG_MARK_END)) {
					content = false;
				} else {
					bos.write(line);
					bos.write(lineSeparator);
				}
			} else if (line.startsWith(PG_MARK_START)) {
				content = true;
			}
		}
		bos.flush();
		bos.close();
		br.close();
	}

}
