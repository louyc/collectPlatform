package com.neusoft.np.arsf.core.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ServicePool {

	private Lock innerLocker = new ReentrantLock();

	private static final String SEMICOLON = "_";

	private static class ServicePoolHolder {
		private static final ServicePool INSTANCE = new ServicePool();
	}

	private ServicePool() {
	}

	/**
	 * pool: store service for OSGi services
	 */
	private static final Map<String, Object> POOL = new ConcurrentHashMap<String, Object>();

	/**
	 * static factory method
	 * @return
	 */
	protected static ServicePool getInstance() {
		return ServicePoolHolder.INSTANCE;
	}

	/**
	 * Bundle上下文对象
	 */
	private BundleContext context;

	protected void setBundleContext(BundleContext context) {
		this.context = context;
	}

	/**
	 * 获取服务
	 * 
	 * @param name 服务名
	 * @return
	 * @throws NMServerException 
	 */
	public Object getService(String name) throws NMServerException {
		Object service = POOL.get(name);
		if (service != null) {
			return service;
		}
		innerLocker.lock();
		try {
			if (POOL.containsKey(name)) {
				return POOL.get(name);
			}
			service = createService(name);
			if (service != null) {
				POOL.put(name, service);
				return service;
			}
		} finally {
			innerLocker.unlock();
		}
		throw new NMServerException("[Proxy]依赖的服务:" + name + "获取失败。");
	}

	/**
	 * 获取服务
	 * 
	 * @param name 服务名
	 * @return
	 * @throws NMServerException 
	 * @throws InvalidSyntaxException 
	 */
	public Object getService(String name, String property) throws NMServerException, InvalidSyntaxException {
		String key = getName(name, property);
		Object service = POOL.get(key);
		if (service != null) {
			return service;
		}
		innerLocker.lock();
		try {
			if (POOL.containsKey(key)) {
				return POOL.get(key);
			}
			service = createService(name, property);
			if (service != null) {
				POOL.put(key, service);
				return service;
			}
			throw new NMServerException("[Proxy]依赖的服务:" + name + "获取失败。");
		} finally {
			innerLocker.unlock();
		}
	}

	private String getName(String firName, String secName) {
		return firName + SEMICOLON + secName;
	}

	/**
	 * 创建指定服务对象
	 * 
	 * @param className 完整服务暴露的类路径
	 * @return
	 * @throws NMServerException 
	 */
	private Object createService(String className) throws NMServerException {
		if (null == context) {
			throw new NMServerException("[Proxy]依赖的服务时，缺少OSGi的context设置。");
		}
		ServiceReference<?> serviceRef = context.getServiceReference(className);
		if (serviceRef == null)
			return null;
		return context.getService(serviceRef);
	}

	/**
	 * 创建指定服务对象
	 * 
	 * @param className 完整服务暴露的类路径
	 * @return
	 * @throws InvalidSyntaxException 
	 * @throws NMServerException 
	 */
	private Object createService(String className, String propertyName) throws InvalidSyntaxException,
			NMServerException {
		if (null == context) {
			throw new NMServerException("[Proxy]依赖的服务时，缺少OSGi的context设置。");
		}
		ServiceReference<?>[] serviceRefs = context.getServiceReferences(className, null);
		if (null == serviceRefs || serviceRefs.length == 0) {
			throw new NMServerException("[Proxy]没有找到接口对应的服务实现。接口名称为:" + className);
		}
		for (ServiceReference<?> serviceReference : serviceRefs) {
			String target = (String) serviceReference.getProperty("service");
			if (propertyName.equals(target)) {
				Object object = context.getService(serviceReference);
				if (null == object) {
					throw new NMServerException("[Proxy]能够找到对应的接口实现，但是不存在指定特征值的接口实现。接口名称为:" + className
							+ ",特征值为:" + propertyName);
				}
				return object;
			}
		}
		throw new NMServerException("");
	}

	/**
	 * 删除服务
	 * 
	 * @param name 服务名
	 */
	public void removeService(String name) {
		innerLocker.lock();
		try {
			POOL.remove(name);
		} finally {
			innerLocker.unlock();
		}
	}

	/**
	 * 清空服务池
	 */
	public void clear() {
		innerLocker.lock();
		try {
			POOL.clear();
		} finally {
			innerLocker.unlock();
		}
	}
}
