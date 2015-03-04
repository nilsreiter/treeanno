package de.nilsreiter.segmentation.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.uniheidelberg.cl.a10.MainWithIODir;

public class ConvertToHTMLFile extends MainWithIODir {

	public static void main(String[] args) throws FileNotFoundException,
	IOException {
		ConvertToHTMLFile m = new ConvertToHTMLFile();
		m.processArguments(args);
		System.exit(m.run());
	}

	public int run() throws FileNotFoundException, IOException {
		for (File file : this.getInputDirectory().listFiles(
				new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.endsWith(".csv");
					}
				})) {
			File oFile =
					new File(this.getOutputDirectory(), file.getName()
							+ ".html");
			FileWriter fw = new FileWriter(oFile);
			fw.write("<html><head></head><body><p>");
			CSVParser parser =
					new CSVParser(new FileReader(file), CSVFormat.RFC4180);
			for (CSVRecord csvRecord : parser) {
				if (csvRecord.getRecordNumber() > 1) {
					String cl = csvRecord.get(5);
					double conf = Double.parseDouble(csvRecord.get(6));
					int clInt = (cl.startsWith("continues") ? 0 : 1);
					double clazz = Math.abs(clInt - conf);
					int n = (int) Math.ceil(255 - (255 * clazz));
					fw.write("<span style=\"background-color:rgb(" + 255 + ","
							+ (255 - n) + "," + (255 - n) + ");\">");
					fw.write(csvRecord.get(10));
					fw.write("</span>&nbsp;");
				}
			}
			fw.write("</p></body></html>");
			fw.close();
			parser.close();
		}

		return 0;
	}
}
