package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.a10.parser.dep.IDependency;

public interface IHasDependency extends IHasDependencyStyle {
	IDependency getDependencyRelation();

}
