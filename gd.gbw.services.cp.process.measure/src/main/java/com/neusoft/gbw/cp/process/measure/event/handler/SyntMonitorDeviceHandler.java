package com.neusoft.gbw.cp.process.measure.event.handler;

import java.util.Collection;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class SyntMonitorDeviceHandler implements BaseEventHandler{
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.SYNT_MONITOR_DEVICE_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 == null) {
			Log.warn("数据采集服务初始化失败，无法进行正常采集");
		}
			
		Collection<MonitorDevice> siteList = (Collection<MonitorDevice>)arg0;
		CollectTaskModel.getInstance().addSiteMap(siteList);
		return true;
	}
}