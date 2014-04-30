package de.nilsreiter.event.main;

import java.io.IOException;

import org.kohsuke.args4j.Option;

import de.nilsreiter.event.GlobalEventDetection;
import de.nilsreiter.event.impl.AllFramesEventDetection;
import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.data2.io.DataWriter;

public class ExtractEvents extends MainWithIO {

	enum DetectionStyle {
		AllFrames
	}

	@Option(name = "--style")
	DetectionStyle style = DetectionStyle.AllFrames;

	private void run() throws IOException {
		DataReader dr = new DataReader();
		Document document = dr.read(getInputStream());

		GlobalEventDetection ged;
		switch (style) {
		case AllFrames:
		default:
			ged = new BasicEventDetection(new AllFramesEventDetection(),
					new FrameEventFactory());
		}
		ged.detectEvents(document);

		DataWriter dw = new DataWriter(getOutputStream());
		dw.write(document);
		dw.close();

	}

	public static void main(String[] args) throws IOException {
		ExtractEvents ee = new ExtractEvents();

		ee.processArguments(args);
		ee.run();
	}

}