package com.neusoft.np.arsf.base.bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.neusoft.np.arsf.base.bundle.control.NPStartCtrlHandler;
import com.neusoft.np.arsf.base.bundle.control.NPStartEventHandler;
import com.neusoft.np.arsf.base.bundle.control.NPStartUtil;
import com.neusoft.np.arsf.base.bundle.event.NPEventInfo;
import com.neusoft.np.arsf.base.bundle.event.NPEventListener;
import com.neusoft.np.arsf.base.bundle.event.NPEventRegister;
import com.neusoft.np.arsf.base.bundle.util.BaseExceptionHandler;
import com.neusoft.np.arsf.common.util.NMOSGiServicePool;
import com.neusoft.np.arsf.common.util.NMServiceCentre;
import com.neusoft.np.arsf.service.config.Configuration;
import com.neusoft.np.arsf.service.event.NMSSubject;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: Bundle启动<br>
 * 创建日期: 2013年8月29日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年8月29日       黄守凯        创建
 * </pre>
 */
public abstract class BaseActivator implements IActivator {

	private static final int EVENT_QUEUE_SIZE = 50000;

	private static BundleContext context;

	private BaseContext baseContext;

	private Thread startingThread;

	private static BaseServicePool baseServicePool;

	private BaseContextImpl pool;

	private BaseInitInfo initInfo;

	// coreService to configfile
	private static Class<?>[] coreServiceClasses = { com.neusoft.np.arsf.service.log.Log.class, Configuration.class,
			NMSSubject.class };

	private class BundleStartRunner implements Runnable {
		@Override
		public void run() {
			initActivator();
		}
	}

