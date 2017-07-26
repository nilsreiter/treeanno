package de.ustu.ims.reiter.treeanno.tree;

import java.util.Comparator;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class TreeSegmentComparator implements Comparator<Node<TreeSegment>> {

	@Override
	public int compare(Node<TreeSegment> n1, Node<TreeSegment> n2) {
		TreeSegment o1 = n1.getObject();
		TreeSegment o2 = n2.getObject();
		// no problem if begin is different
		if (o1.getBegin() != o2.getBegin())
			return Integer.compare(o1.getBegin(), o2.getBegin());
		// no problem if end is different
		// shorter spans first, this applies to virtual segments as well
		if (o1.getEnd() != o2.getEnd())
			return Integer.compare(o1.getEnd(), o2.getEnd());

		// if both segments are virtual
		// the parent goes first
		if (inherits(o1, o2))
			return -1;
		if (inherits(o2, o1))
			return 1;

		// we can't decide otherwise, but these cases should not appear
		return 0;
	}

	/**
	 * Returns true, if ts2 is somewhere in the parent chain of ts1. ts2 is
	 * higher up in the tree.
	 * 
	 * @param ts1
	 * @param ts2
	 * @return
	 */
	protected boolean inherits(TreeSegment ts1, TreeSegment ts2) {
		if (ts1.getParent() == null)
			return false;
		if (ts1.getParent() == ts2)
			return true;
		return inherits(ts1.getParent(), ts2);
	}

}
