package com.neusoft.gbw.cp.process.measure.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class SyntRealMeasureSiteHandler implements BaseEventHandler{
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.REAL_MEASURE_SITE_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 == null) {
			Log.warn("实时收测站点信息初始化失败，无法进行正常采集");
		}
			
		CollectTaskModel.getInstance().setRealSiteMap((Map<String, String>) arg0);
		return true;
	}
}