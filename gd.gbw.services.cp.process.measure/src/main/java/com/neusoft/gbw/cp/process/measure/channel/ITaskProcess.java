package com.neusoft.gbw.cp.process.measure.channel;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;

public interface ITaskProcess {

	public Object disposeV8(CollectData data) throws MeasureDisposeException;
	
	public Object disposeV7(CollectData data) throws MeasureDisposeException;
	
	public Object disposeV6(CollectData data) throws MeasureDisposeException;
	
	public Object disposeV5(CollectData data) throws MeasureDisposeException;
}
