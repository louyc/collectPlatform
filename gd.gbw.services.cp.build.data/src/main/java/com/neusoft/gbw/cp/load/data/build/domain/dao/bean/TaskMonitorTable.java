package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class TaskMonitorTable {

	private int task_id;				//任务ID
	private long monitor_id;				//收测设备ID
	private String device_ip;			//设备IP
	private int type_id;				//收测设备类型
	private String monitor_code;		//收测设备代码
	private int record_length;
	
	public String getMonitor_code() {
		return monitor_code;
	}
	public void setMonitor_code(String monitor_code) {
		this.monitor_code = monitor_code;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public int getRecord_length() {
		return record_length;
	}
	public void setRecord_length(int record_length) {
		this.record_length = record_length;
	}
}
