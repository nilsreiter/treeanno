package de.nilsreiter.pg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.kohsuke.args4j.Option;

import de.nilsreiter.util.IOUtil;
import de.uniheidelberg.cl.a10.Main;

public class GutenbergCopy extends Main {

	@Option(name = "--urllist", required = true)
	File urlList;

	@Option(name = "--output")
	File outputDirectory = new File("target/test/data");

	char remoteSeparator = '/';

	String remoteRoot = "/home/users0/reiterns/local/gutenberg";
	String remoteHost = "zd";

	File tempdir;
	GutenbergClean cleaner;

	public static void main(String[] args) throws IOException,
	InterruptedException {
		GutenbergCopy gc = new GutenbergCopy();
		gc.processArguments(args);
		gc.run();
	}

	private void run() throws IOException, InterruptedException {
		if (!outputDirectory.exists()) outputDirectory.mkdirs();
		tempdir = IOUtil.createTempDir("GutenbergCopy", "");
		if (!tempdir.exists()) tempdir.mkdirs();

		cleaner = new GutenbergClean('\n');

		BufferedReader br = new BufferedReader(new FileReader(urlList));
		String line;
		while ((line = br.readLine()) != null) {
			URL url = new URL(line);
			this.copyFromURL(url);
		}
		br.close();
	}

	protected void copyFromURL(URL url) throws IOException,
	InterruptedException {
		System.err.print(url.toString());
		String localname = url.getFile().substring(8);
		String remotePath = getRemotePath(localname);

		File targetFile = new File(tempdir, localname);
		Process proc =
				Runtime.getRuntime().exec(
						new String[] {
								"scp",
								remoteHost + ":" + remoteRoot + remoteSeparator
								+ remotePath,
								targetFile.getAbsolutePath() });
		proc.waitFor();
		System.err.print(" [copied] ");
		try {
			cleaner.cleanText(new FileInputStream(targetFile),
					new FileOutputStream(new File(outputDirectory, localname
							+ ".txt")));
			System.err.println(" [cleaned]");
		} catch (FileNotFoundException e) {
			System.err.println(" [error]");
		}
	}

	public String getRemotePath(String name) {
		StringBuilder b = new StringBuilder();
		char[] nameArr = name.toCharArray();
		for (int i = 0; i < nameArr.length - 1; i++) {
			b.append(nameArr[i]).append(remoteSeparator);
		}
		b.append(name).append(remoteSeparator).append(name).append(".txt");
		return b.toString();
	}
}
