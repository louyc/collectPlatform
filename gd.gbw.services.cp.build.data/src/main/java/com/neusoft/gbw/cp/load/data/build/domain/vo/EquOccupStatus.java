package com.neusoft.gbw.cp.load.data.build.domain.vo;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class EquOccupStatus {

	private String equCode;
	//由于要清除内存task对象，所以先不缓存task，只缓存占用key
	private CollectTask currentTask;
	private String occupKey;
	private EquRunStatus status;
	
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public CollectTask getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(CollectTask currentTask) {
		this.currentTask = currentTask;
	}
	public EquRunStatus getStatus() {
		return status;
	}
	public void setStatus(EquRunStatus status) {
		this.status = status;
	}
	public String getOccupKey() {
		return occupKey;
	}
	public void setOccupKey(String occupKey) {
		this.occupKey = occupKey;
	}
}
