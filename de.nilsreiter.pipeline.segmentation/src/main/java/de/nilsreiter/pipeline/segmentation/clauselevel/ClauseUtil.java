package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.LinkedList;
import java.util.List;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel;

public class ClauseUtil {

	public static DepRel getDependent(DepRel dr, String reln) {
		if (dr.getDependents() == null) return null;
		for (int i = 0; i < dr.getDependents().size(); i++) {
			if (dr.getDependents(i).getRelation().matches(reln))
				return dr.getDependents(i);
		}
		return null;
	}

	public static List<DepRel> getDependents(DepRel dr, String reln) {
		List<DepRel> deps = new LinkedList<DepRel>();
		for (int i = 0; i < dr.getDependents().size(); i++) {
			if (dr.getDependents(i).getRelation().matches(reln))
				deps.add(dr.getDependents(i));
		}
		return deps;
	}
}
