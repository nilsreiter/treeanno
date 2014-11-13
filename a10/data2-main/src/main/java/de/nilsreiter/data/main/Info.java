package de.nilsreiter.data.main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import de.nilsreiter.util.Output;
import de.nilsreiter.util.OutputFormatConfiguration;
import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class Info extends MainWithIO {
	@Argument
	List<File> files = new LinkedList<File>();

	DataReader dataReader;

	OutputStreamWriter writer;

	@Option(name = "--listEntities", usage = "List entities")
	boolean listEntities = false;

	@Option(name = "--listFrames", usage = "List frames")
	boolean listFrames = false;

	@Option(name = "--table", usage = "Print in table view")
	boolean tableview = false;

	OutputFormatConfiguration ofConf = new OutputFormatConfiguration();

	public static void main(String[] args) throws IOException {
		Info info = new Info();
		info.processArguments(args, info.ofConf);
		info.init();
		info.run();
	}

	void init() {
		dataReader = new DataReader();
		writer = new OutputStreamWriter(this.getOutputStream(),
				Charset.forName("UTF-8"));
	}

	void run() throws IOException {

		if (tableview) {
			Output oStyle = Output.getOutput(ofConf.outputStyle);
			oStyle.setNumberFormatString(ofConf.numberFormat);
			Matrix<String, String, Double> matrix = new MapMatrix<String, String, Double>();
			for (File file : files) {
				Document d = dataReader.read(file);
				Map<String, Boolean> wordTypes = new HashMap<String, Boolean>();
				for (Token token : d.getTokens()) {
					wordTypes.put(token.getSurface(), true);
				}

				String id = d.getId();
				matrix.put(id, "Sentences", (double) d.getSentences().size());
				matrix.put(id, "Tokens", (double) d.getTokens().size());
				matrix.put(id, "Chunks", (double) d.getChunks().size());
				matrix.put(id, "Sections", (double) d.getSections().size());
				matrix.put(id, "Entities", (double) d.getEntities().size());
				matrix.put(id, "Mentions", (double) d.getMentions().size());
				matrix.put(id, "Frames", (double) d.getFrames().size());
				matrix.put(id, "Frame Elements", (double) d.getFrameElms()
						.size());
				matrix.put(id, "Events", (double) d.getEvents().size());
				matrix.put(id, "Types", (double) wordTypes.size());

			}

			writer.write(oStyle.getString(matrix));
		} else {
			for (File file : files) {
				writer.write(getDetails(dataReader.read(file)));
			}
		}
		writer.flush();
		writer.close();
	}

	/**
	 * Returns a string describing details about the ritual document.
	 * 
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	protected String getDetails(final Document rd) throws IOException {
		StringBuilder b = new StringBuilder();
		b.append("== Details for " + rd.getId() + " ==");
		b.append('\n');
		b.append("Sentences: " + rd.getSentences().size()).append('\n');
		b.append("Tokens: " + rd.getTokens().size()).append('\n');
		b.append("Chunks: " + rd.getChunks().size()).append('\n');
		b.append("Sections: " + rd.getSections().size()).append('\n');
		b.append("Entities: " + rd.getEntities().size()).append('\n');
		b.append("Mentions: " + rd.getMentions().size()).append('\n');
		b.append("Frames: " + rd.getFrames().size()).append('\n');
		b.append("Frame elements: " + rd.getFrameElms().size()).append('\n');
		b.append("Events: " + rd.getEvents().size()).append('\n');

		Map<Integer, Integer> linkings = new HashMap<Integer, Integer>();

		if (listEntities) {
			b.append("=== Entities & Mentions ===\n");
			for (Entity entity : rd.getEntities()) {
				b.append("Entity ").append(entity.getId()).append(": ");
				b.append(entity.getMentions().size()).append(" mentions.\n");
			}
			for (Mention mention : rd.getMentions()) {
				int i = mention.getFrameElms().size();
				if (!linkings.containsKey(i)) {
					linkings.put(i, 0);
				}
				linkings.put(i, linkings.get(i) + 1);
			}
			for (Integer n : linkings.keySet()) {
				b.append(linkings.get(n)).append(" mentions are linked to ");
				b.append(n).append(" frame elements.\n");
			}
		}

		if (listFrames) {
			b.append("=== Frames & Frame Elements ===\n");
			linkings.clear();
			for (FrameElement fe : rd.getFrameElms()) {
				int i = fe.getMentions().size();
				if (!linkings.containsKey(i)) {
					linkings.put(i, 0);
				}
				linkings.put(i, linkings.get(i) + 1);
			}
			for (Integer n : linkings.keySet()) {
				b.append(linkings.get(n)).append(
						" frame elements are linked to ");
				b.append(n).append(" mentions.\n");
			}
			int counter = 0;
			for (Frame f : rd.getFrames()) {
				int countLinkedFEs = 0;
				for (FrameElement fe : f.getFrameElms()) {
					if (fe.getMentions().size() > 0)
						countLinkedFEs++;
				}
				if (countLinkedFEs >= 2)
					counter++;
			}
			b.append(counter).append(
					" frames have at least 2 frame elements with mentions.\n");
			b.append("\n\n\n");
			for (Entity e : rd.getEntities()) {
				b.append("\n\n\n\n" + e.getId() + "\n");
				for (Mention m : e.getMentions()) {
					b.append(m.getMentionString() + "\n");
				}
			}
		}
		return b.toString();
	}
}
