package com.neusoft.gbw.cp.measure;

public class ScheduleAttr {

	private int scheduleType;	//调度类型 1：独立时间；2：循环时间
	private int dayOfWeek;		//-1:全部七天(每天) 0：周日 1：周一 …… 6：周六
	private String startTime;	//起始时间,周期任务格式：“hh:mm:ss”； 独立任务格式：“yyyy-mm-dd hh:mm:ss”；
	private String endTime;		//终止时间,周期任务格式：“hh:mm:ss”； 独立任务格式：“yyyy-mm-dd hh:mm:ss”；
	private int overhaul;		//是否是检修时间段，1 是 0 否
	
	public int getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getOverhaul() {
		return overhaul;
	}
	public void setOverhaul(int overhaul) {
		this.overhaul = overhaul;
	}
}
