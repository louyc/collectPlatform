package com.neusoft.gbw.cp.process.inspect.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveAlarmTypeHandler implements BaseEventHandler {
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.AlARM_TYPE_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object obj) {
		if(obj == null) 
			return false;
		if(obj instanceof Map) {
			Map<Integer, String> typeMap = (Map<Integer, String>)obj;
			InspectTaskContext.getInstance().addAlarmType(typeMap);
		}
		return true;
	}

		
}
