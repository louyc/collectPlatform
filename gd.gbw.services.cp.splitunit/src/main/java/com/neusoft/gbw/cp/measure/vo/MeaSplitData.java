package com.neusoft.gbw.cp.measure.vo;

import com.neusoft.gbw.cp.measure.MeasureTask;

public class MeaSplitData {

	private MeasureTask task;
	private String startTime;
	private String endTime;
	
	public MeaSplitData(MeasureTask task) {
		this.task = task;
	}
	public MeasureTask getTask() {
		return task;
	}
	public void setTask(MeasureTask task) {
		this.task = task;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
