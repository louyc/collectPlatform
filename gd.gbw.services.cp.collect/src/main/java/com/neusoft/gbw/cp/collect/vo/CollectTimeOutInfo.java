package com.neusoft.gbw.cp.collect.vo;

import java.util.Timer;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class CollectTimeOutInfo {

	private String collectTaskID;
	private CollectTask taskInfo;
	private Timer timer;
	private boolean isTimeOut;
	
	public CollectTimeOutInfo() {
		this.isTimeOut = false;
	}
	
	public String getCollectTaskID() {
		return collectTaskID;
	}
	public void setCollectTaskID(String collectTaskID) {
		this.collectTaskID = collectTaskID;
	}

	public CollectTask getTaskInfo() {
		return taskInfo;
	}
	public void setTaskInfo(CollectTask taskInfo) {
		this.taskInfo = taskInfo;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public boolean isTimeOut() {
		return isTimeOut;
	}
	public void setTimeOut(boolean isTimeOut) {
		this.isTimeOut = isTimeOut;
	}
}
