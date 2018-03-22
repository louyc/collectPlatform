package com.neusoft.gbw.cp.process.realtime.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.realtime.service.transfer.equ.EquAlarmListenerMgr;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class ReceiveEquAlarmHandler  implements BaseEventHandler {

	private EquAlarmListenerMgr mgr = null;
	
	
	public ReceiveEquAlarmHandler(EquAlarmListenerMgr mgr) {
		this.mgr = mgr;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_ALARM_TOPIC;
	}

	@Override
	public boolean processEvent(Object obj) {
		if(obj != null && obj instanceof String) {
			try {
				mgr.put(obj.toString());
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}
		return false;
	}

}
