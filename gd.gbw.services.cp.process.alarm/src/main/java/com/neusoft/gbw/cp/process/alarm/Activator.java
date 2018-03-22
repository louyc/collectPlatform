package com.neusoft.gbw.cp.process.alarm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.process.alarm.event.handler.AlarmRecieveHandler;
import com.neusoft.gbw.cp.process.alarm.service.AlarmService;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private AlarmService service = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new AlarmRecieveHandler(service));
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPDataSourceFactory.class);
		
		if(service == null) {
			service = new AlarmService();
			service.init();
			service.start();
		}
			
	}
	
	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();
		
		if(service != null) {
			service.stop();
		}
	}
}
