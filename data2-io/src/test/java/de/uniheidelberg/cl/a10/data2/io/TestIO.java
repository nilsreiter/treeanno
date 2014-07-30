package de.uniheidelberg.cl.a10.data2.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.Chunk;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;

public class TestIO {

	File tempdir;

	File r0010;

	@Before
	public void setUp() throws IOException {
		tempdir = File.createTempFile("test", "");
		tempdir.delete();
		tempdir.mkdir();

		URL url = this.getClass().getResource("/r0010.xml");
		r0010 = new File(url.getFile());
	}

	@After
	public void tearDown() {
		// System.err.println(tempdir.getAbsolutePath());
		tempdir.delete();
	}

	@Test
	public void testWellFormedNess() throws ParserConfigurationException,
			SAXException, IOException {
		Document rd = new DataReader().read(r0010);
		for (Token token : rd.getTokens()) {
			assertEquals(
					token.getSurface(),
					rd.getOriginalText().substring(token.getBegin(),
							token.getEnd()));
			assertNotNull(token.getSentence());
			assertNotNull(token.getLemma());
			assertNotNull(token.getPartOfSpeech());
			assertNotNull(token.getId());
		}
		for (Sentence sentence : rd.getSentences()) {
			for (Chunk chunk : sentence.getChunks()) {
				assertNotNull(chunk.getCategory());
				assertNotNull(chunk.getTokens());
				assertNotNull(chunk.getId());
				assertNotNull(chunk.getSentence());
				assertEquals(sentence, chunk.getSentence());
			}
		}
		for (Mention mention : rd.getMentions()) {
			assertNotNull(mention.getConfidence());
			assertNotNull(mention.getEntity());
		}
		for (Entity entity : rd.getEntities()) {
			assertNotNull(entity.getId());
			// assertNotNull(entity.getSense());
			for (Mention mention : entity.getMentions()) {
				assertEquals(entity, mention.getEntity());
			}
		}
		Sentence sentence = rd.getSentences().get(3);
		assertEquals(10, sentence.getTokens().size());
		assertEquals("Send", sentence.firstToken().getSurface());
		assertEquals("pikhālakhu", sentence.getTokens().get(8).getSurface());
		Frame frame = sentence.firstToken().getFrames().iterator().next();
		assertEquals("Sending", frame.getFrameName());
		assertEquals(2, frame.getFrameElms().size());
		assertEquals("f11", frame.getId());
		FrameElement fe = frame.getFrameElm("Theme");
		assertEquals("Theme", fe.getName());
		assertEquals(2, fe.getTokens().size());
		assertEquals("share", fe.lastToken().getSurface());
	}

