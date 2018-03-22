package com.neusoft.gbw.cp.process.realtime.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.channel.Channel;
import com.neusoft.gbw.cp.process.realtime.channel.ChannelPool;
import com.neusoft.gbw.cp.process.realtime.channel.ChannelType;
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
			}catch(Exception e){
				Log.debug(this.getClass().getName()+"队列存储报错",e);
			}
			String type = getChannelType(collectData);
			if (type == null) {
				continue;
			}
			Channel channel = pool.getChannel(ChannelType.valueOf(type));
			channel.put(collectData);
		}
	}
	
	/**
	 * 获取管道类型
	 * @param collectData
	 * @return
	 */
	private String getChannelType(CollectData collectData) {
		String type = null;
		CollectTask collectTask = collectData.getCollectTask();
		if (collectTask == null) {
			Log.warn("[实时处理服务]采集数据对应的任务数据为空,collectTask=" + collectTask);
			return type;
		}
		BusinessTask task = collectTask.getBusTask();
		if (task == null) {
			Log.warn("[实时处理服务]采集数据对应的业务任务数据为空,collectTask=" + collectTask);
			return type;
		}
		BusinessTaskType taskType = task.getType();
		switch(taskType) {
		case measure_manual_set:
			type = ChannelType.realtime.name();
			break;
		case measure_manual_del:
			type = ChannelType.realtime.name();
			break;
		case measure_manual_recover:
			type = ChannelType.realtime.name();
			break;
		case measure_realtime:
			type = ChannelType.realtime.name();
			break;
		case measure_real_record:
			type = ChannelType.realtime.name();
			break;
		case measure_report:
			type = ChannelType.report.name();
			break;
		default:
			Log.warn("[实时处理服务]未找到实时服务指定可处理的业务类型,BusinessTaskType=" + taskType);
			break;
		}
		return type;
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
