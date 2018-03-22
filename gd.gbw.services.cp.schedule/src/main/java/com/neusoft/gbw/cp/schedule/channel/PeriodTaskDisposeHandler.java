package com.neusoft.gbw.cp.schedule.channel;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class PeriodTaskDisposeHandler extends NMService{
	
	private PeriodTaskChannel channel = null;
	
	public PeriodTaskDisposeHandler(PeriodTaskChannel channel) {
		this.channel = channel;
	}
	@Override
	public void run() {
		CollectTask task = null;
		while(isThreadRunning()) {
			try {
				task = (CollectTask)channel.take();
			} catch (InterruptedException e) {
				Log.error("接收周期任务队列数据异常", e);
				break;
			}
			sendTask(task);
		}
	}
	
	private void sendTask(CollectTask task){
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_TASK_TOPIC, task);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}

}
