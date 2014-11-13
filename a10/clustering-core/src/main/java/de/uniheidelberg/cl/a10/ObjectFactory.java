package de.uniheidelberg.cl.a10;

public class ObjectFactory<T> {
	@SuppressWarnings("unchecked")
	public T getNewObject(final String classname) {
		try {
			Object o = Class.forName(classname).newInstance();
			return (T) o;
		} catch (InstantiationException e) {
			System.err.println("No appropriate constructor for class "
					+ classname);
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
