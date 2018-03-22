package com.neusoft.gbw.cp.schedule.service.handler;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ScheduleInfo;
import com.neusoft.gbw.cp.core.collect.ScheduleType;
import com.neusoft.gbw.cp.schedule.channel.Channel;
import com.neusoft.gbw.cp.schedule.channel.ChannelPool;
import com.neusoft.gbw.cp.schedule.channel.ChannelType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class TaskDispatchorHandler extends NMService{
	
	private BlockingQueue<CollectTask> queue;
	private ChannelPool pool;
	
	public TaskDispatchorHandler(BlockingQueue<CollectTask> queue) {
		this.queue = queue;
		this.pool = ChannelPool.getInstance();
	}

	@Override
	public void run() {
		CollectTask task = null;
		while(isThreadRunning()) {
			try {
				task = queue.take();
			} catch (InterruptedException e) {
				Log.error("collectTaskDispatcherHandler分发线程获取队列信息失败", e);
				break;
			}
			
			ChannelType type = getChannelType(task);
			Channel channel = pool.getChannel(type);
			channel.put(task);
		}
	}
	
	private ChannelType getChannelType(CollectTask task) {
		ChannelType type = null;
		ScheduleInfo info = task.getSchedule();
		if(info == null) {
			Log.warn("任务调度信息为空");
			return type;
		}
		ScheduleType Stype = info.getType();
		switch(Stype) {
		case period:
			type = ChannelType.period;
			break;
		case plan:
			type = ChannelType.plan;
			break;
		case realtime:
			type = ChannelType.realtime;
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
