package com.neusoft.gbw.cp.collect.event.handler;

import java.util.Collection;
import java.util.HashMap;

import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.vo.SiteConfig;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class MonitorAlarmHandler implements BaseEventHandler{
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.MONITOR_STATUS_NOTIFY_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	//获取所有站点数据，进行数据缓存，方便以后做站点虚拟管道创建
	public boolean processEvent(Object arg0) {
		if (arg0 == null) {
			Log.warn("站点在线状态更新失败，对正常采集会有影响");
			return false;
		}
		HashMap<Long,Integer> info = (HashMap<Long,Integer>)arg0;
		SiteConfig config = null;
		try{
			for(long monitorId: info.keySet()){
				int status=info.get(monitorId);
				config = CollectTaskContext.getModel().getSiteConfig(String.valueOf(monitorId));
				config.setOnlineStatus(status);
			}
		}catch(Exception e){
			Log.warn("站点在线状态更新失败，对正常采集会有影响");
			return false;
		}
		return true;
	}
}