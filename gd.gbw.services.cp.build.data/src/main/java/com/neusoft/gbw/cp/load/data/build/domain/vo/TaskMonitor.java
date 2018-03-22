package com.neusoft.gbw.cp.load.data.build.domain.vo;

public class TaskMonitor {

	private String device_ip;		//收测设备IP地址
	private int port;				//收测设备通信端口
	private String monitor_code;	//收测设备代码
	private long monitor_id;			//收测设备ID
	private int version_id;			//站点协议版本 
	private int type_id;			//收测设备类型
	private int record_length;      //录音时长
	
	public int getPort() {
		return port;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getMonitor_code() {
		return monitor_code.trim();
	}
	public void setMonitor_code(String monitor_code) {
		this.monitor_code = monitor_code;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public int getVersion_id() {
		return version_id;
	}
	public void setVersion_id(int version_id) {
		this.version_id = version_id;
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
