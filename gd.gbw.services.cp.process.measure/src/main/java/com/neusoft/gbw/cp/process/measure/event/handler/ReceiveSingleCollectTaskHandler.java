package com.neusoft.gbw.cp.process.measure.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.service.CollectTaskDispatcher;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveSingleCollectTaskHandler implements BaseEventHandler {
	
	private CollectTaskDispatcher dispatcher = null;
	
	public ReceiveSingleCollectTaskHandler() {
		dispatcher = new CollectTaskDispatcher();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (!(arg0 instanceof CollectTask)) {
			return false;
		}
		CollectTask task = (CollectTask) arg0;
		dispatcher.taskDispatch(task);
		return true;
	}
}

