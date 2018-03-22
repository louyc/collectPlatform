package com.neusoft.gbw.cp.process.realtime.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.service.stream.StreamDispatchProcess;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class StreamTaskHandler implements BaseEventHandler {
	
	private StreamDispatchProcess process = null;
	
	
	public StreamTaskHandler() {
		this.process = new StreamDispatchProcess();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.STREAM_RECORD_TASK_POCESS_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) 
			return false;
		if(arg0 instanceof CollectTask) 
			process.dispose((CollectTask)arg0);
		return true;
	}
	
}
