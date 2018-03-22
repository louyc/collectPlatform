package com.neusoft.gbw.cp.process.realtime.service.condition;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.np.arsf.base.bundle.Log;

public class TimeoutDataPool<T> {

	private Lock lock = new ReentrantLock();

	private ConcurrentMap<String, TimeoutDataItem<T>> dataPool;

	private ScheduledExecutorService executorService;

	public void init() {
		dataPool = new ConcurrentHashMap<String, TimeoutDataItem<T>>();
		executorService = Executors.newScheduledThreadPool(1);
	}

	public void start() {
		executorService.scheduleWithFixedDelay(new TimeoutTask(), ProcessConstants.dataPoolInitialDelay,
				ProcessConstants.dataPoolPeriod, TimeUnit.SECONDS);
	}

	public void stop() {
		executorService.shutdownNow();
	}

	public TimeoutDataItem<T> get(String id) {
		return dataPool.get(id);
	}

	public T getT(String id) {
		return dataPool.containsKey(id) == false ? null : dataPool.get(id).getData();
	}

	public void update(String id, T data) {
		if(dataPool.containsKey(id))
			dataPool.get(id).setData(data);
	}

	public TimeoutDataItem<T> remove(String id) {
		lock.lock();
		try {
			return dataPool.remove(id);
		} finally {
			lock.unlock();
		}
	}

	public void put(String id, T data) {
		lock.lock();
		try {
			dataPool.put(id, new TimeoutDataItem<T>(data));
		} finally {
			lock.unlock();
		}
	}

	public class TimeoutTask implements Runnable {
		@Override
		public void run() {
			lock.lock();
			try {
				Iterator<Entry<String, TimeoutDataItem<T>>> poolIter = dataPool.entrySet().iterator();
				while (poolIter.hasNext()) {
					Entry<String, TimeoutDataItem<T>> entry = poolIter.next();
					if (entry.getValue().timeout(ProcessConstants.SOUND_RECORD_WAIT_TIME*1000)) {
						Log.info("移除超时请求数据：" + entry);
						poolIter.remove();
					}
				}
			} finally {
				lock.unlock();
			}
		}
	}

}
