package com.neusoft.np.arsf.base.bundle.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.service.event.NMSEvent;
import com.neusoft.np.arsf.service.event.NMSObserver;
import com.neusoft.np.arsf.service.event.NMSSubject;

/**
 * 
 * 项目名称: IT监管处理平台<br>
 * 模块名称: 处理平台服务开发<br>
 * 功能描述: 事件注册工具类，注入需要监听的事件类别， 实现事件的监听功能。
 * 	本类创建事件监听对象并将接收到的事件封装到EventInfo对象中，并添加到传入的queue中<br>
 * 创建日期: 2012-12-19 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-19       黄守凯        创建
 * </pre>
 */
public class NPEventRegister {

	private Map<String, NMSObserver> observerMap = new ConcurrentHashMap<String, NMSObserver>();

	private Lock lock = new ReentrantLock();

	public void register(Collection<String> observerNames, NMSSubject subject, BlockingQueue<NPEventInfo> queue) {
		lock.lock();
		try {
			for (String observerName : observerNames) {
				addObserver(observerName, subject, queue);
			}
		} finally {
			lock.unlock();
		}
	}

	public void register(String observerName, NMSSubject subject, BlockingQueue<NPEventInfo> queue) {
		lock.lock();
		try {
			if (observerMap.containsKey(observerName)) {
				Log.warn("事件主题已经被注册：" + observerName);
				return;
			}
			addObserver(observerName, subject, queue);
		} finally {
			lock.unlock();
		}
	}

	private void addObserver(String observerName, NMSSubject subject, BlockingQueue<NPEventInfo> queue) {
		NMSObserver observer = new NMSObserverImpl(queue);
		observerMap.put(observerName, observer);
		subject.registerNMSObserver(observerName, observer);
	}

	public synchronized void unregister(NMSSubject subject) {
		lock.lock();
		try {
			if (observerMap.isEmpty()) {
				return;
			}
			Iterator<Entry<String, NMSObserver>> observerIter = observerMap.entrySet().iterator();
			while (observerIter.hasNext()) {
				Entry<String, NMSObserver> entry = observerIter.next();
				subject.unregisterNMSObserver(entry.getKey(), entry.getValue());
			}
			observerMap.clear();
		} finally {
			lock.unlock();
		}
	}

	private class NMSObserverImpl implements NMSObserver {

		private Lock innerLocker = new ReentrantLock();

		private BlockingQueue<NPEventInfo> queue;

		public NMSObserverImpl(BlockingQueue<NPEventInfo> queue) {
			this.queue = queue;
		}

		@Override
		public void notifyEvent(NMSEvent event) {
			innerLocker.lock();
			try {
				List<NPEventInfo> events = NPEventUtil.getEventInfo(event);
				for (NPEventInfo info : events) {
					queue.put(info);
				}
			} catch (InterruptedException e) {
				Log.error("NPEventRegister事件通知异常：InterruptedException", e);
			} catch (NMFormateException e) {
				Log.error("NMFormateException事件通知异常：InterruptedException", e);
			} finally {
				innerLocker.unlock();
			}
		}
	}
}
