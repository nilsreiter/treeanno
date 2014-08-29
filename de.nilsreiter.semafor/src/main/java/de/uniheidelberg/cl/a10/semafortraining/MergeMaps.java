/**
 * 
 */
package de.uniheidelberg.cl.a10.semafortraining;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.io.File;
import java.io.IOException;

/**
 * This class merges the maps we received by Dipanjan with our owns.
 * 
 * @author reiter
 * 
 */
public class MergeMaps {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void main(final String[] args) throws IOException,
			ClassNotFoundException {
		File[] inDir = new File[] {
				new File("tasks/semafor-training/Dipanjan"),
				new File("tasks/semafor-training/Rituals") };
		File outDir = new File("tasks/semafor-training/Merged");

		int n = 2;

		THashMap<String, THashSet<String>> map1_out = new THashMap<String, THashSet<String>>();
		THashMap<String, THashSet<String>> map2_out = new THashMap<String, THashSet<String>>();
		for (int i = 0; i < n; i++) {
			THashMap<String, THashSet<String>> map;

			// Map 1
			map = (THashMap<String, THashSet<String>>) de.uniheidelberg.cl.a10.Util
					.readObjectFromFile(new File(inDir[i],
							Constants.FILENAME_MAP_LEXICAL_UNITS));
			for (String key : map.keySet()) {
				if (!map1_out.containsKey(key)) {
					map1_out.put(key, map.get(key));
				} else {
					for (String val : map.get(key)) {
						map1_out.get(key).add(val);
					}
				}
			}

			// Map 2
			map = (THashMap<String, THashSet<String>>) de.uniheidelberg.cl.a10.Util
					.readObjectFromFile(new File(inDir[i],
							Constants.FILENAME_MAP_FRAME_ELEMENTS));
			for (String key : map.keySet()) {
				if (!map2_out.containsKey(key)) {
					map2_out.put(key, map.get(key));
				} else {
					for (String val : map.get(key)) {
						map2_out.get(key).add(val);
					}
				}
			}

		}

		de.uniheidelberg.cl.a10.Util.writeObjectToFile(map1_out, new File(outDir,
				Constants.FILENAME_MAP_LEXICAL_UNITS));
		de.uniheidelberg.cl.a10.Util.writeObjectToFile(map2_out, new File(outDir,
				Constants.FILENAME_MAP_FRAME_ELEMENTS));

	}
}
