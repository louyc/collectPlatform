package com.neusoft.gbw.cp.process.measure.event.handler;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.channel.manual.RecoverTaskhandler;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ManuaRecoverlTaskHandler implements BaseEventHandler {
	
	private RecoverTaskhandler handler = null;
	
	
	
	public ManuaRecoverlTaskHandler() {
		handler = new RecoverTaskhandler();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.MANUAL_RECOVER_TASK_PROCESS_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		List<CollectTask> taskList = (List<CollectTask>)arg0;
		if(taskList == null || taskList.size() == 0) {
			return false;
		}
		handler.dispose(taskList);
		return true;
	}
	
}
