package com.neusoft.gbw.cp.process.measure.event.handler;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class SubscribeFtpServerHandler implements BaseEventHandler {
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.FTP_SERVER_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		CollectTaskModel.getInstance().setFtpServerMap((Map<String, String>) arg0);
		return true;
	}
}