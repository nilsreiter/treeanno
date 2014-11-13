package de.nilsreiter.event.similarity.test;

import java.io.File;
import java.io.IOException;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class TestBasics {
	Document[] rds = new Document[3];

	public void init() throws IOException {
		DataReader dr = new DataReader();
		rds[2] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0003.xml").getFile()));
		rds[0] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0009.xml").getFile()));
		rds[1] = dr.read(new File(this.getClass()
				.getResource("/eventized/r0016.xml").getFile()));
	}
}