package com.neusoft.gbw.cp.schedule.vo;

public class TimeRemindMsg {
	
	private int timeinterval;// 时间间隔（单位：分钟）
	private String remindtime;//提醒时间 YYYY-MM-DD hh24:mi:ss
	private String endTime;
	
	public int getTimeinterval() {
		return timeinterval;
	}
	public void setTimeinterval(int timeinterval) {
		this.timeinterval = timeinterval;
	}
	public String getRemindtime() {
		return remindtime;
	}
	public void setRemindtime(String remindtime) {
		this.remindtime = remindtime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "TimeRemindMsg [timeinterval=" + timeinterval + ", remindtime="
				+ remindtime + ", endTime=" + endTime + "]";
	}
}
