package com.neusoft.gbw.cp.collect.recording.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.collect.recording.monitor.event.handler.MonitorResetMsgHandler;
import com.neusoft.gbw.cp.collect.recording.monitor.event.handler.ThreadListenerHandler;
import com.neusoft.gbw.cp.collect.recording.monitor.event.handler.TimeRemindMsgHandler;
import com.neusoft.gbw.cp.collect.recording.monitor.mgr.MonitorMgr;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.schedule.NPScheduleService;

public class Activator extends BaseActivator {
	
	private MonitorMgr mgr = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new TimeRemindMsgHandler());
		list.add(new ThreadListenerHandler());
		list.add(new MonitorResetMsgHandler());
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPScheduleService.class);
		if(mgr == null) {
			mgr = new MonitorMgr();
			mgr.init();
			mgr.start();
		}
		
	}

	@Override
	public void stop() {
		unbindCoreServices();
		mgr.stop();
	}
	
}
