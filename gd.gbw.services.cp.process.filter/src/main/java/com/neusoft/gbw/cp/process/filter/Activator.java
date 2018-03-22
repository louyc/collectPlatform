package com.neusoft.gbw.cp.process.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.process.filter.event.handler.ReceiveCollectTaskHandler;
import com.neusoft.gbw.cp.process.filter.event.handler.ReceiveRecordAddrHandler;
import com.neusoft.gbw.cp.process.filter.event.handler.RecieveCollectDataHandler;
import com.neusoft.gbw.cp.process.filter.event.handler.SubscribeFtpServerHandler;
import com.neusoft.gbw.cp.process.filter.service.DataDispatchMgr;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class Activator extends BaseActivator {
	
	private DataDispatchMgr dataDisMgr;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new RecieveCollectDataHandler(dataDisMgr));
		list.add(new ReceiveCollectTaskHandler());
		
		list.add(new ReceiveRecordAddrHandler());
		list.add(new SubscribeFtpServerHandler());
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
//		bindService(NMSSubject.class);
		
		if (dataDisMgr == null) {
			dataDisMgr = new DataDispatchMgr();
			dataDisMgr.start();
		}
	}

	@Override
	public void stop() {
//		unbindService(NMSSubject.class);
		unbindCoreServices();
		
		if (dataDisMgr != null) {
			dataDisMgr.stop();
		} 
	}
}

