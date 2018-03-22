package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class MonitorInfo {

	private long monitor_id;				//收测设备ID
	private int type_id;				//收测设备类型
	private String monitor_code;		//收测设备代码
//	private int measureUnitTime;		//收测单元间隔 ，弃用，现在一个站点可能同时收测广播和实验的数据
	private int broadMeasureUnitTime;	//广播收测单元间隔
	private int experMeasureUnitTime;	//实验收测单元间隔
	private String taskRecordLength;	//录音时长
	private String taskRecordLength_gather; //采集录音时长
	private String taskRecordLength_listen; //收测录音时长
	private String device_ip;			//ip地址
	private int version_id;				//站点协议版本
	private int manufacturer_id;		//设备提供商ID
	private String protocol;			//设备通信协议
	private String port;				//Socket通信端口
	private String url;					//Servlet通信地址URL
	
	private int online_state;       //站点状态   在线 1、软件未开启 2、掉线 3、软件开启4
	private String taskControlModel;    //控制自动  手动   lyc
	private String alias_name;      //站点别名
	private String alias_code;      //站点编码
	private String shiyanDelayTime;  //实验延时时间
	private String guangboDelayTime; //广播延时时间
	private String ftp_ip;
	private String ftp_username;
	private String ftp_password;
	private String ftp_path;
	
	
	public String getFtp_ip() {
		return ftp_ip;
	}
	public void setFtp_ip(String ftp_ip) {
		this.ftp_ip = ftp_ip;
	}
	public String getFtp_username() {
		return ftp_username;
	}
	public void setFtp_username(String ftp_username) {
		this.ftp_username = ftp_username;
	}
	public String getFtp_password() {
		return ftp_password;
	}
	public void setFtp_password(String ftp_password) {
		this.ftp_password = ftp_password;
	}
	public String getFtp_path() {
		return ftp_path;
	}
	public void setFtp_path(String ftp_path) {
		this.ftp_path = ftp_path;
	}
	public String getShiyanDelayTime() {
		return shiyanDelayTime;
	}
	public void setShiyanDelayTime(String shiyanDelayTime) {
		this.shiyanDelayTime = shiyanDelayTime;
	}
	public String getGuangboDelayTime() {
		return guangboDelayTime;
	}
	public void setGuangboDelayTime(String guangboDelayTime) {
		this.guangboDelayTime = guangboDelayTime;
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
	public int getOnline_state() {
		return online_state;
	}
	public void setOnline_state(int online_state) {
		this.online_state = online_state;
	}
	public String getTaskControlModel() {
		return taskControlModel;
	}
	public void setTaskControlModel(String taskControlModel) {
		this.taskControlModel = taskControlModel;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public String getMonitor_code() {
		return monitor_code;
	}
	public void setMonitor_code(String monitor_code) {
		this.monitor_code = monitor_code;
	}
//	public int getMeasureUnitTime() {
//		return measureUnitTime;
//	}
//	public void setMeasureUnitTime(int measureUnitTime) {
//		this.measureUnitTime = measureUnitTime;
//	}
	
	public String getDevice_ip() {
		return device_ip;
	}
	public int getBroadMeasureUnitTime() {
		return broadMeasureUnitTime;
	}
	public void setBroadMeasureUnitTime(int broadMeasureUnitTime) {
		this.broadMeasureUnitTime = broadMeasureUnitTime;
	}
	public int getExperMeasureUnitTime() {
		return experMeasureUnitTime;
	}
	public void setExperMeasureUnitTime(int experMeasureUnitTime) {
		this.experMeasureUnitTime = experMeasureUnitTime;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public int getVersion_id() {
		return version_id;
	}
	public void setVersion_id(int version_id) {
		this.version_id = version_id;
	}
	public int getManufacturer_id() {
		return manufacturer_id;
	}
	public void setManufacturer_id(int manufacturer_id) {
		this.manufacturer_id = manufacturer_id;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTaskRecordLength() {
		return taskRecordLength;
	}
	public void setTaskRecordLength(String taskRecordLength) {
		this.taskRecordLength = taskRecordLength;
	}
	public String getTaskRecordLength_gather() {
		return taskRecordLength_gather;
	}
	public void setTaskRecordLength_gather(String taskRecordLength_gather) {
		this.taskRecordLength_gather = taskRecordLength_gather;
	}
	public String getTaskRecordLength_listen() {
		return taskRecordLength_listen;
	}
	public void setTaskRecordLength_listen(String taskRecordLength_listen) {
		this.taskRecordLength_listen = taskRecordLength_listen;
	}
}
