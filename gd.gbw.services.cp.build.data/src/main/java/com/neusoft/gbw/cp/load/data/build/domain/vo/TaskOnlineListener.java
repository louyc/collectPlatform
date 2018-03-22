package com.neusoft.gbw.cp.load.data.build.domain.vo;

public class TaskOnlineListener {
	
	private int clientId;
	
	private int id;
	
	private int listenerId;
	
	private long monitorId;
	
	private String monitorCode;
	
	private String freq;
	
	private int band;
	
	private int scordTypeId;
	
	private String equCode;
	
	private String runplanId;
	
	private Object expandObj;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getListenerId() {
		return listenerId;
	}

	public void setListenerId(int listenerId) {
		this.listenerId = listenerId;
	}

	public long getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(long monitorId) {
		this.monitorId = monitorId;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public int getBand() {
		return band;
	}

	public void setBand(int band) {
		this.band = band;
	}

	public int getScordTypeId() {
		return scordTypeId;
	}

	public void setScordTypeId(int scordTypeId) {
		this.scordTypeId = scordTypeId;
	}

	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	
	public String getRunplanId() {
		return runplanId;
	}

	public void setRunplanId(String runplanId) {
		this.runplanId = runplanId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getMonitorCode() {
		return monitorCode;
	}

	public void setMonitorCode(String monitorCode) {
		this.monitorCode = monitorCode;
	}

	public Object getExpandObj() {
		return expandObj;
	}

	public void setExpandObj(Object expandObj) {
		this.expandObj = expandObj;
	}
	
}
