package com.neusoft.gbw.cp.process.inspect.service;

import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.service.transfer.TransferInspectService;

public class InspectCentre {
	
	private InspectDataDisposeMgr mgr = null;
	private TransferInspectService service = null;

	private void init() {
		InspectTaskContext.getInstance().loadRealtimeProcess();
	}
	
	public void start() {
		init();
		//日常巡检
		mgr = new InspectDataDisposeMgr();
		mgr.start();
		//站点连通性巡检
		service = new TransferInspectService();
		service.init();
		service.start();
	}
	
	public void stop() {
		if(mgr != null)
			mgr.stop();
		
		if(service != null)
			service.stop();
	}

	public InspectDataDisposeMgr getMgr() {
		return mgr;
	}

	public TransferInspectService getService() {
		return service;
	}
	
}
