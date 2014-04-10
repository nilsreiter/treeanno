package de.uniheidelberg.cl.a10.data2.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDataStructures {

	@Test
	public void testDataStructures() {
		Frame_impl frame1 = new Frame_impl("frame_1");
		Frame_impl frame2 = new Frame_impl("frame_2");
		Frame_impl frame3 = new Frame_impl("frame_3");
		frame1.setFrameName("name_1");
		frame2.setFrameName("name_1");
		frame3.setFrameName("name_3");

		assertEquals(frame1.equals(frame1), true);
		// same frame name
		assertEquals(false, frame1.equals(frame2));
		// different frame name
		assertEquals(frame1.equals(frame3), false);

		FrameElm_impl fe_1 = new FrameElm_impl("fe_1");
		FrameElm_impl fe_2 = new FrameElm_impl("fe_2");
		FrameElm_impl fe_3 = new FrameElm_impl("fe_3");
		FrameElm_impl fe_4 = new FrameElm_impl("fe_4");
		fe_1.setFrame(frame1);
		fe_2.setFrame(frame1);
		fe_3.setFrame(frame1);
		fe_4.setFrame(frame3);
		fe_1.setName("fe_1");
		fe_2.setName("fe_1");
		fe_3.setName("fe_3");
		fe_4.setName("fe_1");
		// same frame, same frame name
		assertEquals(fe_1.equals(fe_2), false);
		// same frame, different frame names
		assertEquals(fe_1.equals(fe_3), false);
		// different frames, same frame name
		assertEquals(fe_1.equals(fe_4), false);

		Sentence_impl sent1 = new Sentence_impl("sent_1");
		Sentence_impl sent2 = new Sentence_impl("sent_2");
		Sentence_impl sent3 = new Sentence_impl("sent_3");
		Sentence_impl sent4 = new Sentence_impl("sent_4");
		Token_impl tok1 = new Token_impl("1", "tok1");
		Token_impl tok2 = new Token_impl("2", "tok2");
		Token_impl tok3 = new Token_impl("3", "tok3");
		sent1.add(tok1);
		sent1.add(tok2);
		sent1.add(tok3);
		sent2.add(tok1);
		sent2.add(tok2);
		sent2.add(tok3);
		sent3.add(tok1);
		sent3.add(tok2);
		sent4.add(tok1);
		sent4.add(tok2);
		sent4.add(tok1);
		assertEquals(sent1.equals(sent2), true);
		assertEquals(sent1.equals(sent3), false);
		assertEquals(sent1.equals(sent4), false);

		Document_impl rd1 = new Document_impl("rd1");
		Document_impl rd2 = new Document_impl("rd2");

		Mention_impl m1 = new Mention_impl("m1");
		Mention_impl m2 = new Mention_impl("m2");
		Mention_impl m3 = new Mention_impl("m3");
		Mention_impl m4 = new Mention_impl("m4");

		m1.add(tok1);
		m1.add(tok2);
		m1.setRitualDocument(rd1);

		m2.add(tok1);
		m2.add(tok2);
		m2.setRitualDocument(rd1);

		m3.add(tok1);
		m3.add(tok2);
		m3.setRitualDocument(rd2);

		m4.add(tok1);
		m4.setRitualDocument(rd1);

		// same tokens, different ritual
		assertEquals(m1.equals(m3), false);
		// same tokens, same ritual
		assertEquals(m1.equals(m2), false);
		// different tokens, same ritual
		assertEquals(m1.equals(m4), false);

	}
}
