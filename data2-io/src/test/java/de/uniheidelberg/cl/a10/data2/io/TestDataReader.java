package de.uniheidelberg.cl.a10.data2.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Entity;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;

public class TestDataReader {

	@Test
	public void testReader() throws Exception {
		URL url = this.getClass().getResource("/test_docs/document.xml");
		File testWsdl = new File(url.getFile());

		DataReader dr = new DataReader();

		Document text = dr.read(testWsdl);
		// sentence extraction
		assertEquals(text.getId(), "doc_1");
		assertEquals(text.getSentences().size(), 2);
		assertEquals(text.getSentences().get(0).getTokens().size(), 9);
		assertEquals(text.getSentences().get(1).getTokens().size(), 3);
		assertEquals(text.getSentences().get(0).getSection().getId(), "sec_1");
		assertEquals(text.getSentences().get(0).getSection().getSentences()
				.size(), 2);

		// token extraction
		Token tok = text.getSentences().get(0).getTokens().get(0);
		assertEquals(tok.getSurface(), "the");
		assertEquals(tok.getId(), "t1");
		assertEquals(tok.getBegin(), 0);
		assertEquals(tok.getEnd(), 3);
		assertEquals(tok.getSentence().getTokens().size(), 9);
		assertEquals("sense_1", tok.getSense().getId());
		assertEquals(tok.getSense().getWordNetId(), "sense_the");
		assertEquals(tok.getFrameElms().size(), 1);
		assertEquals(tok.getFrames().size(), 0);
		assertEquals(text.getSentences().get(0).getTokens().get(5).getFrames()
				.size(), 1);

		// setting of governor
		assertEquals(tok.getGovernor().getId(), "t2");
		assertEquals(tok.getGovernor().getSentence().getTokens().size(), 9);

		// entity extraction
		assertEquals(text.getEntities().size(), 2);
		assertEquals(text.getMentions().size(), 5);

		// mention extraction
		Mention m = text.getMentionById("men_1");
		Entity e = m.getEntity();
		assertEquals(m.getTokens().size(), 2);
		assertEquals(1, m.getFrameElms().size());
		assertEquals(m.getTokens().get(0).getSurface(), "the");
		assertEquals(e.getId(), "ent_1");
		assertEquals(e.getMentions().size(), 3);
		assertEquals(e.getSense().getId(), "sense_3");

		// frame extraction
		assertEquals(text.getFrames().size(), 1);
		assertEquals(text.getFrames().get(0).getFrameName(), "chant_frame");
		assertEquals(text.getFrames().get(0).getFrameElms().size(), 2);

		// frame elements
		FrameElement fe = text.getFrameElmById("fe_1");
		assertEquals(fe.getMentions().size(), 3);
		assertEquals(fe.getTokens().size(), 5);
		assertEquals(fe.getName(), "singer");

		// chunks
		assertEquals(text.getSentences().get(0).getChunks().size(), 2);
		assertEquals(text.getChunks().size(), 2);

		// mantras
		assertEquals(2, text.getMantras().size());
		assertEquals("mantra2", text.getMantras().get(1).getId());
		assertEquals("t2", text.getMantras().get(1).getTokens().get(0).getId());

		// events
		assertEquals(1, text.getEvents().size());
		assertEquals("t6", text.getEvents().get(0).getAnchor().getId());
		assertEquals(1, text.getEvents().get(0).getArguments().size());
		assertEquals(5, text.getEvents().get(0).getArguments().get("Subject")
				.size());
	}
}
