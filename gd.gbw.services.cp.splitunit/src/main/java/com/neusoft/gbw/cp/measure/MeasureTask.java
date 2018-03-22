package com.neusoft.gbw.cp.measure;

public class MeasureTask {

	private String groupKey;		//用于区分组的标识，每组检修只适用于组
	private Object busObj;			//实际业务数据
	private ScheduleAttr attr;		//调度相关属性（周期、计划）
	private int measureUnitTime;	//收测单元时间间隔
	
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public Object getBusObj() {
		return busObj;
	}
	public void setBusObj(Object busObj) {
		this.busObj = busObj;
	}
	public ScheduleAttr getAttr() {
		return attr;
	}
	public void setAttr(ScheduleAttr attr) {
		this.attr = attr;
	}
	public int getMeasureUnitTime() {
		return measureUnitTime;
	}
	public void setMeasureUnitTime(int measureUnitTime) {
		this.measureUnitTime = measureUnitTime;
	}
}
