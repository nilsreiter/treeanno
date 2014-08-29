package edu.cmu.cs.lti.ark.fn.parsing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.cmu.cs.lti.ark.fn.data.prep.ParsePreparation;
import edu.cmu.cs.lti.ark.util.FileUtil;
import gnu.trove.TIntObjectHashMap;

public class LocalFeatureReading {
	private final String mEventsFile;
	private final String mSpansFile;
	private final String mFrameFile;
	private ArrayList<FrameFeatures> mFrameFeaturesList;
	private final ArrayList<String> mFrameLines;
	private int count;

	public LocalFeatureReading(final String eventsFile, final String spanFile,
			final String frameFile) {
		mEventsFile = eventsFile;
		mSpansFile = spanFile;
		mFrameFile = frameFile;
		mFrameLines = ParsePreparation.readSentencesFromFile(mFrameFile);
		mFrameFeaturesList = new ArrayList<FrameFeatures>();
	}

	public LocalFeatureReading(final String eventsFile, final String spanFile,
			final ArrayList<String> frameLines) {
		mEventsFile = eventsFile;
		mSpansFile = spanFile;
		mFrameFile = null;
		mFrameLines = frameLines;
		mFrameFeaturesList = new ArrayList<FrameFeatures>();
	}

	public void readLocalFeatures() throws Exception {
		readSpansFile();
		readLocalEventsFile();
	}

	public void readLocalEventsFile() throws Exception {
		BufferedInputStream bis = new BufferedInputStream(
				FileUtil.openInputStream(mEventsFile));
		int currentFrameFeaturesIndex = 0;
		int currentFEIndex = 0;
		int[] line = readALine(bis);
		ArrayList<int[]> temp = new ArrayList<int[]>();
		boolean skip = false;
		while (line.length > 0 || skip) {
			if (!skip) {
				while (line.length > 0) {
					temp.add(line);
					line = readALine(bis);
				}
			} else {
				skip = false;
			}
			int size = temp.size();
			FrameFeatures f = mFrameFeaturesList.get(currentFrameFeaturesIndex);
			if (f.fElements.size() == 0) {
				System.out.println(f.frameName
						+ ". No frame elements for the frame.");
				currentFrameFeaturesIndex++;
				if (currentFrameFeaturesIndex == mFrameFeaturesList.size())
					break;
				currentFEIndex = 0;
				System.out.println("temp.size()=" + temp.size());
				skip = true;
				continue;
			}
			SpanAndCorrespondingFeatures[] spans = f.fElementSpansAndFeatures
					.get(currentFEIndex);
			// System.out.println(f.frameName+" "+f.fElements.get(currentFEIndex)+" "+spans.length);
			if (size != spans.length) {
				System.out
						.println("Problem. Exiting. currentFrameFeaturesIndex:"
								+ currentFrameFeaturesIndex + " temp.size()="
								+ size + " spans.length:" + spans.length);
				System.exit(0);
			}
			for (int k = 0; k < size; k++) {
				spans[k].features = temp.get(k);
			}
			SpanAndCorrespondingFeatures gold = new SpanAndCorrespondingFeatures();
			gold.span = new int[2];
			gold.features = new int[spans[0].features.length];
			gold.span[0] = spans[0].span[0];
			gold.span[1] = spans[0].span[1];
			for (int k = 0; k < gold.features.length; k++)
				gold.features[k] = spans[0].features[k];
			SpanAndCorrespondingFeatures.sort(spans);
			int ind = SpanAndCorrespondingFeatures.search(spans, gold);
			f.fGoldSpans.add(ind);
			if (currentFEIndex == f.fElements.size() - 1) {
				currentFrameFeaturesIndex++;
				currentFEIndex = 0;
			} else {
				currentFEIndex++;
			}
			temp = new ArrayList<int[]>();
			line = readALine(bis);
		}
		bis.close();
	}

