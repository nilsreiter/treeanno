package de.nilsreiter.event.main;

import java.io.File;
import java.io.IOException;

import org.kohsuke.args4j.Option;

import de.nilsreiter.event.GlobalEventDetection;
import de.nilsreiter.event.impl.AllFramesEventDetection;
import de.nilsreiter.event.impl.AnnotatedFramesEventDetection;
import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.impl.FrameNetBasedEventDetection;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.data2.io.DataWriter;

public class ExtractEvents extends MainWithIO {

	enum DetectionStyle {
		AllFrames, AnnotatedFrames, EventFrames
	}

	@Option(name = "--style", usage = "The extraction style")
	DetectionStyle style = DetectionStyle.AllFrames;

	private void run() throws IOException {
		DataReader dr = new DataReader();
		Document document = dr.read(getInputStream());

		GlobalEventDetection ged = null;
		switch (style) {
		case AnnotatedFrames:
			ged = new BasicEventDetection(new AnnotatedFramesEventDetection(),
					new FrameEventFactory());
			break;
		case EventFrames:
			FrameNet frameNet = new FrameNet();
			frameNet.readData(new FNDatabaseReader15(new File(
					getConfiguration().getString("paths.fnhome")), false));
			try {
				ged = new BasicEventDetection(new FrameNetBasedEventDetection(
						frameNet, "Event"), new FrameEventFactory());
			} catch (FrameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
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