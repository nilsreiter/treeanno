package de.uniheidelberg.cl.a10.semafortraining;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.io.File;

/**
 * This class is for inspecting the files we received by Dipanjan.
 * 
 * @author reiter
 */
public class InspectMap {

	/**
	 * @param args
	 *            Currently empty
	 */
	@SuppressWarnings("unchecked")
	public static void main(final String[] args) throws Throwable {
		File file = new File(args[0]);

		THashMap<String, THashSet<String>> map1 = (THashMap<String, THashSet<String>>) de.uniheidelberg.cl.a10.Util
				.readObjectFromFile(file);
		System.out.println(map1);

	}

}