	@Test
	public void testIO() throws ParserConfigurationException, SAXException,
			IOException {
		Document rd = new DataReader().read(r0010);
		DataWriter xw =
				new DataWriter(new FileOutputStream(
						new File(tempdir, "tmp.xml")));
		xw.write(rd);
		xw.close();

		File file2 = new File(tempdir, "tmp.xml");
		Document rd2 = new DataReader().read(file2);
		assertEquals(rd.getId(), rd2.getId());
		assertEquals(rd.getOriginalText(), rd2.getOriginalText());

		assertEquals(rd.getSentences().size(), rd2.getSentences().size());
		assertEquals(rd.getSections().size(), rd2.getSections().size());
		assertEquals(rd.getTokens().size(), rd2.getTokens().size());
		assertEquals(rd.getChunks().size(), rd2.getChunks().size());
		assertEquals(rd.getMentions().size(), rd2.getMentions().size());
		assertEquals(rd.getEntities().size(), rd2.getEntities().size());
		assertEquals(rd.getFrames().size(), rd2.getFrames().size());
		assertEquals(rd.getFrameElms().size(), rd2.getFrameElms().size());

		// sentences
		Sentence s1 = rd.getSentences().get(1);
		Sentence s2 = rd2.getSentences().get(1);
		assertEquals(s1.getId(), s2.getId());
		assertEquals(s1.numberOfTokens(), s2.numberOfTokens());
		assertEquals(s1.firstToken(), s2.firstToken());
		assertEquals(s1.lastToken(), s2.lastToken());
		assertEquals(s1.getTokensBetween(1, 3), s2.getTokensBetween(1, 3));
		assertEquals(s1.getChunks().size(), s2.getChunks().size());
		assertEquals(s1.getSection(), s2.getSection());

		// sections (no sections in test document!)
		// Section sec1 = rd.getSections().get(1);
		// Section sec2 = rd2.getSections().get(1);
		// assertEquals(sec1.getId(), sec2.getId());
		// assertEquals(sec1.getSentences().size(), sec2.getSentences().size());

		// tokens
		assertEquals(s1.getTokens().get(3).getId(), s2.getTokens().get(3)
				.getId());
		assertEquals(s1.getTokens().get(3).getSurface(), s2.getTokens().get(3)
				.getSurface());
		assertEquals(s1.getTokens().get(3).getSense(), s2.getTokens().get(3)
				.getSense());
		assertEquals(s1.getTokens().get(3).getGovernor(), s2.getTokens().get(3)
				.getGovernor());
		assertEquals(s1.getTokens().get(3).getPartOfSpeech(), s2.getTokens()
				.get(3).getPartOfSpeech());
		assertEquals(s1.getTokens().get(3).getDependencyRelation(), s2
				.getTokens().get(3).getDependencyRelation());
		assertEquals(s1.getTokens().get(3).getLemma(), s2.getTokens().get(3)
				.getLemma());
		assertEquals(s1.getTokens().get(3).getFrames().size(), s2.getTokens()
				.get(3).getFrames().size());
		assertEquals(s1.getTokens().get(3).getFrameElms().size(), s2
				.getTokens().get(3).getFrameElms().size());
		assertEquals(s1.getTokens().get(3).getSurface(), s2.getTokens().get(3)
				.getSurface());
		assertEquals(s1.getTokens().get(3).getSentence(), s2.getTokens().get(3)
				.getSentence());

		// chunks
		Chunk ch1 = rd.getChunkById("c11");
		Chunk ch2 = rd2.getChunkById("c11");
		assertEquals(ch1.getId(), ch2.getId());
		assertEquals(ch1.getCategory(), ch2.getCategory());
		assertEquals(ch1.getTokens().size(), ch2.getTokens().size());
		assertEquals(ch1.firstToken(), ch2.firstToken());
		assertEquals(ch1.lastToken(), ch2.lastToken());
		assertEquals(ch1.getTokensBetween(0, 1), ch2.getTokensBetween(0, 1));
		assertEquals(ch1.getSentence().getId(), ch2.getSentence().getId());

		// mentions
		Mention men1 = rd.getMentionById("m6");
		Mention men2 = rd2.getMentionById("m6");
		assertEquals(men1.getTokens().size(), men2.getTokens().size());
		assertEquals(men1.firstToken(), men2.firstToken());
		assertEquals(men1.lastToken(), men2.lastToken());
		assertEquals(men1.getTokensBetween(1, 3), men2.getTokensBetween(1, 3));
		assertEquals(men1.getEntity().getId(), men2.getEntity().getId());

		assertEquals(men1.getFrameElms().size(), men2.getFrameElms().size());

		// entities
		Entity ent1 = rd.getEntityById("e0");
		Entity ent2 = rd2.getEntityById("e0");
		assertEquals(ent1.getMentions().size(), ent2.getMentions().size());
		assertEquals(ent1.getSense(), ent2.getSense());

		// frames
		Frame frame1 = rd.getFrames().get(3);
		Frame frame2 = rd2.getFrames().get(3);
		assertEquals(frame1.getId(), frame2.getId());
		assertEquals(frame1.numberOfTokens(), frame2.numberOfTokens());
		assertEquals(frame1.firstToken(), frame2.firstToken());
		assertEquals(frame1.lastToken(), frame2.lastToken());
		assertEquals(frame1.getTokensBetween(3, 5),
				frame2.getTokensBetween(3, 5));
		assertEquals(frame1.getFrameElms().size(), frame2.getFrameElms().size());
		assertEquals(frame1.getFrameName(), frame2.getFrameName());

		// frameElms
		FrameElement fe1 = rd.getFrameElmById("fe3");
		FrameElement fe2 = rd2.getFrameElmById("fe3");
		assertEquals(fe1.getMentions().size(), fe2.getMentions().size());
		assertEquals(fe1.numberOfTokens(), fe2.numberOfTokens());
		assertEquals(fe1.firstToken(), fe2.firstToken());
		assertEquals(fe1.lastToken(), fe2.lastToken());
		assertEquals(fe1.getTokensBetween(3, 5), fe2.getTokensBetween(3, 5));
		assertEquals(fe1.getName(), fe2.getName());

	}

}
