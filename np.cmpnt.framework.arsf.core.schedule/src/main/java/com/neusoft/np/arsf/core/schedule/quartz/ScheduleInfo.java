package com.neusoft.np.arsf.core.schedule.quartz;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台任务管理控制Bundle<br>
 * 功能描述: 任务周期调度对象<br>
 * 创建日期: 2012-3-1 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-3-1       黄守凯        创建
 * </pre>
 */
public class ScheduleInfo {

	/**
	 * 任务信息调度类型：即时、被动、周期、计划（星期）、计划（月份）
	 * 即时：数值为0、被动：数值为1、周期：数值为2、计划（月份）：数值为3、计划（星期）：数值为4
	 */
	private int planType;

	/**
	 * 启动时间
	 */
	private Date disTime;

	/**
	 * 发现周期（分钟）
	 */
	private String disPeriod;
	
	/**
	 * modify by jiahao 
	 * 计划中的年字段，存储为4位字符串，如2014,2015
	 */
	private String planYear;

	/**
	 * 计划中的月字段，存储为12个数字字符串，有效的设置为1，无效的设置为0。
	 */
	private String planMonth;

	/**
	 * 计划中的日字段，存储为31个数字字符串，有效的设置为1，无效的设置为0。
	 */
	private String planDay;

	/**
	 * 计划中的小时字段，存储为24个数字字符串，有效的设置为1，无效的设置为0。
	 */
	private String planHour;

	/**
	 * 计划中的周字段，存储为7个数字字符串，有效的设置为1，无效的设置为0。
	 */
	private String planWeek;

	/**
	 * 计划中的分钟字段，存储为60个数字字符串，有效的设置为1，无效的设置为0。
	 */
	private String planMinute;

	/**
	 * 业务单元ID：标识此任务信息操作的最小单元。
	 */
	private String businessUnitID;

	/**
	 * 用于针对任务管理中任务的添加和删除操作的标识。
	 */
	private String taskOperatorType;

	private String startTime;
	
	/**
	 * modify by jiahao 
	 */
	private String planSeconds;
	
	/**
	 * modify by jiahao 
	 * //周期分钟，周期小时开始执行时间（对于计划无用），格式：YYYY-MM-DD hh:mi:ss
	 */
	private String periodStartTime;

	/**
	 * modify by jiahao 
	 * //周期分钟的 分钟时间间隔
	 */
	private String periodMinuteInterval;
	
	/**
	 * modify by jiahao 
	 * //周期小时的 小时时间间隔
	 */
	private String periodHourInterval;
	
	private String srcTime;         //计算计划开始时间的源时间（格式：YYYY-MM-DD hh:mi:ss），当计划模式不同时，根据这个时间戳填充不同的ScheduleFormatType  20160818
	
	public String getSrcTime() {
		return srcTime;
	}
	public void setSrcTime(String srcTime) {
		this.srcTime = srcTime;
	}

	public int getPlanType() {
		return planType;
	}

	public void setPlanType(int planType) {
		this.planType = planType;
	}

	public Date getDisTime() {
		return disTime;
	}

	public void setDisTime(Date disTime) {
		this.disTime = disTime;
	}

	public String getDisPeriod() {
		return disPeriod;
	}

	public void setDisPeriod(String disPeriod) {
		this.disPeriod = disPeriod;
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

	public String getPlanWeek() {
		return planWeek;
	}

	public void setPlanWeek(String planWeek) {
		this.planWeek = planWeek;
	}

	public String getPlanMinute() {
		return planMinute;
	}

	public void setPlanMinute(String planMinute) {
		this.planMinute = planMinute;
	}

	public String getBusinessUnitID() {
		return businessUnitID;
	}

	public void setBusinessUnitID(String businessUnitID) {
		this.businessUnitID = businessUnitID;
	}

	public String getTaskOperatorType() {
		return taskOperatorType;
	}

	public void setTaskOperatorType(String taskOperatorType) {
		this.taskOperatorType = taskOperatorType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	protected String getPlanYear() {
		return planYear;
	}

	protected void setPlanYear(String planYear) {
		this.planYear = planYear;
	}

	protected String getPlanSeconds() {
		return planSeconds;
	}

	protected void setPlanSeconds(String planSeconds) {
		this.planSeconds = planSeconds;
	}

	protected String getPeriodStartTime() {
		return periodStartTime;
	}

	protected void setPeriodStartTime(String periodStartTime) {
		this.periodStartTime = periodStartTime;
	}

	protected String getPeriodMinuteInterval() {
		return periodMinuteInterval;
	}

	protected void setPeriodMinuteInterval(String periodMinuteInterval) {
		this.periodMinuteInterval = periodMinuteInterval;
	}

	protected String getPeriodHourInterval() {
		return periodHourInterval;
	}

	protected void setPeriodHourInterval(String periodHourInterval) {
		this.periodHourInterval = periodHourInterval;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
