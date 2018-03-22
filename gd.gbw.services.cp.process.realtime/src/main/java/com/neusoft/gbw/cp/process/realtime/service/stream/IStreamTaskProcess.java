package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public interface IStreamTaskProcess {
	
	public void disposeForceStart(CollectTask task);
	
	public void disposeForceStop(CollectTask task);
	
	public void disposeStart(CollectTask task);
	
	public void disposeStop(CollectTask task);
	
}
