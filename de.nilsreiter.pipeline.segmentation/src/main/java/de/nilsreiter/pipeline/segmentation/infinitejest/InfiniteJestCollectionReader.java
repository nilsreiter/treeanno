package de.nilsreiter.pipeline.segmentation.infinitejest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Progress;

import de.nilsreiter.pipeline.segmentation.type.Segment;

public class InfiniteJestCollectionReader extends JCasCollectionReader_ImplBase {

	public static final String PARAM_INPUT_FILE = "Input File";

	@ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
	String filePath = null;

	public boolean hasNext() throws IOException, CollectionException {
		return false;
	}

	public Progress[] getProgress() {

		return new Progress[] { new Progress() {

			private static final long serialVersionUID = 1L;

			public long getCompleted() {
				return 1;
			}

			public long getTotal() {
				return 1;
			}

			public String getUnit() {
				return "Document";
			}

			public boolean isApproximate() {
				return false;
			}
		} };
	}

	enum Mode {
		TEXT, PAR
	}

	@Override
	public void getNext(JCas jCas) throws IOException, CollectionException {
		File file = new File(filePath);

		InputStreamReader isr =
				new InputStreamReader(new FileInputStream(file), "UTF-8");

		Map<String, Integer> openSegments = new HashMap<String, Integer>();
		JCasBuilder jb = new JCasBuilder(jCas);
		StringBuilder sb = new StringBuilder();

		while (isr.ready()) {
			char ch = (char) isr.read();

			if (ch == '<') {
				jb.add(sb.toString());
				sb = new StringBuilder();
			} else if (ch == '>') {
				String tag = sb.toString();
				if (tag.startsWith("/")) {
					String rtag = tag.substring(1);
					if (openSegments.containsKey(rtag)) {
						jb.add(openSegments.get(rtag), Segment.class).setValue(
								rtag);
						openSegments.remove(rtag);
					}
				} else {
					openSegments.put(tag, jb.getPosition());
				}

				sb = new StringBuilder();
			} else {
				sb.append(ch);
			}

		}

		isr.close();
		/*
		 * String fileContents = FileUtils.readFileToString(file, "UTF-8");
		 * while (index < fileContents.length() && start >= 0 && end >= 0) {
		 * start = fileContents.indexOf('<', index); end =
		 * fileContents.indexOf('>', start); if (start >= 0 && end >= 0) {
		 * String seg = fileContents.substring(start, end + 1);
		 * jb.add(fileContents.substring(index, start - 1)); if
		 * (seg.startsWith("</")) { String segValue = seg.substring(2,
		 * seg.length() - 1); if (openSegments.containsKey(segValue)) {
		 * jb.add(openSegments.get(segValue), Segment.class)
		 * .setValue(segValue); openSegments.remove(segValue); } } else {
		 * openSegments.put(seg.substring(1, seg.length() - 1),
		 * jb.getPosition()); } index = end; } }
		 */

		jb.close();
	}
}
