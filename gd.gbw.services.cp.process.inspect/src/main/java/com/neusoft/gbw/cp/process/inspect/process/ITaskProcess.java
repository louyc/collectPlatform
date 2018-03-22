package com.neusoft.gbw.cp.process.inspect.process;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.inspect.exception.InspectDisposeException;

/**
 * 版本协议处理方法
 * @author jh
 *
 */
public interface ITaskProcess {

	public void disposeV8(CollectData data) throws InspectDisposeException; 
	
	public void disposeV7(CollectData data) throws InspectDisposeException; 
	
	public void disposeV6(CollectData data) throws InspectDisposeException; 
	
	public void disposeV5(CollectData data) throws InspectDisposeException; 
}
