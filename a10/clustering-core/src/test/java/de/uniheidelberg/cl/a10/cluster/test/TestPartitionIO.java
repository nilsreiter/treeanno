package de.uniheidelberg.cl.a10.cluster.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.cluster.io.PartitionReader;
import de.uniheidelberg.cl.a10.cluster.io.PartitionWriter;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.reiter.util.IO;

public class TestPartitionIO {
	PartitionReader pr;
	DataReader dataReader;
	File data;
	File tempdir;

	@Before
	public void setUp() throws Exception {
		data = new File("data/atu/data2");
		pr = new PartitionReader(data);
		dataReader = new DataReader();
		tempdir = IO.createTempDir("partitionio", "");
	}

	@Test
	public void testReader() throws IOException {
		IPartition<Document> atu = pr.read(new File("data/atu/partition.xml"));
		assertEquals(7, atu.getClusters().size());
		assertEquals(38, atu.getObjects().size());
		Document d1 = dataReader.read(new File(new File(data, "atu156"),
				"11.txt.xml"));
		Document d2 = dataReader.read(new File(new File(data, "atu156"),
				"12.txt.xml"));
		Document d3 = dataReader.read(new File(new File(data, "atu225a"),
				"5.txt.xml"));
		assertTrue(atu.together(d1, d2));
		assertFalse(atu.together(d2, d3));
		assertFalse(atu.together(d1, d3));
		assertTrue(atu.together(d3, d3));
		assertTrue(atu.together(d1, d1));
	}

	@Test
	public void testWriter() throws IOException {
		IPartition<Document> atu = pr.read(new File("data/atu/partition.xml"));
		File partitionFile = new File(tempdir, "partition.xml");
		FileOutputStream fos = new FileOutputStream(partitionFile);
		PartitionWriter pw = new PartitionWriter(fos);
		pw.write(atu);

		IPartition<Document> atu2 = pr.read(partitionFile);
		assertEquals(atu.getClusters().size(), atu2.getClusters().size());
		fos.close();
	}

	@After
	public void tearDown() {
		tempdir.delete();
	}
}
