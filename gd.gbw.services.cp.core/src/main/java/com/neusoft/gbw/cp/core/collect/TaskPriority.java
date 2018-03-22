package com.neusoft.gbw.cp.core.collect;

public class TaskPriority {

	private int collectPriority;		//总体采集的优先级，也是控制优先级队列
	private long measurePriority;		//收测单元任务的采集优先级
	
	public int getCollectPriority() {
		return collectPriority;
	}
	public void setCollectPriority(int collectPriority) {
		this.collectPriority = collectPriority;
	}
	public long getMeasurePriority() {
		return measurePriority;
	}
	public void setMeasurePriority(long measurePriority) {
		this.measurePriority = measurePriority;
	}
	
	@Override
	public String toString() {
		return "TaskPriority [collectPriority=" + collectPriority
				+ ", measurePriority=" + measurePriority + "]";
	}
}
