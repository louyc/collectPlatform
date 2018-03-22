package com.neusoft.gbw.cp.collect.event.handler;

import com.neusoft.gbw.cp.collect.service.transfer.TransferDownMgr;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectTaskHandler implements BaseEventHandler{
	
	private TransferDownMgr transferDownMgr;

	public CollectTaskHandler(TransferDownMgr transferDownMgr) {
		this.transferDownMgr =  transferDownMgr;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_TASK_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		//获取主题对象
		if (arg0 instanceof CollectTask) {
			Log.debug("[采集服务]采集接收任务信息，collectTask=" + ((CollectTask)arg0).getCollectTaskID());
			transferDownMgr.putTask((CollectTask)arg0);
		}
		return true;
	}
}
