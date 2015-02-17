package de.nilsreiter.pipeline.segmentation.clauselevel;

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
}
