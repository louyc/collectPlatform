package com.neusoft.gbw.cp.core.collect;

public class ScheduleInfo {
	
	private OperationType operType;     	//操作类型
	private ScheduleType type;
	private ScheduleFormatType format;
	private String time;
	//format:yyyy-MM-dd HH:mm:ss
	private String effecBeginTime;
	private String effecEndTime;
	private Object expandObj;			//扩展对象
	
	public ScheduleType getType() {
		return type;
	}
	public void setType(ScheduleType type) {
		this.type = type;
	}
	public ScheduleFormatType getFormat() {
		return format;
	}
	public void setFormat(ScheduleFormatType format) {
		this.format = format;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEffecBeginTime() {
		return effecBeginTime;
	}
	public void setEffecBeginTime(String effecBeginTime) {
		this.effecBeginTime = effecBeginTime;
	}
	public String getEffecEndTime() {
		return effecEndTime;
	}
	public void setEffecEndTime(String effecEndTime) {
		this.effecEndTime = effecEndTime;
	}
	public Object getExpandObj() {
		return expandObj;
	}
	public void setExpandObj(Object expandObj) {
		this.expandObj = expandObj;
	}
	public OperationType getOperType() {
		return operType;
	}
	public void setOperType(OperationType operType) {
		this.operType = operType;
	}
	
}
