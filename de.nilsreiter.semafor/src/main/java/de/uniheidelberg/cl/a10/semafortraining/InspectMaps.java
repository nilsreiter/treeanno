package de.uniheidelberg.cl.a10.semafortraining;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.io.File;

/**
 * This class is for inspecting the files we received by Dipanjan.
 * 
 * @author reiter
 */
public class InspectMaps {

	/**
	 * @param args
	 *            Currently empty
	 */
	@SuppressWarnings("unchecked")
	public static void main(final String[] args) throws Throwable {
		File dir = null;
		if (args.length == 1) {
			dir = new File(args[0]);
		} else {
			dir = new File("tasks/semafor-training/Merged");
		}
		File f1 = new File(dir, Constants.FILENAME_MAP_LEXICAL_UNITS);
		File f2 = new File(dir, Constants.FILENAME_MAP_FRAME_ELEMENTS);

		THashMap<String, THashSet<String>> map1 = (THashMap<String, THashSet<String>>) de.uniheidelberg.cl.a10.Util
				.readObjectFromFile(f1);
		System.out.println(map1);

		THashMap<String, THashSet<String>> map2 = (THashMap<String, THashSet<String>>) de.uniheidelberg.cl.a10.Util
				.readObjectFromFile(f2);
		System.out.println(map2);

	}

}
