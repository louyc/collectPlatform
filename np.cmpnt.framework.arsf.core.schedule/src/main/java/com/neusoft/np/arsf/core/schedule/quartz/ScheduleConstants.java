package com.neusoft.np.arsf.core.schedule.quartz;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 数据常量<br>
 * 创建日期: 2013年11月8日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年11月8日       黄守凯        创建
 * </pre>
 */
public interface ScheduleConstants {

	String SEMICOLON = "_|||_";

	String PERIOD_NAME = "PERIOD_NAME";

	String PERIOD_GROUP = "PERIOD_GROUP";

	String TASKOPERATORADD = "1";

	String TASKOPERATORDEL = "2";

	String TASKSCHEDULETYPE = "taskScheduleType";

	String TASKOPERATORTYPE = "taskOperatorType";

	// 管理任务，管理类别标识。0代表停止发送任务。
	String MANAGETYPEOFSTOP = "0";

	// Quartz 中使用的"组"标识
	String TASKGROUP = "arsf.core.schedule";

	// 即时：数值为0；不需要填写调度参数；
	String IMMEDIATE_SCHEDULE = "0";
	// 被动：数值为1；不需要填写调度参数；
	String NORNAL_SCHEDULE = "1";
	// 周期：数值为2；调度参数中，必填项为：planType、disPeriod；
	String PERIOD_SCHEDULE = "2";
	// 计划（月份）：数值为3；调度参数中，必填项为：planType、planMonth、planHour、planWeek、planMinute
	String PLANWEEK_SCHEDULE = "3";
	// 计划（星期）：数值为4；调度参数中，必填项为：planType、planMonth、planHour、planDay、planMinute
	String PLANMONTH_SCHEDULE = "4";
	// 周期：Quartz的周期方式
	String QUARTZ_PERIOD_SCHEDULE = "6";
	
	// 计划（时间点）：数值为7；调度参数中，必填项为：planType、planYear、planMonth、planHour、planDay、planMinute
	String PLANTIMEPOINT_SCHEDULE = "7";
	// 周期分钟（按开始时间执行后，按间隔分钟周期执行）：数值为8；调度参数中，必填项为：planType、planYear、planMonth、planHour、planDay、planMinute、planSeconds、startTime
	String PERIOD_MINITUE_SCHEDULE = "8";
	// 周期小时（按开始时间执行后，按间隔分钟周期执行）：数值为9；调度参数中，必填项为：planType、planYear、planMonth、planHour、planDay、planMinute、planSeconds、startTime
	String PERIOD_HOUR_SCHEDULE = "9";

	/*
	 *  调度参数
	 */
	String DISPERIOD = "period";

	String PLANMONTH = "planMonth";

	String PLANDAY = "planDay";

	String PLANHOUR = "planHour";

	String PLANWEEK = "planWeek";

	String PLANMINUTE = "planMinute";

	String SIMILARTARGET = "similarTarget";

	String BUSINESSUNITID = "businessUnitID";
	
	String STARTTIME = "nextTime";
	
	//modify by jiahao 周期调度开始时间
	String PERIODSTARTTIME = "periodStartTime";
	//modify by jiahao 周期调度分钟间隔
	String PERIODMINUTEINTERVAL = "periodMinuteInterval";
	//modify by jiahao 周期调度小时间隔
	String PERIODHOURINTERVAL = "periodHourInterval";
	//modify by jiahao 计划年
	String PLANYEAR = "planYear";
	//modify by jiahao 计划秒
	String PLANSECONDS = "planSeconds";
	String PLAN_SRC_TIME ="srcTime";
	// ScheduleTask解析参数
	String HANDLERID = "handlerId";
	// ScheduleTask解析参数
	String MESSAGECODE = "messageCode";

	String JOBKEY = "JobKey";

}
