package com.neusoft.np.arsf.common.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.neusoft.np.arsf.common.exception.NMServiceException;

/**
 * 
 * 项目名称: common<br>
 * 模块名称: common<br>
 * 功能描述: 线程池处理，同NMService一起使用<br>
 * 创建日期: 2012-11-30 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-11-30       黄守凯        创建
 * </pre>
 */
public class NMServiceCentre {

	private final ConcurrentMap<String, NMService> serviceProps;

	private final ConcurrentMap<String, Thread> threadProps;

	private final ConcurrentMap<String, ThreadPoolExecutor> poolProps;

	private Class<? extends NPExceptionHandler> handlerClass;

	public NMServiceCentre() {
		serviceProps = new ConcurrentHashMap<String, NMService>();
		threadProps = new ConcurrentHashMap<String, Thread>();
		poolProps = new ConcurrentHashMap<String, ThreadPoolExecutor>();
	}

	public void addService(NMService service) {
		String name = service.getServiceName();
		if (name == null || "".equals(name)) {
			throw new IllegalArgumentException("Null Pointer of serviceName");
		}
		if (serviceProps.containsKey(name)) {
			throw new NMServiceException("NMService Error，该线程已经存在 " + name);
		} else {
			synchronized (NMServiceCentre.class) {
				clearInvalidService();
				if (serviceProps.containsKey(name)) {
					throw new NMServiceException("NMService Error，该线程已经存在 " + name);
				} else {
					serviceProps.put(name, service);	//rem 2016-07-06
					service.setThreadRunning(true);
					Thread thread = new Thread(service);
					thread.setName(name);
					UncaughtExceptionHandler handler = getHanderClass();
					if (handler != null) {
						thread.setUncaughtExceptionHandler(handler);
					}
					thread.start();
					while (thread.isAlive() == false) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					threadProps.put(name, thread);
				}
			}
		}
	}

	/**
	 * 清理无效的线程服务
	 */
	private void clearInvalidService() {
		for (String name:threadProps.keySet()){
			Thread thread = threadProps.get(name);
			if (thread == null) continue;
			if (thread.isAlive() == false) {
				threadProps.remove(name);
				serviceProps.remove(name);
			}
		}
	}
	
	public void removeServiceByName(String serviceName) {
		if (serviceName == null || "".equals(serviceName)) {
			throw new IllegalArgumentException("Null Pointer of serviceName");
		}
		if (serviceProps.containsKey(serviceName)) {
			synchronized (NMServiceCentre.class) {
				if (serviceProps.containsKey(serviceName)) {
					serviceProps.get(serviceName).stopThreadRunning();
					serviceProps.remove(serviceName);
				} else {
					throw new NMServiceException("NMServicePool Error，该线程不存在 " + serviceName);
				}
			}
		} else {
			throw new NMServiceException("NMServicePool Error，该线程不存在 " + serviceName);
		}
		if (threadProps.containsKey(serviceName)) {
			synchronized (NMServiceCentre.class) {
				if (threadProps.containsKey(serviceName)) {
					Thread thread = threadProps.get(serviceName);
					if (thread == null) {
						return;
					}
					if (thread.isAlive()) {
						thread.interrupt();
					}
					threadProps.remove(serviceName);
				} else {
					throw new NMServiceException("NMServicePool Error，该线程不存在 " + serviceName);
				}
			}
		}
	}

	public void stopAllThreads() {
		synchronized (NMServiceCentre.class) {
			Iterator<NMService> propsIter = serviceProps.values().iterator();
			while (propsIter.hasNext()) {
				propsIter.next().stopThreadRunning();
			}
			serviceProps.clear();
			Iterator<Thread> threadIter = threadProps.values().iterator();
			while (threadIter.hasNext()) {
				Thread t = threadIter.next();
				if (t == null) {
					continue;
				}
				if (t.isAlive()) {
					t.interrupt();
				}
			}
			serviceProps.clear();
			threadProps.clear();
		}
	}

