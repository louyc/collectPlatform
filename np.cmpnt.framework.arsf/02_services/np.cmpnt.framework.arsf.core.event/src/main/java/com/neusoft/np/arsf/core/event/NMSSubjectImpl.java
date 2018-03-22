package com.neusoft.np.arsf.core.event;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import com.neusoft.np.arsf.base.bundle.BaseContextImpl;
import com.neusoft.np.arsf.service.event.NMSObserver;
import com.neusoft.np.arsf.service.event.NMSSubject;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 事件发布订阅服务<br>
* 功能描述: 事件发布订阅服务实现类<br>
* 创建日期: 2012-6-11 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-11       马仲佳       创建
* </pre>
 */
public class NMSSubjectImpl implements NMSSubject {

	/**
	 * ServiceRegistration Pool
	 */
	private static Map<String, Map<NMSObserver, ServiceRegistration<?>>> serviceReferencePool = new HashMap<String, Map<NMSObserver, ServiceRegistration<?>>>();

	@Override
	public synchronized void registerNMSObserver(String topic, NMSObserver observer) {
		//检查是否已经注册
		String key = topic;
		Map<NMSObserver, ServiceRegistration<?>> topicServiceRegistrations = serviceReferencePool.get(key);
		if (topicServiceRegistrations == null) {
			topicServiceRegistrations = new HashMap<NMSObserver, ServiceRegistration<?>>();
			serviceReferencePool.put(key, topicServiceRegistrations);
		}
		//重复订阅
		if (topicServiceRegistrations.containsKey(observer)) {
			return;
		}
		BundleContext context = BaseContextImpl.getInstance().getContext();
		//准备注册必须属性
		EventHandler handler = new EventHandlerImpl(observer);
		Dictionary<String, String[]> dictionary = new Hashtable<String, String[]>();
		String[] topics = new String[] { topic };
		dictionary.put(EventConstants.EVENT_TOPIC, topics);
		//注册
		ServiceRegistration<?> sr = context.registerService(EventHandler.class.getName(), handler, dictionary);
		//写入注册表
		topicServiceRegistrations.put(observer, sr);
	}

	@Override
	public synchronized void unregisterNMSObserver(String topic, NMSObserver observer) {
		String key = topic;
		if (!serviceReferencePool.containsKey(key)) {
			return;
		}
		Map<NMSObserver, ServiceRegistration<?>> topicServiceRegistrations = serviceReferencePool.get(key);

		ServiceRegistration<?> sr = topicServiceRegistrations.get(observer);
		if (sr == null) {
			return;
		}
		sr.unregister();
		topicServiceRegistrations.remove(observer);
		if (topicServiceRegistrations.size() == 0) {
			serviceReferencePool.remove(key);
		}
	}

	@Override
	public synchronized void notifyNMSObservers(String topic, Map<String, ?> properties, boolean isSync) {
		Event event = new Event(topic, properties);
		if (isSync) {
			Proxy.getEventAdmin().sendEvent(event);
		} else {
			Proxy.getEventAdmin().postEvent(event);
		}
	}

}
