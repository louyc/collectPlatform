package com.neusoft.np.arsf.core.schedule.vo;

public class ScheduleMsg {
	
	private String taskScheduleType;//0:即时，1：被动，2：周期，3、计划（月份）4：计划（周期），5、计划（时间点），6、周期（按分钟间隔执行），7、周期（按小时间隔执行）
	private String planYear;
	private String planMonth;
	private String planDay;
	private String planHour;
	private String planMinute;
	private String planSeconds;
	private String planWeek;
	private String handlerId;//回来消息的主题
	private String messageCode;//存对应任务id
	private String planType;
	
	private String disPeriod;//发现周期，以秒为单位，脱离其他属性单独周期轮寻使用
	
	private String periodStartTime;//周期分钟，周期小时开始执行时间（对于计划无用），格式：YYYY-MM-DD hh:mi:ss
	private String periodMinuteInterval;//周期分钟的 分钟时间间隔
	private String periodHourInterval;//周期小时的 小时时间间隔
	private String srcTime;         //计算计划开始时间的源时间（格式：YYYY-MM-DD hh:mi:ss），当计划模式不同时，根据这个时间戳填充不同的ScheduleFormatType  20160818
	
	public String getSrcTime() {
		return srcTime;
	}
	public void setSrcTime(String srcTime) {
		this.srcTime = srcTime;
	}
	
	public String getPeriodMinuteInterval() {
		return periodMinuteInterval;
	}
	public void setPeriodMinuteInterval(String periodMinuteInterval) {
		this.periodMinuteInterval = periodMinuteInterval;
	}
	public String getPeriodHourInterval() {
		return periodHourInterval;
	}
	public void setPeriodHourInterval(String periodHourInterval) {
		this.periodHourInterval = periodHourInterval;
	}
	public String getPeriodStartTime() {
		return periodStartTime;
	}
	public void setPeriodStartTime(String periodStartTime) {
		this.periodStartTime = periodStartTime;
	}
	public String getTaskScheduleType() {
		return taskScheduleType;
	}
	public void setTaskScheduleType(String taskScheduleType) {
		this.taskScheduleType = taskScheduleType;
	}
	public String getPlanYear() {
		return planYear;
	}
	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}
	public String getPlanMonth() {
		return planMonth;
	}
	public void setPlanMonth(String planMonth) {
		this.planMonth = planMonth;
	}
	public String getPlanDay() {
		return planDay;
	}
	public void setPlanDay(String planDay) {
		this.planDay = planDay;
	}
	public String getPlanHour() {
		return planHour;
	}
	public void setPlanHour(String planHour) {
		this.planHour = planHour;
	}
	public String getPlanMinute() {
		return planMinute;
	}
	public void setPlanMinute(String planMinute) {
		this.planMinute = planMinute;
	}
	public String getPlanSeconds() {
		return planSeconds;
	}
	public void setPlanSeconds(String planSeconds) {
		this.planSeconds = planSeconds;
	}
	public String getPlanWeek() {
		return planWeek;
	}
	public void setPlanWeek(String planWeek) {
		this.planWeek = planWeek;
	}
	public String getDisPeriod() {
		return disPeriod;
	}
	public void setDisPeriod(String disPeriod) {
		this.disPeriod = disPeriod;
	}
	public String getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
}
