package com.neusoft.gbw.cp.process.realtime.channel;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;

public interface ITaskProcess {

	public Object disposeV8(CollectData data) throws RealtimeDisposeException;
	
	public Object disposeV7(CollectData data) throws RealtimeDisposeException;
	
	public Object disposeV6(CollectData data) throws RealtimeDisposeException;
	
	public Object disposeV5(CollectData data) throws RealtimeDisposeException;
}
