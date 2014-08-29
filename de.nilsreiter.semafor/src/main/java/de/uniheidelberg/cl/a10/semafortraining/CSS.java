package de.uniheidelberg.cl.a10.semafortraining;

/**
 * CheckSentenceSanity
 * 
 * @author reiter
 * 
 */
public class CSS {
	public static int roots(final int[] governors) {
		int r = 0;
		for (int i = 0; i < governors.length; i++) {
			if (governors[i] == RunSemafor.ROOT_GOVERNOR)
				r++;
		}
		return r;
	}

	public static boolean existingGovernors(final int[] governors) {
		for (int i = 0; i < governors.length; i++) {
			if (governors[i] > governors.length
					|| (governors[i] < 0 && governors[i] != RunSemafor.ROOT_GOVERNOR))
				return false;
		}
		return true;
	}

	public static int haveLabels(final String[] relations) {
		int r = 0;
		for (int i = 0; i < relations.length; i++) {
			if (relations[i] == null)
				r++;
		}
		return r;
	}

	public static boolean allNodesReachingTop(final int[] governors) {
		for (int i = 0; i < governors.length; i++) {
			if (!nodeReachingTop(governors, i, 0))
				return false;
		}
		return true;
	}

	public static boolean nodeReachingTop(final int[] governors,
			final int node, final int steps) {
		if (governors[node] == RunSemafor.ROOT_GOVERNOR)
			return true;
		else if (steps >= governors.length)
			return false;
		else
			return nodeReachingTop(governors, governors[node], steps + 1);
	}
}
