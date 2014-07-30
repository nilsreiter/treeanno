package de.nilsreiter.lm.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import de.nilsreiter.lm.NGramModel;
import de.uniheidelberg.cl.a10.io.AbstractReader;

public class ModelReader<T> extends AbstractReader<NGramModel<T>> {

	@SuppressWarnings("unchecked")
	@Override
	public NGramModel<T> read(InputStream is) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(is);
		NGramModel<T> model = null;
		try {
			Object obj = ois.readObject();
			if (obj instanceof NGramModel) model = (NGramModel<T>) obj;
			return model;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
