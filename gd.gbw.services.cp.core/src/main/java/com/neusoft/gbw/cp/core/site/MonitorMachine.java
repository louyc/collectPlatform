package com.neusoft.gbw.cp.core.site;

public class MonitorMachine {

	private long monitor_id;			//设备ID
	private int machine_id;			//接收机ID
	private String machine_code;	//接收机编码
	private String model_id;		//接收机型号ID
	private String model_name;		//接收机型号
	private String usage_mode;		//接收机用途 （-1全部,1 指标任务,2录音任务,3频谱任务,4频偏任务,5实时指标,6实时频谱,7实时录音）
	private int is_default;			//是否为默认接收机
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public int getMachine_id() {
		return machine_id;
	}
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}
	public String getMachine_code() {
		return machine_code;
	}
	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}
	public String getModel_id() {
		return model_id;
	}
	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public int getIs_default() {
		return is_default;
	}
	public void setIs_default(int is_default) {
		this.is_default = is_default;
	}
	public String getUsage_mode() {
		return usage_mode;
	}
	public void setUsage_mode(String usage_mode) {
		this.usage_mode = usage_mode;
	}
}
