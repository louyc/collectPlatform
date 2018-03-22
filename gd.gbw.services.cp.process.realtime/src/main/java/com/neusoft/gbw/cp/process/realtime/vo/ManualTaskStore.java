package com.neusoft.gbw.cp.process.realtime.vo;

public class ManualTaskStore {
	
	private long task_id;
	private int freq_id;
	private long monitor_id;
	private String task_type;
	private int set_status;
	
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public int getFreq_id() {
		return freq_id;
	}
	public void setFreq_id(int freq_id) {
		this.freq_id = freq_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public int getSet_status() {
		return set_status;
	}
	public void setSet_status(int set_status) {
		this.set_status = set_status;
	}
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
}
