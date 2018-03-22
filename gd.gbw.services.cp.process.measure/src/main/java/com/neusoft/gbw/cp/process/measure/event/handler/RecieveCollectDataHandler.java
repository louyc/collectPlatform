package com.neusoft.gbw.cp.process.measure.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.measure.service.DataDispatchMgr;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
/**
 * 接收采集数据处理
 * @author jh
 *
 */
public class RecieveCollectDataHandler implements BaseEventHandler {
	
	private DataDispatchMgr dataDispatcher;
	
	public RecieveCollectDataHandler(DataDispatchMgr dataDispatcher) {
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
			//增加采集数据过滤
			CollectData data = (CollectData)arg0;
			dataDispatcher.recieveDate(data);
			return true;
		}
		return false;
	}
	
}
