package com.neusoft.gbw.cp.load.data.build.domain.vo;

public class MeasureUnitTime {

	private long monitorID;			//设备ID
	private int timeInterval;		//对应类的时间间隔
	private int unitType;			//单元间隔类型，3：广播类型， 4:实验类型
	
	public long getMonitorID() {
		return monitorID;
	}
	public void setMonitorID(long monitorID) {
		this.monitorID = monitorID;
	}
	public int getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	public int getUnitType() {
		return unitType;
	}
	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}
	@Override
	public String toString() {
		return "MeasureUnitTime [monitorID=" + monitorID + ", timeInterval="
				+ timeInterval + ", unitType=" + unitType + "]";
	}

}
