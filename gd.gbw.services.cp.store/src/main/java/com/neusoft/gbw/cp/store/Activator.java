package com.neusoft.gbw.cp.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.store.event.handler.BatchHandler;
import com.neusoft.gbw.cp.store.service.StoreDispatcherMgr;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private StoreDispatcherMgr storeDispatcherMgr = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new BatchHandler(storeDispatcherMgr));
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPDataSourceFactory.class);
		
		if(storeDispatcherMgr == null) {
			storeDispatcherMgr  = new StoreDispatcherMgr();
			storeDispatcherMgr.start();
		}

	}

	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();

		if(storeDispatcherMgr != null) {
			storeDispatcherMgr.stop();;
		}
	}
	
}