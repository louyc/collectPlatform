package com.neusoft.gbw.cp.process.measure.service.handler;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.channel.Channel;
import com.neusoft.gbw.cp.process.measure.channel.ChannelPool;
import com.neusoft.gbw.cp.process.measure.channel.ChannelType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class DataDispatcherHandler extends NMService{
	
	private BlockingQueue<CollectData> queue;
	private ChannelPool pool = null;
	
	public DataDispatcherHandler(BlockingQueue<CollectData> queue) {
		this.queue = queue;
		this.pool = ChannelPool.getInstance();
	}

	@Override
	public void run() {
		CollectData collectData = null;
		while(isThreadRunning()) {
			try {
				collectData = queue.take();
			} catch (InterruptedException e) {
				Log.error("DataDispatcherHandler分发线程获取队列信息失败", e);
			}
			
			String type = getChannelType(collectData);
			if (type == null) {
				clear(collectData);
				continue;
			}
			Channel channel = pool.getChannel(ChannelType.valueOf(type));
			channel.put(collectData);
		}
	}
	
	private String getChannelType(CollectData collectData) {
		String type = null;
		CollectTask collectTask = collectData.getCollectTask();
		if (collectTask == null) {
			Log.warn("采集数据对应的任务数据为空");
			return type;
		}
		BusinessTask task = collectTask.getBusTask();
		if (task == null) {
			Log.warn("采集数据对应的业务任务数据为空");
			return type;
		}
		BusinessTaskType taskType = task.getType();
		switch(taskType) {
		case measure_manual_set:
			type = ChannelType.manual_set_recover.name();
			break;
		case measure_manual_recover:
			type = ChannelType.manual_set_recover.name();
			break;
		case measure_auto:
			if (collectTask.getBusTask().getTask_build_mode() == 0)
				type = ChannelType.measure_unit_auto.name();
			else
				type = ChannelType.measure_unit_manual.name();
			break;
		case measure_online:
			type = ChannelType.measure_online_auto.name();
			break;
		default:
			break;
		}
		
		return type;
	}
	
	@SuppressWarnings("unused")
	private void clear(CollectData data) {
		if (data != null) {
			CollectTask task = data.getCollectTask();
			task = null;
			data = null;
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
