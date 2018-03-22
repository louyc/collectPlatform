package com.neusoft.np.arsf.base.bundle;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;

import com.neusoft.np.arsf.base.bundle.util.NPBlockingQueue;
import com.neusoft.np.arsf.base.bundle.util.NPBlockingQueueMBean;
import com.neusoft.np.arsf.common.exception.NMServiceException;
import com.neusoft.np.arsf.common.util.NMServiceCentre;
import com.neusoft.np.arsf.service.config.Configuration;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.log.Log;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: OSGi服务引用工具类<br>
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
public final class ARSFToolkit {

	private ARSFToolkit() {
	}

	/**
	 * 获取log服务
	 */
	public static Log getLog() {
		Log log = (Log) BaseServicePool.getInstance().findService(Log.class.getName());
		checkNeedArgument(log);
		return log;
	}

	/**
	 * 获取配置服务
	 */
	public static Configuration getConfiguration() {
		Configuration config = null;
		config = (Configuration) BaseServicePool.getInstance().findService(Configuration.class.getName());
		checkNeedArgument(config);
		return config;
	}

	public static String getConfigurationFrameProperty(String key) {
		return getConfiguration().getFrameProperty(key);
	}

	/**
	 * 获取Event服务
	 */
	public static NMSSubject getSubject() {
		NMSSubject subject = null;
		subject = (NMSSubject) BaseServicePool.getInstance().findService(NMSSubject.class.getName());
		checkNeedArgument(subject);
		return subject;
	}

	public static void sendEvent(String topic, Object eventData) {
		if (eventData == null) {
			return;
		}
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(topic, eventData);
		getSubject().notifyNMSObservers(topic, properties, true);
	}

	public static NPDataSourceFactory getDefaultDataSource() {
		NPDataSourceFactory factory = null;
		factory = (NPDataSourceFactory) BaseServicePool.getInstance().findService(NPDataSourceFactory.class.getName());
		checkNeedArgument(factory);
		return factory;
	}

	public static BaseContext getBaseContext() {
		return BaseContextImpl.getInstance();
	}
	/**
	 * 获取线程队列
	 * @param clazz
	 * @param name
	 * @param capacity
	 * @return
	 */
	public static <T> BlockingQueue<T> getBlockingQueue(Class<T> clazz, String name, int capacity) {
		NPBlockingQueue<T> queue = new NPBlockingQueue<T>(name, capacity);
		manageBlockingQueue(name, queue);
		return queue;
	}

	public static <T> BlockingQueue<T> getBlockingQueue(String name, int capacity) {
		NPBlockingQueue<T> queue = new NPBlockingQueue<T>(name, capacity);
		manageBlockingQueue(name, queue);
		return queue;
	}

	private static <T> void manageBlockingQueue(String name, NPBlockingQueueMBean mbean) {
		try {
			String bundleName = BaseContextImpl.getInstance().getBundleName();
			// bundleName = StringUtils.replace(bundleName, ":", "_");
			bundleName = bundleName.replaceAll(":", "_");
			// bundleName = StringUtils.replace(bundleName, " ", "");
			bundleName = bundleName.replaceAll(" ", "");
			// String qname = StringUtils.replace(name, ":", "_");
			String qname = name.replaceAll(":", "_");
			// qname = StringUtils.replace(qname, " ", "");
			qname = qname.replace(" ", "");
			String queueName = "com.neusoft.np.arsf.base.bundle.util:type=" + bundleName + "|" + qname;
			ObjectName objectName = new ObjectName(queueName);
			try {
				ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectName);
			} catch (InstanceNotFoundException e) {
				// System.out.println("-----------InstanceNotFoundException----------");
			}
			ManagementFactory.getPlatformMBeanServer().registerMBean(mbean, objectName);
		} catch (Exception e) {
			getLog().error("JMX监控开启错误。", e);
		}
	}

	public static void addEventHandler(BaseEventHandler baseEventHandler) {
		BaseContextImpl pool = BaseContextImpl.getInstance();
		pool.getRegister().register(baseEventHandler.getTopicName(), getSubject(), pool.getEventQueue());
		pool.addEventHandler(baseEventHandler);
	}

	public static NMServiceCentre getServiceCentre() {
		return BaseContextImpl.getInstance().getServiceCentre();
	}

	public static void test() {
		ARSFToolkit.getServiceCentre().startServicePool("name", Runnable.class, 10);
		ARSFToolkit.getServiceCentre()
				.startServicePool("name", Runnable.class, new ArrayBlockingQueue<Object>(100), 10);
		ARSFToolkit.getServiceCentre().removeServicePoolByName("nanme");
	}

	/**
	 * 验证对象是否为null
	 * 
	 * @param argument
	 */
	private static <T> void checkNeedArgument(T argument) {
		if (null == argument) {
			throw new NMServiceException("获取服务失败，得到引用对象为空");
		}
	}

}
