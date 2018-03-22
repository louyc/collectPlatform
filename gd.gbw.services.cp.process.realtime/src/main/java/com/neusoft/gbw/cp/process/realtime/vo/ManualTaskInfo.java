package com.neusoft.gbw.cp.process.realtime.vo;

import java.util.List;

public class ManualTaskInfo {
	private List<Object> manualTask;
	private int taskSize;
	private ManualTaskStore delStore;
	private int recoverSize;
	
	public List<Object> getManualTask() {
		return manualTask;
	}
	public void setManualTask(List<Object> manualTask) {
		this.manualTask = manualTask;
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
		return ++recoverSize;
	}
	public ManualTaskStore getDelStore() {
		return delStore;
	}
	public void setDelStore(ManualTaskStore delStore) {
		this.delStore = delStore;
	}
}
