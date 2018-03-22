package com.neusoft.gbw.cp.process.realtime.vo;

import java.io.Serializable;

public class EquHardAlarm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5803615723350619508L;

	private String deivceIp;
	private String deviceId;
	private String equCode;
	private String alarmID;
	private int alarmMode;
	private int alarmType;
	private String alarmDesc;
	private String alarmReason;
	private String alarmTime;
	//Type=1时填充以下属性
	private String outputLineLevel;
	private String inputLineLevel;
	private String lineFrequency;
	private String batteryLevel;
	private String upsStatus;
	
	public EquHardAlarm() {
		this.outputLineLevel = "";
		this.inputLineLevel = "";
		this.lineFrequency = "";
		this.batteryLevel = "";
		this.upsStatus = "";
	}
	
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String getAlarmID() {
		return alarmID;
	}
	public void setAlarmID(String alarmID) {
		this.alarmID = alarmID;
	}
	public int getAlarmMode() {
		return alarmMode;
	}
	public void setAlarmMode(int alarmMode) {
		this.alarmMode = alarmMode;
	}
	public int getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmDesc() {
		return alarmDesc;
	}
	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}
	public String getAlarmReason() {
		return alarmReason;
	}
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getOutputLineLevel() {
		return outputLineLevel;
	}
	public void setOutputLineLevel(String outputLineLevel) {
		this.outputLineLevel = outputLineLevel;
	}
	public String getInputLineLevel() {
		return inputLineLevel;
	}
	public void setInputLineLevel(String inputLineLevel) {
		this.inputLineLevel = inputLineLevel;
	}
	public String getLineFrequency() {
		return lineFrequency;
	}
	public void setLineFrequency(String lineFrequency) {
		this.lineFrequency = lineFrequency;
	}
	public String getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	public String getUpsStatus() {
		return upsStatus;
	}
	public void setUpsStatus(String upsStatus) {
		this.upsStatus = upsStatus;
	}
	public String getDeivceIp() {
		return deivceIp;
	}
	public void setDeivceIp(String deivceIp) {
		this.deivceIp = deivceIp;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("AlarmID=" + alarmID + ",");
		buffer.append("EquCode=" + equCode + ",");
		buffer.append("DeivceIp=" + deivceIp + ",");
		buffer.append("AlarmType=" + alarmType + ",");
		buffer.append("AlarmMode=" + alarmMode + ",");
		buffer.append("AlarmDesc=" + alarmDesc + ",");
		buffer.append("AlarmReason=" + alarmReason + ",");
		buffer.append("AlarmTime=" + alarmTime + ",");
		buffer.append("OutputLineLevel=" + outputLineLevel + ",");
		buffer.append("InputLineLevel=" + inputLineLevel + ",");
		buffer.append("LineFrequency=" + lineFrequency + ",");
		buffer.append("BatteryLevel=" + batteryLevel + ",");
		buffer.append("UPSStatus=" + upsStatus);
		
		return buffer.toString();
	}
}
