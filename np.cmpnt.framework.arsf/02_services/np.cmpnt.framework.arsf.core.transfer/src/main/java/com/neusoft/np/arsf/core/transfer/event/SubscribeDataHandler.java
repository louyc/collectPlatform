package com.neusoft.np.arsf.core.transfer.event;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.core.transfer.service.ChannelPool;

public class SubscribeDataHandler implements BaseEventHandler{

	private String topicName;

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public boolean processEvent(Object arg0) {
		ChannelPool.getInstance().put(topicName, arg0);
		return true;
	}
}
