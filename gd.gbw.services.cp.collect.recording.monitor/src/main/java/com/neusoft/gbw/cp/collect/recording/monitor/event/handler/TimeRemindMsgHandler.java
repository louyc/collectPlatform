package com.neusoft.gbw.cp.collect.recording.monitor.event.handler;

import com.neusoft.gbw.cp.collect.recording.monitor.comstant.MoniContants;
import com.neusoft.gbw.cp.collect.recording.monitor.mgr.RecordMonitorProcess;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class TimeRemindMsgHandler  implements BaseEventHandler{

	private RecordMonitorProcess process = null;
	
	@Override
	public String getTopicName() {
		return MoniContants.MONITOR_TIME_REMIND_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) {
			return false;
		}
		if(arg0 instanceof String) {
			process = new RecordMonitorProcess();
			new Thread(process).start();
		}
		return true;
	}

}
