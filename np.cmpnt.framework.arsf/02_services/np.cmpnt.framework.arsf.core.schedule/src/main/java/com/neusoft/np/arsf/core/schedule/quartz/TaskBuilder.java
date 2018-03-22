package com.neusoft.np.arsf.core.schedule.quartz;

import java.util.Map;

import com.neusoft.np.arsf.common.util.NMFormateException;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 传输规范对象数据验证与创建<br>
 * 创建日期: 2012-6-13 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-6-13       黄守凯        创建
 * </pre>
 */
public final class TaskBuilder {

	private TaskBuilder() {
	}

	/**
	 * 根据接入规范中的数据，创建任务管理内部的任务信息对象TaskInfo，包含传输规范和调度参数。
	 *  
	 * @param taskPlanMap 根据接入规范生成的数据map
	 * @return 任务信息对象，包含传输规范和调度参数。
	 * @throws NMFormateException
	 */
	public static TaskInfo buildTaskInfo(Map<String, String> taskPlanMap) throws NMFormateException {
		ScheduleTask task = getScheduleTask(taskPlanMap);
		ScheduleInfo info = getInfo(taskPlanMap);
		return new TaskInfo(task,info);
	}

	private static ScheduleTask getScheduleTask(Map<String, String> taskPlanMap) {
		ScheduleTask task = new ScheduleTask();
		task.setHandlerId(taskPlanMap.containsKey(ScheduleConstants.HANDLERID) ? taskPlanMap
				.get(ScheduleConstants.HANDLERID) : "");
		task.setMessageCode(taskPlanMap.containsKey(ScheduleConstants.MESSAGECODE) ? taskPlanMap
				.get(ScheduleConstants.MESSAGECODE) : "");
		return task;
	}

	/**
	 * 创建调度周期
	 * 
	 * @param taskPlanMap 接收任务规范xml中数据内容
	 * @return 数据传输规范
	 * @throws TaskException
	 */
	private static ScheduleInfo getInfo(Map<String, String> taskPlanMap) throws NMFormateException {
		ScheduleInfo info = new ScheduleInfo();
		String type = taskPlanMap.containsKey(ScheduleConstants.TASKSCHEDULETYPE) ? taskPlanMap
				.get(ScheduleConstants.TASKSCHEDULETYPE) : "";
		info.setPlanType(getScheduleType(type));
		info.setDisPeriod(taskPlanMap.containsKey(ScheduleConstants.DISPERIOD) ? taskPlanMap
				.get(ScheduleConstants.DISPERIOD) : "");
		info.setPlanDay(taskPlanMap.containsKey(ScheduleConstants.PLANDAY) ? taskPlanMap.get(ScheduleConstants.PLANDAY)
				: "");
		info.setPlanHour(taskPlanMap.containsKey(ScheduleConstants.PLANHOUR) ? taskPlanMap
				.get(ScheduleConstants.PLANHOUR) : "");
		info.setPlanMinute(taskPlanMap.containsKey(ScheduleConstants.PLANMINUTE) ? taskPlanMap
				.get(ScheduleConstants.PLANMINUTE) : "");
		info.setPlanMonth(taskPlanMap.containsKey(ScheduleConstants.PLANMONTH) ? taskPlanMap
				.get(ScheduleConstants.PLANMONTH) : "");
		info.setPlanWeek(taskPlanMap.containsKey(ScheduleConstants.PLANWEEK) ? taskPlanMap
				.get(ScheduleConstants.PLANWEEK) : "");
		info.setBusinessUnitID(taskPlanMap.containsKey(ScheduleConstants.MESSAGECODE) ? taskPlanMap
				.get(ScheduleConstants.MESSAGECODE) : "");
		info.setTaskOperatorType(taskPlanMap.containsKey(ScheduleConstants.TASKOPERATORTYPE) ? taskPlanMap
				.get(ScheduleConstants.TASKOPERATORTYPE) : "");
		info.setStartTime(taskPlanMap.containsKey(ScheduleConstants.STARTTIME) ? taskPlanMap
				.get(ScheduleConstants.STARTTIME) : "");
		
		info.setPeriodStartTime(taskPlanMap.containsKey(ScheduleConstants.PERIODSTARTTIME) ? taskPlanMap.get(ScheduleConstants.PERIODSTARTTIME)
				: "");//modify by jiahao 
		info.setPeriodMinuteInterval(taskPlanMap.containsKey(ScheduleConstants.PERIODMINUTEINTERVAL) ? taskPlanMap.get(ScheduleConstants.PERIODMINUTEINTERVAL)
				: "");//modify by jiahao 
		info.setPeriodHourInterval(taskPlanMap.containsKey(ScheduleConstants.PERIODHOURINTERVAL) ? taskPlanMap.get(ScheduleConstants.PERIODHOURINTERVAL)
				: "");//modify by jiahao 
		info.setPlanYear(taskPlanMap.containsKey(ScheduleConstants.PLANYEAR) ? taskPlanMap.get(ScheduleConstants.PLANYEAR)
				: "");//modify by jiahao
		info.setPlanDay(taskPlanMap.containsKey(ScheduleConstants.PLANDAY) ? taskPlanMap.get(ScheduleConstants.PLANDAY)
				: "");
		info.setPlanSeconds(taskPlanMap.containsKey(ScheduleConstants.PLANSECONDS) ? taskPlanMap.get(ScheduleConstants.PLANSECONDS)
				: "");//modify by jiahao 暂时先不加秒
		info.setSrcTime(taskPlanMap.containsKey(ScheduleConstants.PLAN_SRC_TIME) ? taskPlanMap.get(ScheduleConstants.PLAN_SRC_TIME)
				: "");//20160818
		return info;
	}

	/**
	 * 获取调度类型。
	 * 任务信息调度类型：即时、被动、周期、计划（星期）、计划（月份）
	 * 即时：数值为0；不需要填写调度参数；
	 * 被动：数值为1；不需要填写调度参数；
	 * 周期：数值为2；调度参数中，必填项为：planType、disPeriod；
	 * 计划（月份）：数值为3；调度参数中
	 * 计划（星期）：数值为4；调度参数中
	 * 
	 * @param type 类型
	 * @return 调度类型，内部
	 * @throws NMFormateException
	 */
	private static int getScheduleType(String type) throws NMFormateException {
		if (ScheduleConstants.IMMEDIATE_SCHEDULE.equals(type)) {
			return 0;
		} else if (ScheduleConstants.NORNAL_SCHEDULE.equals(type)) {
			return 1;
		} else if (ScheduleConstants.PERIOD_SCHEDULE.equals(type)) {
			return 2;
		} else if (ScheduleConstants.PLANWEEK_SCHEDULE.equals(type)) {
			return 3;
		} else if (ScheduleConstants.PLANMONTH_SCHEDULE.equals(type)) {
			return 4;
		} else if (ScheduleConstants.QUARTZ_PERIOD_SCHEDULE.equals(type)) {
			return 6;
		//modify by jiahao
		} else if (ScheduleConstants.PLANTIMEPOINT_SCHEDULE.equals(type)) {
			return 7;
		//modify by jiahao
		} else if (ScheduleConstants.PERIOD_MINITUE_SCHEDULE.equals(type)) {
			return 8;
		//modify by jiahao
		} else if (ScheduleConstants.PERIOD_HOUR_SCHEDULE.equals(type)) {
			return 9;
		}else {
			throw new NMFormateException("未知周期类型：" + type);
		}
	}

}
