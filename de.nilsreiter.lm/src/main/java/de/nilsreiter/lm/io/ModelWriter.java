package de.nilsreiter.lm.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import de.nilsreiter.lm.NGramModel;
import de.uniheidelberg.cl.a10.io.AbstractWriter;

public class ModelWriter<T> extends AbstractWriter<NGramModel<T>> {

	public ModelWriter(OutputStream os) {
		super(os);
	}

	@Override
	public void write(NGramModel<T> object) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(this.outputStream);
		oos.writeObject(object);
		oos.flush();
		oos.close();
	}

}
