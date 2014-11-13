package de.uniheidelberg.cl.a10.data;

import de.uniheidelberg.cl.a10.parser.dep.IDependency;

public interface IHasDependencyStyle {
	Class<? extends IDependency> getDependencyStyle();

}
