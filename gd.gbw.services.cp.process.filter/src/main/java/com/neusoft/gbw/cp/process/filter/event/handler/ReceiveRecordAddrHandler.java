package com.neusoft.gbw.cp.process.filter.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.filter.context.TaskMgrModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveRecordAddrHandler implements BaseEventHandler {
	
	
	public ReceiveRecordAddrHandler() {
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.RECORD_ADDRESS_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 instanceof Map) {
			Map<String, String> map = (Map<String, String>)arg0;
			if(!map.isEmpty())
				dispose(map);
		}
		return false;
	}
	
	private void dispose(Map<String, String> map) {
		TaskMgrModel.getInstance().setRecordAddr(map);
	}
}

