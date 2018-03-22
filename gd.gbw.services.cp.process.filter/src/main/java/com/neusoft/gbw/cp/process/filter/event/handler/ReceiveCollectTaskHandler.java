package com.neusoft.gbw.cp.process.filter.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.filter.context.TaskMgrModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveCollectTaskHandler implements BaseEventHandler {
	
	public ReceiveCollectTaskHandler() {
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) 
			return false;
		
		if(arg0 instanceof CollectTask) {
			CollectTask task = (CollectTask)arg0;
			TaskMgrModel.getInstance().addTask(task);
			return true;
		}
		return false;
	}
	
}