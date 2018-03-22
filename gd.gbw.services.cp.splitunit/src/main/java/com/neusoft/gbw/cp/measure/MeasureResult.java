package com.neusoft.gbw.cp.measure;

public class MeasureResult {

	private MeasureTask task;
	private String startMTime;
	private String endMTime;
	
	public MeasureTask getTask() {
		return task;
	}
	public void setTask(MeasureTask task) {
		this.task = task;
	}
	public String getStartMTime() {
		return startMTime;
	}
	public void setStartMTime(String startMTime) {
		this.startMTime = startMTime;
	}
	public String getEndMTime() {
		return endMTime;
	}
	public void setEndMTime(String endMTime) {
		this.endMTime = endMTime;
	}
	@Override
	public String toString() {
		return "MeasureResult [task=" + task.getBusObj() + ", startMTime=" + startMTime
				+ ", endMTime=" + endMTime + "]";
	}
}
