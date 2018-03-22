package com.neusoft.gbw.cp.collect.vo;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.gbw.cp.core.site.MonitorDevice;

public class SiteConfig {

	private String monitorID;
	private int equSize;//接收机个数
	private List<String> equCodeList;//接收机代码，R1
	
	private MonitorDevice moDevice;  //站点信息
	private long timeStamp;     //时间戳
	
	public int getOnlineStatus() {
		return moDevice.getOnline_state();
	}
	public void setOnlineStatus(int onlineStatus) {
		moDevice.setOnline_state(onlineStatus);
	}
	public MonitorDevice getMoDevice() {
		return moDevice;
	}
	public void setMoDevice(MonitorDevice moDevice) {
		this.moDevice = moDevice;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public SiteConfig() {
		equCodeList = new ArrayList<String>();
	}
	public String getMonitorID() {
		return monitorID;
	}
	public void setMonitorID(String monitorID) {
		this.monitorID = monitorID;
	}
	public int getEquSize() {
		return equSize;
	}
	public void setEquSize(int equSize) {
		this.equSize = equSize;
	}
	public List<String> getEquCodeList() {
		return equCodeList;
	}
	public void addEquCode(String equCode) {
		equCodeList.add(equCode);
	}
}
