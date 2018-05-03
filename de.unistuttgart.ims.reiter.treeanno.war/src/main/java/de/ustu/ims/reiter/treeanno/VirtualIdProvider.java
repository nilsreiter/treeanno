package de.ustu.ims.reiter.treeanno;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;
import de.ustu.ims.reiter.treeanno.util.Util;

public class VirtualIdProvider {

	public static enum Scheme {
		NONE, ARNDT
	}

	public static String getVirtualId(Scheme scheme, TreeSegment ts) {
		switch (scheme) {
		case ARNDT:
			return getVirtualId_ARNDT(ts);
		default:
			return getVirtualId_NONE(ts);
		}
	}

	private static String getVirtualId_ARNDT(TreeSegment ts) {
		if (ts == null)
			return "root";
		if (Util.isVirtual(ts)) {
			return "Q" + ts.getBegin() + "." + ts.getEnd();
		} else {
			return "A" + String.valueOf(ts.getBegin());
		}
	}

	private static String getVirtualId_NONE(TreeSegment ts) {
		if (ts != null)
			return String.valueOf(ts.getId());
		else
			return "root";
	}

}
