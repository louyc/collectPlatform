package com.neusoft.gbw.cp.process.realtime.event.handler;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.realtime.service.ManualTaskDispathcher;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ManuaAutoSetlTaskHandler implements BaseEventHandler {
	
	private ManualTaskDispathcher dispatch = null;
	
	public ManuaAutoSetlTaskHandler() {
		dispatch = new ManualTaskDispathcher();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.MANUAL_AUTO_SET_TASK_PROCESS_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		List<Object> taskList = (List<Object>)arg0;
		if(taskList == null || taskList.size() == 0) {
			return false;
		}
		dispatch.autoTaskDispose(taskList);
		return true;
	}
	
}
