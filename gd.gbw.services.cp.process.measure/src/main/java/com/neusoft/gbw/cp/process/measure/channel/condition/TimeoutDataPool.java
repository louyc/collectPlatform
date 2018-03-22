package com.neusoft.gbw.cp.process.measure.channel.condition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
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
						VoiceStream reStream = (VoiceStream)entry.getValue().getData();
						poolIter.remove();
						updateStatus(reStream);
					}
				}
			} finally {
				lock.unlock();
			}
		}
	}
	
	/**
	 * 下发任务前 站点或接收机出现故障  更新收测单元失败  lyc
	 * @param task
	 */
	public void updateStatus(VoiceStream reStream){
		//更新过滤掉的采集任务状态
		CollectTask task = reStream.getCt();
		if(task.getBusTask().getType().equals(BusinessTaskType.measure_auto)){
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("task_id",task.getBusTask().getTask_id());
			map.put("monitor_alias_code",task.getBusTask().getAlias_code());
			map.put("runplan_id",task.getBusTask().getRunplan_id());
			map.put("runplan_type_id",task.getBusTask().getTask_type_id());
			map.put("unit_collect_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			map.put("unit_status_id","-1");
			map.put("uncollect_reason","超时请求数据");
			map.put("unit_begin",task.getBusTask().getUnit_begin());
			map.put("unit_end",task.getBusTask().getUnit_end());
			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, map);
		}
	}

}
