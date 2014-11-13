package de.nilsreiter.ocr.eval;

import java.io.File;
import java.io.IOException;

import org.kohsuke.args4j.Option;

import de.nilsreiter.pipeline.uima.ocr.OCRUtil;
import de.nilsreiter.util.IOUtil;
import de.uniheidelberg.cl.a10.Main;

public class EvaluateOCR extends Main {

	@Option(name = "--silver", required = true)
	File silver;

	@Option(name = "--gold", required = true)
	File gold;

	public static void main(String[] args) throws IOException {
		EvaluateOCR eo = new EvaluateOCR();
		eo.processArguments(args);
		eo.run();
	}

	private void run() throws IOException {
		String goldString = IOUtil.getFileContents(gold);
		String silverString =
				IOUtil.getFileContents(silver, goldString.length());

		int lev = OCRUtil.levenshtein(silverString, goldString);
		System.out.println("lev = " + lev);
		System.out.println("length = " + goldString.length());
		System.out.println("lev/length = "
				+ ((double) lev / (double) goldString.length()));
	}
}
