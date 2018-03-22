package com.neusoft.gbw.cp.process.realtime.event.handler;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.service.ManualTaskDispathcher;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class ManuaSetlTaskHandler implements BaseEventHandler {
	
	private ManualTaskDispathcher dispatch = null;
	
	public ManuaSetlTaskHandler() {
		dispatch = new ManualTaskDispathcher();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		List<Object> taskList = (List<Object>)arg0;
		if(taskList == null || taskList.size() == 0) {
			return false;
		}
		
		Object obj = taskList.get(0);
		if(obj instanceof CollectTask) {
			dispatch.taskDispatch(taskList);
			return true;
		}
//		if(obj instanceof Map) {
//			dispatch.taskNoControlDispatch((Map<String, String>)obj);
//			return true;
//		}
		return true;
	}
	
}
