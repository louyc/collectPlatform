package com.neusoft.np.arsf.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.osgi.framework.BundleContext;

public class NPConstructorUtils {

	/**
	 * 根据类名（className）和构造参数创建对象
	 * 
	 * @param className 类名，全限定名，如：com.neusoft.np.arsf.common.util.ClassSample
	 * @param objects 构造参数，可以为多个
	 * @return 创建出来的对象
	 * @throws NPConstructorException
	 */
	public static Object build(Class<?> clazz, Object... objects) throws NPConstructorException {
		try {
			return buildObject(clazz, objects);
		} catch (SecurityException e) {
			throw new NPConstructorException("SecurityException", e);
		} catch (IllegalArgumentException e) {
			throw new NPConstructorException("IllegalArgumentException", e);
		} catch (ClassNotFoundException e) {
			throw new NPConstructorException("ClassNotFoundException", e);
		} catch (NoSuchMethodException e) {
			throw new NPConstructorException("NoSuchMethodException", e);
		} catch (InstantiationException e) {
			throw new NPConstructorException("InstantiationException", e);
		} catch (IllegalAccessException e) {
			throw new NPConstructorException("IllegalAccessException", e);
		} catch (InvocationTargetException e) {
			throw new NPConstructorException("InvocationTargetException", e);
		}
	}

	private static Object buildObject(Class<?> clazz, Object... objects) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NPConstructorException {
		if (objects == null || objects.length == 0) {
			return clazz.newInstance();
		}
		Class<?>[] parameterTypes = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			parameterTypes[i] = objects[i].getClass();
		}
		Constructor<?> constructor = clazz.getConstructor(parameterTypes);
		if (constructor == null) {
			throw new NPConstructorException("NMServicePool Error，线程类通过构造体初始化错误");
		}
		return constructor.newInstance(objects);
	}

	/**
	 * 根据类名（className）和构造参数创建对象
	 * 
	 * @param className 类名，全限定名，如：com.neusoft.np.arsf.common.util.ClassSample
	 * @param objects 构造参数，可以为多个
	 * @return 创建出来的对象
	 * @throws NPConstructorException
	 */
	public static Object build(BundleContext bundleContext, String className, Object... objects)
			throws NPConstructorException {
		try {
			return buildObject(bundleContext, className, objects);
		} catch (SecurityException e) {
			throw new NPConstructorException("SecurityException", e);
		} catch (IllegalArgumentException e) {
			throw new NPConstructorException("IllegalArgumentException", e);
		} catch (ClassNotFoundException e) {
			throw new NPConstructorException("ClassNotFoundException", e);
		} catch (NoSuchMethodException e) {
			throw new NPConstructorException("NoSuchMethodException", e);
		} catch (InstantiationException e) {
			throw new NPConstructorException("InstantiationException", e);
		} catch (IllegalAccessException e) {
			throw new NPConstructorException("IllegalAccessException", e);
		} catch (InvocationTargetException e) {
			throw new NPConstructorException("InvocationTargetException", e);
		}
	}

	private static Object buildObject(BundleContext bundleContext, String className, Object... objects)
			throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NPConstructorException {
		Class<?> clazz = bundleContext.getBundle().loadClass(className);
		if (objects.length == 0) {
			return clazz.newInstance();
		}
		Class<?>[] parameterTypes = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			parameterTypes[i] = objects[i].getClass();
		}
		Constructor<?> constructor = clazz.getConstructor(parameterTypes);
		if (constructor == null) {
			throw new NPConstructorException("NMServicePool Error，线程类通过构造体初始化错误");
		}
		return constructor.newInstance(objects);
	}

	public static void main(String[] args) {
		try {
			ClassSample a = (ClassSample) NPConstructorUtils.build(ClassSample.class);
			System.out.println(a);
			ClassSample b = (ClassSample) NPConstructorUtils.build(ClassSample.class, "abc");
			System.out.println(b);
			ClassSample c = (ClassSample) NPConstructorUtils.build(ClassSample.class, "abc", "def");
			System.out.println(c);
		} catch (NPConstructorException e) {
			e.printStackTrace();
		}
	}
}

class ClassSample {

	private String args1;
	private String args2;

	public ClassSample() {
		System.out.println("ClassSample()");
	}

	public ClassSample(String args1) {
		this.args1 = args1;
		System.out.println("ClassSample(String args1)" + args1);
	}

	public ClassSample(String args1, String args2) {
		this.args1 = args1;
		this.args2 = args2;
		System.out.println("ClassSample(String args1,String args2)" + args1 + "," + args2);
	}

	@Override
	public String toString() {
		return "\tTo String : ClassSample [args1=" + args1 + ", args2=" + args2 + "]";
	}

}
