package com.neusoft.gbw.cp.process.inspect.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.inspect.service.InspectTaskDisposeHandler;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveInspectTaskHandler implements BaseEventHandler {
	
	private InspectTaskDisposeHandler handler = null;

	
	public ReceiveInspectTaskHandler() {
		handler = new InspectTaskDisposeHandler();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_INSPECT_TASK_PROCESS_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object obj) {
		if(obj == null || obj.toString().length() == 0) 
			return false;
	
		Map<String, Object> map = (Map<String, Object>)obj;
		handler.dispose(map);
		return true;
	}

		
}
