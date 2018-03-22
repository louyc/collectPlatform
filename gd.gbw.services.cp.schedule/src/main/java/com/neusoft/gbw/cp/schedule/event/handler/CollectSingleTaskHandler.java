package com.neusoft.gbw.cp.schedule.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.schedule.service.TaskDispatchorService;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class CollectSingleTaskHandler implements BaseEventHandler{
	
	private TaskDispatchorService service;
	

	public CollectSingleTaskHandler(TaskDispatchorService service) {
		this.service = service;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) {
			return false;
		}
		if(arg0 instanceof CollectTask) {
			service.recieveDate((CollectTask)arg0);
		}
		return true;
	}

}
