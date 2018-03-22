package com.neusoft.gbw.cp.process.measure.vo.auto;

import java.util.Timer;

public class MeaUnitCalculate {

	private int collectTaskCount = 0;
	private int recordDataCount = 0;
	private Timer timer = null;
	
	public int getCollectTaskCount() {
		return collectTaskCount;
	}
	public void setCollectTaskCount(int collectTaskCount) {
		this.collectTaskCount = collectTaskCount;
	}
	public int getRecordDataCount() {
		return recordDataCount;
	}
	public void setRecordDataCount(int recordDataCount) {
		this.recordDataCount = recordDataCount;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
