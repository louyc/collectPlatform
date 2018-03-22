package com.neusoft.gbw.cp.process.realtime.vo;

import java.util.Timer;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class TaskTimeOutInfo {
	private CollectTask task;
	private Timer timer;
	private boolean isTimeOut;
	
	public TaskTimeOutInfo() {
		this.isTimeOut = false;
	}
	
	public CollectTask getTask() {
		return task;
	}
	public void setTask(CollectTask task) {
		this.task = task;
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
