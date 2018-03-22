package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class RequestMsgHandler implements BaseEventHandler{

	@Override
	public String getTopicName() {
		return EventServiceTopic.RECEIVE_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof Object) {
			
		}
		return true;
	}
}