	/**
	 * 获取OSGi上下文
	 * 
	 * @return
	 */
	public BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		BaseActivator.context = context;
		baseServicePool = BaseServicePool.getInstance();
		baseServicePool.setBundleContext(context);
		initInfo = BaseInitInfo.getInstance();
		startingThread = new Thread(new BundleStartRunner());
		startingThread.setName("StartingThread of " + context.getBundle().getSymbolicName());
		startingThread.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		pool.getRegister().unregister(ARSFToolkit.getSubject());
		stopActivator();
		BaseContextImpl.getInstance().clear();
		startingThread.interrupt();
		baseServicePool.clear();
		BaseActivator.context = null;
	}

	/**
	 * Bundle初始化方法
	 */
	public abstract void init();

	public abstract void stop();

	/**
	 * Bundle初始化方法
	 */
	private void proxyInit() {
		beforeInit();
		init();
		afterInit();
	}

	private void proxyStop() {
		beforeStop();
		stop();
		afterStop();
	}

	public void beforeInit() {
		// to override
	}

	public void afterInit() {
		// to override
	}

	public void beforeStop() {
		// to override
	}

	public void afterStop() {
		// to override
	}

	/**
	 * 需要订阅的主题列表，在init之后进行初始化
	 * 
	 * 如：Topic1、Topic2
	 */
	public abstract Collection<BaseEventHandler> getEventHandler();

	public Collection<BaseEventHandler> getBusinessFrameworkEventHandlers() {
		return null;
	}

	private Collection<BaseEventHandler> getProxyEventHandler() {
		Collection<BaseEventHandler> businessFrameworkEventHandlers = getBusinessFrameworkEventHandlers();
		if (businessFrameworkEventHandlers == null || businessFrameworkEventHandlers.size() == 0) {
			return getEventHandler();
		}
		Collection<BaseEventHandler> eventHandlers = getEventHandler();
		if (eventHandlers == null || eventHandlers.size() == 0) {
			return businessFrameworkEventHandlers;
		}
		eventHandlers.addAll(businessFrameworkEventHandlers);
		return eventHandlers;
	}

	/**
	 * 该服务是否是控制者
	 */
	public boolean isControler() {
		// to override
		return false;
	}

	public NMServiceCentre getNMServiceCentre() {
		return BaseContextImpl.getInstance().getServiceCentre();
	}

	public String getBundleName() {
		// to override
		return "";
	}

	public static void bindCoreServices() {
		Set<Class<?>> serviceClasses = new LinkedHashSet<Class<?>>();
		for (Class<?> coreClass : coreServiceClasses) {
			serviceClasses.add(coreClass);
		}
		bindServices(serviceClasses);
	}

	public static void bindServices(Set<Class<?>> serviceClasses) {
		for (Class<?> serviceClass : serviceClasses) {
			ServiceTrackerCustomizer<Object, Object> trackerCustomizer = new DefSrvTrackerCustomizer(context);
			@SuppressWarnings("unchecked")
			ServiceTracker<Object, Object> serviceTracker = new ServiceTracker<Object, Object>(context,
					(Class<Object>) serviceClass, trackerCustomizer);
			serviceTracker.open();
			baseServicePool.registerService(serviceClass.getName(), serviceTracker);
		}
		for (Class<?> serviceClass : serviceClasses) {
			boolean ifExist = true;
			while (ifExist) {
				Object oService = baseServicePool.findService(serviceClass.getName());
				if (oService == null) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					ifExist = false;
				}
			}
		}
	}

	public static void bindService(Class<?> serviceClass) {
		Set<Class<?>> serviceClasses = new LinkedHashSet<Class<?>>();
		serviceClasses.add(serviceClass);
		bindServices(serviceClasses);
	}

	public static void unbindService(Class<?> serviceClass) {
		Set<Class<?>> serviceClasses = new LinkedHashSet<Class<?>>();
		serviceClasses.add(serviceClass);
		unbindServices(serviceClasses);
	}

	public static void unbindCoreServices() {
		Set<Class<?>> serviceClasses = new LinkedHashSet<Class<?>>();
		for (Class<?> coreClass : coreServiceClasses) {
			serviceClasses.add(coreClass);
		}
		unbindServices(serviceClasses);
	}

	public static void unbindServices(Set<Class<?>> serviceClasses) {
		for (Class<?> serviceClass : serviceClasses) {
			baseServicePool.unregisterService(serviceClass.getName());
		}
	}

	// -------------------- 内部方法  -------------------- 

	private void initActivator() {
		pool = BaseContextImpl.getInstance();
		pool.setCustomName(getBundleName());
		pool.setContext(context);
		pool.setServicePool(new NMOSGiServicePool(context));
		NMServiceCentre serviceCentre = new NMServiceCentre();
		serviceCentre.setHandlerClass(BaseExceptionHandler.class);
		pool.setServiceCentre(serviceCentre);
		setBaseContext(pool);
		proxyInit();
		initEvent(pool);
		initInfo.finish();
		Log.info("初始化完成");
	}

	private void stopActivator() {
		proxyStop();
	}

	private void initEvent(BaseContextImpl pool) {
		Collection<BaseEventHandler> handlers = getProxyEventHandler();
		if (handlers == null) {
			if (!NPStartUtil.needStartControl(getContext().getBundle().getSymbolicName())) {
				return;
			}
			handlers = new ArrayList<BaseEventHandler>();
		}
		handlers.add(new NPStartEventHandler());
		if (isControler()) {
			handlers.add(new NPStartCtrlHandler());
		}
		Set<String> topics = new HashSet<String>();
		for (BaseEventHandler handler : handlers) {
			if (handler.getTopicName() == null || "".endsWith(handler.getTopicName())) {
				Log.warn("注册事件处理类未提供Topic名称." + handler);
				handlers.remove(handler);
				continue;
			}
			topics.add(handler.getTopicName());
		}
		pool.setRegister(new NPEventRegister());
		// pool.setEventQueue(new ArrayBlockingQueue<NPEventInfo>(EVENT_QUEUE_SIZE));
		pool.setEventQueue(ARSFToolkit.getBlockingQueue(NPEventInfo.class, "ARSF事件队列", EVENT_QUEUE_SIZE));
		pool.getRegister().register(topics, ARSFToolkit.getSubject(), pool.getEventQueue());
		pool.initEventHandler(handlers);
		pool.getServiceCentre().addService(new NPEventListener(pool.getBundleName() + "NPEventListener"));
	}

	private void setBaseContext(BaseContext baseContext) {
		this.baseContext = baseContext;
	}

	/**
	 * 获取自定义服务上下文
	 * 
	 * @return
	 */
	public BaseContext getBaseContext() {
		return baseContext;
	}
}
