package com.neusoft.gbw.cp.schedule.vo;

public class TimeSetMsg {
	
	private String remindTopic;		//提醒发送主题
	private int timeinterval;		//分钟或者小时
	private String remindTime;		//设置开始时间 YYYY-MM-DD hh24:mi:ss
	private String timeUnit;       	//0:分钟 ，1：小时，不赋值：分钟
	
	public int getTimeinterval() {
		return timeinterval;
	}
	public void setTimeinterval(int timeinterval) {
		this.timeinterval = timeinterval;
	}
	public String getRemindTopic() {
		return remindTopic;
	}
	public void setRemindTopic(String remindTopic) {
		this.remindTopic = remindTopic;
	}
	public String getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	@Override
	public String toString() {
		return "TimeSetMsg [remindTopic=" + remindTopic + ", timeinterval="
				+ timeinterval + ", remindTime=" + remindTime + ", timeUnit="
				+ timeUnit + "]";
	}
	
}
