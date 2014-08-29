package de.uniheidelberg.cl.a10.parser;

import java.lang.reflect.InvocationTargetException;

import de.uniheidelberg.cl.a10.parser.dep.IDependency;

public abstract class AbstractDependencyParser implements IDependencyParser {
	Class<? extends IDependency> dependencyStyle;

	protected AbstractDependencyParser(
			final Class<? extends IDependency> dependencyStyle) {
		this.dependencyStyle = dependencyStyle;
	}

	/**
	 * @return the dependencyStyle
	 */
	public Class<? extends IDependency> getDependencyStyle() {
		return dependencyStyle;
	}

	/**
	 * @param dependencyStyle
	 *            the dependencyStyle to set
	 */
	public void setDependencyStyle(
			final Class<? extends IDependency> dependencyStyle) {
		this.dependencyStyle = dependencyStyle;
	}

	public IDependency fromString(final String s) {

		try {

			IDependency ret = (IDependency) this.getDependencyStyle()
					.getMethod("fromString", String.class).invoke(null, s);
			return ret;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
