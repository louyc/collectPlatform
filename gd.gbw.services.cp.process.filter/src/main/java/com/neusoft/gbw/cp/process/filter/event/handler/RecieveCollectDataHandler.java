package com.neusoft.gbw.cp.process.filter.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.filter.service.DataDispatchMgr;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class RecieveCollectDataHandler implements BaseEventHandler {
	
	private DataDispatchMgr dataDispatcher;
	
	public RecieveCollectDataHandler(DataDispatchMgr dataDispatcher) {
		this.dataDispatcher = dataDispatcher;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.REPORT_COLLECT_DATA_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) 
			return false;
		
		if(arg0 instanceof CollectData) {
			CollectData data = (CollectData)arg0;
			dataDispatcher.recieveDate(data);
			return true;
		}
		return false;
	}
	
}
