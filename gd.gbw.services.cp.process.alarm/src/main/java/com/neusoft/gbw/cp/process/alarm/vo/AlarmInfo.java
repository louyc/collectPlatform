package com.neusoft.gbw.cp.process.alarm.vo;

public class AlarmInfo {

	private int monitorId;	//设备ID
	private int alarmType;	//告警类型
	private int alarmConut; //告警计数
	private String alarmBeginTime;
	
	public int getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(int monitorId) {
		this.monitorId = monitorId;
	}
	public int getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
	public int getAlarmConut() {
		return alarmConut;
	}
	public void addAlarmConut() {
		this.alarmConut = alarmConut ++;
	}
	public String getAlarmBeginTime() {
		return alarmBeginTime;
	}
	public void setAlarmBeginTime(String alarmBeginTime) {
		this.alarmBeginTime = alarmBeginTime;
	}
}
