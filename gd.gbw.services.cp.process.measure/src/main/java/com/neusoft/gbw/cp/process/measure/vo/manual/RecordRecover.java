package com.neusoft.gbw.cp.process.measure.vo.manual;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class RecordRecover {
	
	private CollectTask data;
	
	private long time;
	
	private int recordSize;
	
	private int recoverSize;
	
	private boolean recordStatus; 

	public CollectTask getData() {
		return data;
	}

	public void setData(CollectTask data) {
		this.data = data;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getRecoverSize() {
		return recoverSize;
	}

	public void setRecoverSize(int recoverSize) {
		this.recoverSize = recoverSize;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

//	public Timer getTimer() {
//		return timer;
//	}
//
//	public void setTimer(Timer timer) {
//		this.timer = timer;
//	} 
}
