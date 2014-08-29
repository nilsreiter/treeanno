package de.uniheidelberg.cl.a10.opennlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

import opennlp.tools.util.InvalidFormatException;
import de.nilsreiter.util.StringUtil;

/**
 * This class reads in datafiles, converts them and trains models. In each
 * configuration, we train {@value #folds}+1 models. The first {@value #folds}
 * models can be used in CV as usual. The last model is trained on all available
 * data and thus, can be applied on data that is not gold tagged.
 * 
 * @author reiter
 * 
 */
public class PrepareTrainingData {

	static String conf;
	static String wsjFile;
	static String indexFile;
	static String ritFilesDir;
	static String[] ritFiles;
	static File outDir;
	static int folds = 10;
	static SortedMap<String, Integer> index = new TreeMap<String, Integer>();
	@Deprecated
	static OutputStreamWriter[] testWriters;
	static OutputStreamWriter[] trainWriters;

	public static OutputStreamWriter[] getFileWriters(final String suf)
			throws IOException {
		OutputStreamWriter[] outWriter = new OutputStreamWriter[folds + 1];
		for (int i = 0; i < folds + 1; i++) {
			outWriter[i] =
					new OutputStreamWriter(new FileOutputStream(new File(
							outDir, String.valueOf(i) + suf)),
							Charset.forName("UTF-8"));
		}

		return outWriter;
	}

	public static Stack<String> getRitualData() throws IOException {
		Stack<String> data = new Stack<String>();
		for (String s : ritFiles) {
			BufferedReader br =
					new BufferedReader(new InputStreamReader(
							new FileInputStream(new File(ritFilesDir
									+ File.separator + s)),
							Charset.forName("UTF-8")));
			String line = br.readLine();
			List<String> sentence = new LinkedList<String>();
			String sentenceId = null;
			while (line != null) {
				if (line.matches("^$")) {
					data.add(sentenceId + "::" + StringUtil.join(sentence, " ")
							+ "\n");
					sentence.clear();
				} else {
					String[] l = line.split(" ");
					sentence.add(l[1] + "_" + l[2]);
					sentenceId = l[0].substring(0, l[0].indexOf('t'));
				}

				line = br.readLine();
			}
		}
		return data;
	}

	public static void closeStreams(final OutputStreamWriter[] streams)
			throws IOException {
		for (int i = 0; i < streams.length; i++) {
			streams[i].close();
		}
	}

	public static void writeWSJtoStreams(final OutputStreamWriter[] streams,
			final int limit) throws IOException {
		BufferedReader br =
				new BufferedReader(new InputStreamReader(new FileInputStream(
						new File(wsjFile)), Charset.forName("UTF-8")));
		String line = br.readLine();
		int counter = 0;
		while (line != null && counter < limit) {
			for (int i = 0; i < streams.length; i++) {
				streams[i].write(line);
				streams[i].write("\n");
			}
			line = br.readLine();
			counter++;
		}
	}

	public static void writeIndex() throws IOException {
		FileWriter fw = new FileWriter(new File(indexFile));
		for (String s : index.keySet()) {
			String b = s.substring(0, s.length() - 1);
			fw.write(b + " " + index.get(s).toString() + "\n");
		}
		fw.flush();
		fw.close();
	}

	public static void doWSJminusplusRit() throws IOException {
		trainWriters = getFileWriters(".train");
		Stack<String> ritData = getRitualData();

		try {
			int cur = 0;
			for (String l : ritData) {
				String[] p = l.split("::");
				for (int i = 0; i < folds; i++) {
					if (cur % folds == i) {
						index.put(p[0], Integer.valueOf(i));
					} else {
						trainWriters[i].write(p[1]);
						trainWriters[i].flush();
					}
				}
				trainWriters[folds].write(p[1]);
				trainWriters[folds].flush();
				cur++;
			}

		} catch (EmptyStackException e) {}
		double d = (folds - 1.0) * ((double) ritData.size() / (double) folds);
		writeWSJtoStreams(trainWriters, (int) d);
		closeStreams(trainWriters);
	}