	public void startServicePool(String poolName, Class<?> clazz, int count) {
		if (poolName == null || "".equals(poolName) || count < 1) {
			throw new NMServiceException("NMServicePool Error，参数不正确。poolName: " + poolName + ", count: " + count);
		}
		if (poolProps.containsKey(poolName)) {
			throw new NMServiceException("NMServicePool Error，该线程池已经存在 " + poolName);
		} else {
			synchronized (NMServiceCentre.class) {
				if (poolProps.containsKey(poolName)) {
					throw new NMServiceException("NMServicePool Error，该线程已经存在 " + poolName);
				} else {
					ThreadPoolExecutor executor = getThreadPool(count);
					poolProps.put(poolName, executor);
					try {
						for (int i = 0; i < count; i++) {
							NMService item = (NMService) clazz.newInstance();
							item.setThreadRunning(true);
							item.setServiceName(poolName + "-" + i);
							executor.execute(item);
						}
					} catch (Exception e) {
						throw new NMServiceException("NMServicePool Error，线程类实例化过程出错。" + e);
					}
				}
			}
		}
	}

	public void startServicePool(String poolName, Class<?> clazz, BlockingQueue<?> queue, int count) {
		if (poolName == null || "".equals(poolName) || count < 1) {
			throw new NMServiceException("NMServicePool Error，参数不正确。poolName: " + poolName + ", count: " + count);
		}
		if (poolProps.containsKey(poolName)) {
			throw new NMServiceException("NMServicePool Error，该线程池已经存在 " + poolName);
		} else {
			synchronized (NMServiceCentre.class) {
				if (poolProps.containsKey(poolName)) {
					throw new NMServiceException("NMServicePool Error，该线程已经存在 " + poolName);
				} else {
					ThreadPoolExecutor executor = getThreadPool(count);
					poolProps.put(poolName, executor);
					try {
						for (int i = 0; i < count; i++) {
							Constructor<?> constructor = clazz.getConstructor(BlockingQueue.class);
							if (constructor == null) {
								throw new NMServiceException("NMServicePool Error，线程类通过构造体初始化错误");
							}
							NMService item = (NMService) constructor.newInstance(queue);
							item.setThreadRunning(true);
							item.setServiceName(poolName + "-" + i);
							executor.execute(item);
						}
					} catch (Exception e) {
						throw new NMServiceException("NMServicePool Error，线程类实例化过程出错。" + e);
					}
				}
			}
		}
	}

	public boolean isDesignatedServicePoolExist(String poolName) {
		if (poolName == null || "".equals(poolName)) {
			return false;
		}
		return poolProps.containsKey(poolName);
	}

	public void removeServicePoolByName(String poolName) {
		if (poolName == null || "".equals(poolName)) {
			throw new IllegalArgumentException("Null Pointer of serviceName");
		}
		if (poolProps.containsKey(poolName)) {
			synchronized (NMServiceCentre.class) {
				if (poolProps.containsKey(poolName)) {
					stopPool(poolProps.get(poolName));
					poolProps.remove(poolName);
				} else {
					throw new NMServiceException("NMServicePool Error，该线程池不存在 " + poolName);
				}
			}
		} else {
			throw new NMServiceException("NMServicePool Error，该线程池不存在 " + poolName);
		}
	}

	private ThreadPoolExecutor getThreadPool(int nThreads) {
		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>()) {
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				NPExceptionHandler handler = getHanderClass();
				if (handler != null) {
					getHanderClass().uncaughtThreadPoolException(r, t);
				}
			}
		};
	}

	public void stopAllServicePool() {
		synchronized (NMServiceCentre.class) {
			Iterator<ThreadPoolExecutor> poolsIter = poolProps.values().iterator();
			while (poolsIter.hasNext()) {
				stopPool(poolsIter.next());
			}
			poolProps.clear();
		}
	}

	private void stopPool(ThreadPoolExecutor threadPool) {
		threadPool.shutdownNow();
		try {
			while (threadPool.getActiveCount() > 0 || threadPool.getQueue().size() > 0) {
				Thread.sleep(300);
			}
		} catch (InterruptedException e) {
			System.out.println("Error : stopPool process Interrupted");
		}
	}

	public NPExceptionHandler getHanderClass() {
		if (handlerClass != null) {
			try {
				return handlerClass.newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	public Class<? extends NPExceptionHandler> getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(Class<? extends NPExceptionHandler> handlerClass) {
		this.handlerClass = handlerClass;
	}
}
