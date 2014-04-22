package de.uniheidelberg.cl.a10.eval;

import java.util.List;
import java.util.SortedSet;

public interface Results {

	int getNumberOfMeasures();

	SortedSet<String> getMeasures();

	List<SingleResult> getConfigurations();

	void addResult(SingleResult res);

}
