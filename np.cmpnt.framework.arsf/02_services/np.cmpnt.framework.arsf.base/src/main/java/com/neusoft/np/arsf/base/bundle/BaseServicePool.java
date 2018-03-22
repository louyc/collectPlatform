package com.neusoft.np.arsf.base.bundle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.util.tracker.ServiceTracker;

public class BaseServicePool extends ServicePool {

	private Lock innerLocker = new ReentrantLock();

	/**
	 * pool: store service for OSGi services
	 */
	private ConcurrentMap<String, ServiceTracker<Object,Object>> store4Service;

	private static BaseServicePool servicePool;

	public final synchronized static BaseServicePool getInstance() {
		if (servicePool == null)
			servicePool = new BaseServicePool();
		return servicePool;
	}

	private BaseServicePool() {
		super();
		store4Service = new ConcurrentHashMap<String, ServiceTracker<Object,Object>>();
	}

	/**
	 * 注册服务
	 * 
	 * @param name 服务名
	 */
	public void registerService(String serviceName, ServiceTracker<Object, Object> serviceTracker) {
		innerLocker.lock();
		try {
			store4Service.put(serviceName, serviceTracker);
		} finally {
			innerLocker.unlock();
		}
	}

	/**
	 * 查找服务
	 * 
	 * @param name 服务名
	 */
	public Object findService(String serviceName) {
		Object oService = null;
		innerLocker.lock();
		try {
			ServiceTracker<Object, Object> serviceTracker = store4Service.get(serviceName);
			oService = serviceTracker.getService();
		} finally {
			innerLocker.unlock();
		}
		return oService;
	}

	/**
	 * 注销服务
	 * 
	 * @param name 服务名
	 */
	public void unregisterService(String serviceName) {
		innerLocker.lock();
		try {
			store4Service.remove(serviceName);
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
			super.clear();
			store4Service = null;
		} finally {
			innerLocker.unlock();
		}

	}
}
