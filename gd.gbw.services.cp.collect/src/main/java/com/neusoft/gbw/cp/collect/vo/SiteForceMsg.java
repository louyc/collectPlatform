package com.neusoft.gbw.cp.collect.vo;

public class SiteForceMsg {
	private String monitorId;
	private String command;
	private String equCode;
	private String stopMarkId;
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	
	public String getStopMarkId() {
		return stopMarkId;
	}
	public void setStopMarkId(String stopMarkId) {
		this.stopMarkId = stopMarkId;
	}
	@Override
	public String toString() {
		return "SiteForceMsg [monitorId=" + monitorId + ", command=" + command
				+ ", equCode=" + equCode + "]";
	}
	
}
