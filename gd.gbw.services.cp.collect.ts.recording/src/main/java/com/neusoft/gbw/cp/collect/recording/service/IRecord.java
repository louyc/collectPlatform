package com.neusoft.gbw.cp.collect.recording.service;

import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamRecord;


public interface IRecord {
	
	public void init(StreamParam info);
	
	public StreamRecord start();
	
	public void stop();

	public boolean isRealStoped();

}
