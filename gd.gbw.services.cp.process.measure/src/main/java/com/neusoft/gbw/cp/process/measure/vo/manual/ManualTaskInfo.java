package com.neusoft.gbw.cp.process.measure.vo.manual;

import java.util.List;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class ManualTaskInfo {
	private List<CollectTask> manualTask;
//	private Timer timer;
	private long time;				//回收开始时间，毫秒
	private boolean taskStatus; 	//任务回收状态，0：失败，1：成功， 如果有一个失败，则全部失败
	private int taskSize;
	private int recoverSize;
	private int nullSize;   //文件空的条数
	
	public int getNullSize() {
		return nullSize;
	}
	public void setNullSize(int nullSize) {
		this.nullSize = nullSize;
	}
	public List<CollectTask> getManualTask() {
		return manualTask;
	}
	public void setManualTask(List<CollectTask> manualTask) {
		this.manualTask = manualTask;
		this.taskStatus = true; //默认状态为成功
		this.taskSize = manualTask.size();
		this.recoverSize = 0;
	}
	public int getTaskSize() {
		return taskSize;
	}
	public void setTaskSize(int taskSize) {
		this.taskSize = taskSize;
	}
	public int getRecoverSize() {
		return recoverSize;
	}
	public void setRecoverSize(int recoverSize) {
		this.recoverSize = recoverSize;
	}
//	public Timer getTimer() {
//		return timer;
//	}
//	public void setTimer(Timer timer) {
//		this.timer = timer;
//	}
	public boolean isTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(boolean taskStatus) {
		this.taskStatus = taskStatus;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
