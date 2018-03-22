package com.neusoft.gbw.cp.store.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.store.service.StoreDispatcherMgr;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class BatchHandler implements BaseEventHandler{

	private StoreDispatcherMgr storeDispatcherMgr = null;
	
	
	public BatchHandler(StoreDispatcherMgr storeDispatcherMgr) {
		this.storeDispatcherMgr = storeDispatcherMgr;
	}
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.STORE_DATA_TOPIC;
	}

	@Override
	public boolean processEvent(Object eventData) {
		if (eventData == null) {
			return false;
		}
		if (eventData instanceof StoreInfo) {
			storeDispatcherMgr.recieveData((StoreInfo)eventData);
		}
		if(eventData instanceof Map){
			storStatus((Map)eventData);
		}
		return true;
	}
	
	public void storStatus(Map<String,Object> eventData){
		StoreInfo info = new StoreInfo();
		info.setDataMap(eventData);
		info.setLabel("updateMeasureAutoFailStatus");
		storeDispatcherMgr.recieveData(info);
		
	}
}
