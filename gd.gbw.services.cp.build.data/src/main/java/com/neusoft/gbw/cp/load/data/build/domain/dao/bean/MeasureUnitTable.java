package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class MeasureUnitTable {
	private long task_unit_id;
	private long task_id;
	private String runplan_id;
	private long monitor_id;
	private int unit_status_id;
	private String unit_create_time;
	private String unit_collect_time;
	private String unit_start_time;
	private String eval_url;
	private int task_build_mode;
	
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public int getUnit_status_id() {
		return unit_status_id;
	}
	public void setUnit_status_id(int unit_status_id) {
		this.unit_status_id = unit_status_id;
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
	public String getEval_url() {
		return eval_url;
	}
	public void setEval_url(String eval_url) {
		this.eval_url = eval_url;
	}
	public int getTask_build_mode() {
		return task_build_mode;
	}
	public void setTask_build_mode(int task_build_mode) {
		this.task_build_mode = task_build_mode;
	}
	public long getTask_unit_id() {
		return task_unit_id;
	}
	public void setTask_unit_id(long task_unit_id) {
		this.task_unit_id = task_unit_id;
	}
	public String getUnit_start_time() {
		return unit_start_time;
	}
	public void setUnit_start_time(String unit_start_time) {
		this.unit_start_time = unit_start_time;
	}
}
