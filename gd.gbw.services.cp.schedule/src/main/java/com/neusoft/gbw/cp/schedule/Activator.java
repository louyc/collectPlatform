package com.neusoft.gbw.cp.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.schedule.event.handler.CollectListTaskHandler;
import com.neusoft.gbw.cp.schedule.event.handler.CollectSingleTaskHandler;
import com.neusoft.gbw.cp.schedule.event.handler.TaskScheduleMsgHandler;
import com.neusoft.gbw.cp.schedule.event.handler.TimeRemindMsgHandler;
import com.neusoft.gbw.cp.schedule.event.handler.TimeSetMsgHandler;
import com.neusoft.gbw.cp.schedule.service.ScheduleService;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.schedule.NPScheduleService;

public class Activator extends BaseActivator {
	
	private ScheduleService scheduleService = null;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new CollectListTaskHandler(scheduleService.getmDispatchorService()));
		list.add(new CollectSingleTaskHandler(scheduleService.getmDispatchorService()));
		list.add(new TaskScheduleMsgHandler());
		list.add(new TimeSetMsgHandler());
		list.add(new TimeRemindMsgHandler());
		return list;
	}

	@Override
	public void init() {	
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPScheduleService.class);
		
		if(scheduleService == null) {
			scheduleService = new ScheduleService();
			scheduleService.start();
		}
	}

	@Override
	public void stop() {
		unbindService(NPScheduleService.class);
		unbindService(NMSSubject.class);
		unbindCoreServices();
		
		if(scheduleService != null) {
			scheduleService.stop();
		}
	}
	
}

