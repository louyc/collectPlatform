package com.neusoft.gbw.cp.collect.event.handler;

import java.util.Collection;

import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectInitHandler implements BaseEventHandler{
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.SYNT_MONITOR_DEVICE_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	//获取所有站点数据，进行数据缓存，方便以后做站点虚拟管道创建
	public boolean processEvent(Object arg0) {
		if (arg0 == null || !(arg0 instanceof Collection)) {
			Log.warn("数据采集服务初始化失败，无法进行正常采集");
			return false;
		}
			
		Collection<MonitorDevice> list = (Collection<MonitorDevice>)arg0;
		CollectTaskContext.getModel().load(list);
		return true;
	}
}