	public int[] readALine(final InputStream fis) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int[] ret;
		int n = readAnInt(fis);
		boolean printnum = false;
		int printcount = 0;
		while (n != -1) {
			temp.add(n);
			n = readAnInt(fis);
			count++;
			if (count % 10000000 == 0) {
				System.out.println(count);
				printnum = true;
			}
			if (printnum) {
				System.out.println("num:" + n);
				printcount++;
				if (printcount > 100) {
					printcount = 0;
					printnum = false;
				}
			}

		}
		ret = new int[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			ret[i] = temp.get(i);
		}
		return ret;
	}

	public static int readAnInt(final InputStream fis) {
		byte[] b = new byte[4];
		try {
			fis.read(b);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			return -1;
		}
		int ret = 0;
		ret += (b[0] & 0xff) << 24;
		ret += (b[1] & 0xff) << 16;
		ret += (b[2] & 0xff) << 8;
		ret += (b[3] & 0xff);
		return ret;
	}

	public void addIntSpanArray(
			final ArrayList<SpanAndCorrespondingFeatures[]> list,
			final int[][] arr) {
		int len = arr.length;
		SpanAndCorrespondingFeatures[] stringSpans = new SpanAndCorrespondingFeatures[len];
		for (int i = 0; i < len; i++) {
			stringSpans[i] = new SpanAndCorrespondingFeatures();
			stringSpans[i].span = new int[2];
			stringSpans[i].span[0] = arr[i][0];
			stringSpans[i].span[1] = arr[i][1];
		}
		list.add(stringSpans);
	}

	public void readSpansFile() {
		TIntObjectHashMap<ArrayList<Integer>> frameIndexMap = new TIntObjectHashMap<ArrayList<Integer>>();
		for (int i = 0; i < mFrameLines.size(); i++) {
			frameIndexMap.put(i, new ArrayList<Integer>());
		}
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(
					mSpansFile));
			String line = null;
			while ((line = bReader.readLine()) != null) {
				line = line.trim();
				if (line.equals(""))
					continue;
				lines.add(line);
			}
			bReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i = 0;
		int lineSize = lines.size();
		ArrayList<String> feLines = new ArrayList<String>();
		ArrayList<SpanAndCorrespondingFeatures[]> spansList = new ArrayList<SpanAndCorrespondingFeatures[]>();
		String feLine = lines.get(0);
		feLines.add(feLine);
		ArrayList<int[]> spans = new ArrayList<int[]>();
		i = 1;
		System.out.println("LineSize:" + lineSize);
		while (i < lineSize) {
			String[] toks = lines.get(i).split("\t");
			if (toks.length == 6) {
				feLines.add(lines.get(i));
				int spanSize = spans.size();
				int[][] spansArr = new int[spanSize][];
				spans.toArray(spansArr);
				addIntSpanArray(spansList, spansArr);
				spans = new ArrayList<int[]>();
			} else if (toks.length == 2) {
				int[] intSpan = new int[2];
				intSpan[0] = new Integer(toks[0]);
				intSpan[1] = new Integer(toks[1]);
				spans.add(intSpan);
			} else {
				System.out.println("Problem with line:" + lines.get(i));
			}
			// System.out.print(i+" ");
			// if(i%100==0)
			// System.out.println();
			i++;
		}
		// System.out.println();
		int spanSize = spans.size();
		int[][] spansArr = new int[spanSize][];
		spans.toArray(spansArr);
		addIntSpanArray(spansList, spansArr);
		System.out.println("FE Lines Size:" + feLines.size());
		System.out.println("Spans List Size:" + spansList.size());
		for (i = 0; i < feLines.size(); i++) {
			String[] toks = feLines.get(i).split("\t");
			int sentNum = new Integer(toks[toks.length - 1]);
			ArrayList<Integer> list = frameIndexMap.get(sentNum);
			if (list == null) {
				list = new ArrayList<Integer>();
				list.add(i);
				frameIndexMap.put(sentNum, list);
			} else {
				list.add(i);
			}
		}
		int[] keys = frameIndexMap.keys();
		for (i = 0; i < keys.length; i++) {
			if (frameIndexMap.get(keys[i]).size() == 0) {
				System.out.println("There are no spans listed for line:"
						+ keys[i]);
			}
		}
		System.out.println("Frame index map size:" + frameIndexMap.size());
		mFrameFeaturesList = new ArrayList<FrameFeatures>();
		for (i = 0; i < mFrameLines.size(); i++) {
			String[] toks = mFrameLines.get(i).split("\t");
			String frame = toks[1].trim();
			String[] span = toks[3].split("_");
			int start = new Integer(span[0]);
			int end = new Integer(span[span.length - 1]);
			ArrayList<Integer> feLineNums = frameIndexMap.get(i);
			if (feLineNums.size() > 0) {
				String assocFeLine = feLines.get(feLineNums.get(0));
				String[] aFLToks = assocFeLine.split("\t");
				int aStart = new Integer(aFLToks[3]);
				int aEnd = new Integer(aFLToks[4]);
				if (start == aStart && end == aEnd) {

				} else {
					System.out.println("Problem with frameline:"
							+ mFrameLines.get(i));
					System.exit(0);
				}
			}
			FrameFeatures f = new FrameFeatures(frame, start, end);
			for (int j = 0; j < feLineNums.size(); j++) {
				int feLineNum = feLineNums.get(j);
				String feLine1 = feLines.get(feLineNum);
				String[] feLine1Toks = feLine1.split("\t");
				f.fElements.add(feLine1Toks[1]);
				f.fElementSpansAndFeatures.add(spansList.get(feLineNum));
			}
			mFrameFeaturesList.add(f);
		}
		System.out.println("Checked all frame lines.");
	}

	public ArrayList<FrameFeatures> getMFrameFeaturesList() {
		return mFrameFeaturesList;
	}

	public void setMFrameFeaturesList(
			final ArrayList<FrameFeatures> frameFeaturesList) {
		mFrameFeaturesList = frameFeaturesList;
	}

}