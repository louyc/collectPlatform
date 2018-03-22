package com.neusoft.gbw.cp.collect.stream.service;

import com.neusoft.gbw.cp.collect.stream.service.transfer.CollectStreamListenerMgr;

public class CollectStreamCentre {
	
	private CollectStreamListenerMgr mgr = null;

	public void start() {
		mgr = new CollectStreamListenerMgr();
		mgr.openListener();
		
	}
	
	public void stop() {
		if(mgr != null)
			mgr.closeListener();
	}
}
