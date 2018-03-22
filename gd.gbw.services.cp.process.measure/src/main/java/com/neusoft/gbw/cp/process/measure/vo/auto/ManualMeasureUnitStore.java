package com.neusoft.gbw.cp.process.measure.vo.auto;

public class ManualMeasureUnitStore {

	private String task_range_id;
	private long task_id;
	private long monitor_id;
	private String reciever_code; 
	private String runplan_id;
	private int runplan_type_id;
	private String unit_create_time;
	private String unit_begin;
	private String unit_end;
	private String unit_collect_time;
	private String radio_url;
	private String eval_url;
	private int unit_status_id;
	private String freq;
	private int unit_interval;
	private String uncollect_reason;
	private int unit_marking_type;
	private String monitor_alias;
	private String monitor_alias_code ;
	private long orgMonitorId;       // 创建收测单元时的站点id
	
	public String getMonitor_alias_code() {
		return monitor_alias_code;
	}
	public void setMonitor_alias_code(String monitor_alias_code) {
		this.monitor_alias_code = monitor_alias_code;
	}
	public long getOrgMonitorId() {
		return orgMonitorId;
	}
	public void setOrgMonitorId(long orgMonitorId) {
		this.orgMonitorId = orgMonitorId;
	}
	public String getMonitor_alias() {
		return monitor_alias;
	}
	public void setMonitor_alias(String monitor_alias) {
		this.monitor_alias = monitor_alias;
	}
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
	public String getUnit_begin() {
		return unit_begin;
	}
	public void setUnit_begin(String unit_begin) {
		this.unit_begin = unit_begin;
	}
	public String getUnit_end() {
		return unit_end;
	}
	public void setUnit_end(String unit_end) {
		this.unit_end = unit_end;
	}
	public int getUnit_interval() {
		return unit_interval;
	}
	public void setUnit_interval(int unit_interval) {
		this.unit_interval = unit_interval;
	}
	public String getUncollect_reason() {
		return uncollect_reason;
	}
	public void setUncollect_reason(String uncollect_reason) {
		this.uncollect_reason = uncollect_reason;
	}
	public int getUnit_marking_type() {
		return unit_marking_type;
	}
	public void setUnit_marking_type(int unit_marking_type) {
		this.unit_marking_type = unit_marking_type;
	}
}
