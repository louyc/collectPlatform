package com.neusoft.gbw.cp.core.business;

import java.util.List;

public class BusinessTask {

	private long task_id;			//收测任务ID
	private String task_name;		//任务名称，主要用于中波地方运行图任务
	private String runplan_id;		//运行图ID
	private long monitor_id;			//设备ID
	private int monitor_type;		//设备类型
	private String monitor_code;	//设备编码
	private int task_origin_id;		//运行图类型ID
	private int task_build_mode;	//任务创建方式 0:自动任务 1:手动任务
	private String freq;			//频率信息
	private int taskfreq_id;		//频率对应ID
	private int time_id;			//时间周期ID
	private String unit_begin;		//收测单元开始时间
	private String unit_end;		//收测单元结束时间
	private String recover_begin;	//设置任务回收开始时间
	private String recover_end;		//设置任务回收结束时间
	private int measure_unit_time;  //收测单元时间间隔
	private BusinessTaskType type;	//任务来源：三满、手动、中央、地方、实验
	private int manufacturer_id;	//设备提供商ID
	private String is_force;       	//是否为强制占用接收机类型数据
	private int recordLength;		//录音时长
	private int measure_status;		//收测状态， 1：第一次采集，2：补采
	private int unit_marking_type;  //收测状态，1：实时收测，2：实时采集
	private BusinessRunplan runplan;//运行图信息
	private int center_id;			//直属台ID
	
	private int task_type_id;       //效果类型
	private long orgMonitorId;       // 创建收测单元时的站点id
	private String alias_name;       //站点别名
	private String alias_code;       //站点编码  20160808  lyc
	private String delayTime; //实验延时时间  分
	private String currentTime;     //前台发送时间  针对实时音频 添加字段  lyc 20170427
	
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(String delayTime) {
		this.delayTime = delayTime;
	}
	public int getTask_type_id() {
		return task_type_id;
	}
	public void setTask_type_id(int task_type_id) {
		this.task_type_id = task_type_id;
	}
	public String getAlias_code() {
		return alias_code;
	}
	public void setAlias_code(String alias_code) {
		this.alias_code = alias_code;
	}
	public String getAlias_name() {
		return alias_name;
	}
	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}
	public long getOrgMonitorId() {
		return orgMonitorId;
	}
	public void setOrgMonitorId(long orgMonitorId) {
		this.orgMonitorId = orgMonitorId;
	}
	private String monitor_alias;   //站点别名    lyc
	private List<Long> monitor_id_list;   //站点组     效果类  多站点采集
	
	public List<Long> getMonitor_id_list() {
		return monitor_id_list;
	}
	public void setMonitor_id_list(List<Long> monitor_id_list) {
		this.monitor_id_list = monitor_id_list;
	}
	public String getMonitor_alias() {
		return monitor_alias;
	}
	public void setMonitor_alias(String monitor_alias) {
		this.monitor_alias = monitor_alias;
	}
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
	public String getMonitor_code() {
		return monitor_code;
	}
	public void setMonitor_code(String monitor_code) {
		this.monitor_code = monitor_code;
	}
	public int getTask_origin_id() {
		return task_origin_id;
	}
	public void setTask_origin_id(int task_origin_id) {
		this.task_origin_id = task_origin_id;
	}
	public int getTask_build_mode() {
		return task_build_mode;
	}
	public void setTask_build_mode(int task_build_mode) {
		this.task_build_mode = task_build_mode;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public int getTaskfreq_id() {
		return taskfreq_id;
	}
	public void setTaskfreq_id(int taskfreq_id) {
		this.taskfreq_id = taskfreq_id;
	}
	public int getTime_id() {
		return time_id;
	}
	public void setTime_id(int time_id) {
		this.time_id = time_id;
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
	public BusinessTaskType getType() {
		return type;
	}
	public void setType(BusinessTaskType type) {
		this.type = type;
	}
	public int getManufacturer_id() {
		return manufacturer_id;
	}
	public void setManufacturer_id(int manufacturer_id) {
		this.manufacturer_id = manufacturer_id;
	}
	public String getIs_force() {
		return is_force;
	}
	public void setIs_force(String is_force) {
		this.is_force = is_force;
	}

	public int getMonitor_type() {
		return monitor_type;
	}
	public void setMonitor_type(int monitor_type) {
		this.monitor_type = monitor_type;
	}
	public int getRecordLength() {
		return recordLength;
	}
	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}
	public int getMeasure_unit_time() {
		return measure_unit_time;
	}
	public void setMeasure_unit_time(int measure_unit_time) {
		this.measure_unit_time = measure_unit_time;
	}
	
	public String getRecover_begin() {
		return recover_begin;
	}
	public void setRecover_begin(String recover_begin) {
		this.recover_begin = recover_begin;
	}
	public String getRecover_end() {
		return recover_end;
	}
	public void setRecover_end(String recover_end) {
		this.recover_end = recover_end;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public int getMeasure_status() {
		return measure_status;
	}
	public void setMeasure_status(int measure_status) {
		this.measure_status = measure_status;
	}
	public int getUnit_marking_type() {
		return unit_marking_type;
	}
	public void setUnit_marking_type(int unit_marking_type) {
		this.unit_marking_type = unit_marking_type;
	}
	public BusinessRunplan getRunplan() {
		return runplan;
	}
	public void setRunplan(BusinessRunplan runplan) {
		this.runplan = runplan;
	}
	public int getCenter_id() {
		return center_id;
	}
	public void setCenter_id(int center_id) {
		this.center_id = center_id;
	}
	@Override
	public String toString() {
		return "BusinessTask [task_id=" + task_id + ", runplan_id="
				+ runplan_id + ", monitor_id=" + monitor_id + ", monitor_type="
				+ monitor_type + ", monitor_code=" + monitor_code
				+ ", task_origin_id=" + task_origin_id + ", task_build_mode="
				+ task_build_mode + ", freq=" + freq + ", taskfreq_id="
				+ taskfreq_id + ", time_id=" + time_id + ", unit_begin="
				+ unit_begin + ", unit_end=" + unit_end
				+ ", measure_unit_time=" + measure_unit_time + ", type=" + type
				+ ", manufacturer_id=" + manufacturer_id + ", is_force="
				+ is_force + ", recordLength=" + recordLength + "]";
	}
	
}
