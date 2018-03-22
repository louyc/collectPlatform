package com.neusoft.gbw.cp.process.realtime.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class RecoverTaskDataHandler implements BaseEventHandler {
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.RECOVER_DATA_REPEAT_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) 
			return false;
		if(arg0 instanceof Map) {
			CollectTaskModel.getInstance().setRecoverDataMap((Map<String, String>)arg0);
			return true;
		}
		return false;
	}
	
}
