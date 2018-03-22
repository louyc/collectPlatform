package com.neusoft.gbw.cp.process.realtime.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.service.DataDispatcher;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
/**
 * 采集数据接收处理
 * @author jh
 *
 */
public class RecieveCollectDataHandler implements BaseEventHandler {
	
	private DataDispatcher dataDispatcher;
	
	public RecieveCollectDataHandler(DataDispatcher dataDispatcher) {
		this.dataDispatcher = dataDispatcher;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_DATA_PROCESS_TOPIC;
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
