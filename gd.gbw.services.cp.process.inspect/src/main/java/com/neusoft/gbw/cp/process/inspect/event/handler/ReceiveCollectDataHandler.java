package com.neusoft.gbw.cp.process.inspect.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.inspect.service.InspectDataDisposeMgr;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveCollectDataHandler implements BaseEventHandler {
	
	private InspectDataDisposeMgr mgr = null;
	
	public ReceiveCollectDataHandler(InspectDataDisposeMgr mgr) {
		this.mgr = mgr;
	}
	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_DATA_PROCESS_TOPIC;
	}

	@Override
	public boolean processEvent(Object obj) {
		if(obj instanceof CollectData) {
			CollectData data = (CollectData)obj;
			mgr.put(data);
			return true;
		}
		return false;
	}
	
}
