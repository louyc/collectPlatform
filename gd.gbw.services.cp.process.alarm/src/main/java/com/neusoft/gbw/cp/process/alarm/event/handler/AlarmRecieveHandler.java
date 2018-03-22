package com.neusoft.gbw.cp.process.alarm.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.alarm.service.AlarmService;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class AlarmRecieveHandler implements BaseEventHandler {
	
	private AlarmService service = null;
	
	public AlarmRecieveHandler(AlarmService service) {
		this.service = service;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.MONITOR_ALARM_DISPOSE_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) {
			return false;
		}
		service.put((EquAlarm)arg0);
		return true;
	}
	
}