	public static void doWSJplusRit() throws IOException {
		trainWriters = getFileWriters(".train");
		Stack<String> ritData = getRitualData();

		try {
			int cur = 0;
			for (String l : ritData) {
				String[] p = l.split("::");
				for (int i = 0; i < folds; i++) {
					if (cur % folds == i) {
						index.put(p[0], Integer.valueOf(i));
					} else {
						trainWriters[i].write(p[1]);
						trainWriters[i].flush();
					}
				}
				trainWriters[folds].write(p[1]);
				trainWriters[folds].flush();
				cur++;
			}

		} catch (EmptyStackException e) {}
		writeWSJtoStreams(trainWriters, Integer.MAX_VALUE);
		closeStreams(trainWriters);
	}

	public static void doWSJ() throws IOException {
		Stack<String> ritData = getRitualData();

		trainWriters = getFileWriters(".train");
		int cur = 0;
		for (String l : ritData) {
			String[] p = l.split("::");
			for (int i = 0; i < folds; i++) {
				if (cur % folds == i) {
					index.put(p[0], Integer.valueOf(i));
				}
			}
			cur++;
		}
		writeWSJtoStreams(trainWriters, Integer.MAX_VALUE);
		closeStreams(trainWriters);
	}

	public static void doRit() throws IOException {
		trainWriters = getFileWriters(".train");
		Stack<String> ritData = getRitualData();
		try {
			int cur = 0;
			for (String l : ritData) {
				String[] p = l.split("::");
				for (int i = 0; i < folds; i++) {
					if (cur % folds == i) {
						index.put(p[0], Integer.valueOf(i));
					} else {
						trainWriters[i].write(p[1]);
						trainWriters[i].flush();
					}
				}
				trainWriters[folds].write(p[1]);
				trainWriters[folds].flush();
				cur++;
			}

		} catch (EmptyStackException e) {
			e.printStackTrace();
		}
		closeStreams(trainWriters);
	}

	public static void doWSJplusRitplus() throws IOException {
		trainWriters = getFileWriters(".train");
		Stack<String> ritData = getRitualData();

		int cur = 0;
		while (cur < 47122)
			for (String l : ritData) {
				String[] p = l.split("::");
				for (int i = 0; i < folds; i++) {
					if (cur % folds == i) {
						index.put(p[0], Integer.valueOf(i));
					} else {
						trainWriters[i].write(p[1]);
						trainWriters[i].flush();
					}

					// TODO: Train the 10th model with all data
				}
				trainWriters[folds].write(p[1]);
				trainWriters[folds].flush();
				cur++;
			}

		writeWSJtoStreams(trainWriters, Integer.MAX_VALUE);
		closeStreams(trainWriters);
	}

	public static void trainModels() throws IOException, InvalidFormatException {
		for (int i = 0; i < folds + 1; i++) {
			String[] newArgs =
					new String[] {
							new File(outDir, i + ".train").getCanonicalPath(),
							new File(outDir, i + ".bin.gz").getCanonicalPath() };
			opennlp.tools.postag.POSTaggerME.main(newArgs);
		}
	}

	public static void main(final String[] args) throws IOException,
	InvalidFormatException {
		conf = args[0];
		wsjFile = args[1];
		indexFile = args[2];
		ritFilesDir = args[3];
		ritFiles = new File(args[3]).list(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.endsWith(".pos_chunks");
			}
		});
		outDir = new File("uima/resources/OpenNLP_Model_POS-Tagging_" + conf);
		outDir.delete();
		outDir.mkdirs();

		if (conf.equals("WSJ+Rit")) {
			doWSJplusRit();
		} else if (conf.equals("WSJ+Rit+")) {
			doWSJplusRitplus();
		} else if (conf.equals("WSJ-+Rit")) {
			doWSJminusplusRit();
		} else if (conf.equals("WSJ")) {
			doWSJ();
		} else if (conf.equals("Rit")) {
			doRit();
		} else {
			System.exit(1);
		}
		writeIndex();
		trainModels();
	}
}
