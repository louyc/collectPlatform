package com.neusoft.gbw.cp.process.realtime.vo;

public class ManualMeasureUnit {

	private String task_range_id;
	private long task_id;
	private long monitor_id;
	private String reciever_code; 
	private String runplan_id;
	private int runplan_type_id;
	private String unit_create_time;
	private String unit_collect_time;
	private String radio_url;
	private String eval_url;
	private int unit_status_id;
	private String freq;
	
	public String getTask_range_id() {
		return task_range_id;
	}
	public void setTask_range_id(String task_range_id) {
		this.task_range_id = task_range_id;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public String getReciever_code() {
		return reciever_code;
	}
	public void setReciever_code(String reciever_code) {
		this.reciever_code = reciever_code;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}
	public int getRunplan_type_id() {
		return runplan_type_id;
	}
	public void setRunplan_type_id(int runplan_type_id) {
		this.runplan_type_id = runplan_type_id;
	}
	public String getUnit_create_time() {
		return unit_create_time;
	}
	public void setUnit_create_time(String unit_create_time) {
		this.unit_create_time = unit_create_time;
	}
	public String getUnit_collect_time() {
		return unit_collect_time;
	}
	public void setUnit_collect_time(String unit_collect_time) {
		this.unit_collect_time = unit_collect_time;
	}
	public String getRadio_url() {
		return radio_url;
	}
	public void setRadio_url(String radio_url) {
		this.radio_url = radio_url;
	}
	public int getUnit_status_id() {
		return unit_status_id;
	}
	public void setUnit_status_id(int unit_status_id) {
		this.unit_status_id = unit_status_id;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getEval_url() {
		return eval_url;
	}
	public void setEval_url(String eval_url) {
		this.eval_url = eval_url;
	}
}
