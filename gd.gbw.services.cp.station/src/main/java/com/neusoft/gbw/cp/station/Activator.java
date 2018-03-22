package com.neusoft.gbw.cp.station;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.station.service.TransferMgr;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.service.event.NMSSubject;


public class Activator extends BaseActivator {

	private TransferMgr mgr = null;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		return list;
	}
	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		  if(mgr == null) {
	        	mgr = new TransferMgr();
	        	mgr.start();
	        	Log.info("模拟站点启动.........");
	        }
	}
	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();
		 if(mgr != null) {
	        	mgr.stop();
	        	Log.info("模拟站点停止..........");
	        }
	}

}
