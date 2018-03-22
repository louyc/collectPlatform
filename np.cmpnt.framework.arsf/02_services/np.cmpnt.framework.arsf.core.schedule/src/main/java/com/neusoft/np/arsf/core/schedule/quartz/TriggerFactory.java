package com.neusoft.np.arsf.core.schedule.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMCheckArgcUtil;
import com.neusoft.np.arsf.common.util.NMDateUtil;
import com.neusoft.np.arsf.common.util.QuartzDateBuilder;
import com.neusoft.np.arsf.core.schedule.quartz.TaskClassify.QuartzType;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 获取org.quartz.Trigger，Quartz触发器，生产工厂。<br>
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
public final class TriggerFactory {

	private TriggerFactory() {
	}

	/**
	 * 获取日期类型的复杂触发器
	 * 
	 * @param taskID 任务ID
	 * @param info 周期
	 * @return 触发器
	 * @throws ParseException
	 */
	protected static Trigger getMonthTrigger(String taskID, ScheduleInfo info, QuartzType type) throws ParseException {
		Date startTime = getStartTime(info.getStartTime());
		CronTrigger trigger = newTrigger().withIdentity(taskID, ScheduleConstants.TASKGROUP).startAt(startTime)
				.withSchedule(cronSchedule(getCronExpression(info, type))).build();
		return trigger;
	}

	/**
	 * 获取星期类型的复杂触发器
	 * 
	 * @param taskID 任务ID
	 * @param info 周期
	 * @return 触发器
	 * @throws ParseException
	 */
	protected static Trigger getPlanTrigger(String taskID, ScheduleInfo info, QuartzType type) throws ParseException {
		Date startTime = getStartTime(info.getStartTime());
		CronTrigger trigger = newTrigger().withIdentity(taskID, ScheduleConstants.TASKGROUP).startAt(startTime)
				.withSchedule(cronSchedule(getCronExpression(info, type))).build();
		return trigger;
	}
	
	/**
	 * 获取时间点类型的复杂触发器
	 *  modify by jiahao
	 * @param taskID 任务ID
	 * @param info 周期
	 * @return 触发器
	 * @throws ParseException
	 */
	protected static Trigger getTimePointTrigger(String taskID, ScheduleInfo info, QuartzType type) throws ParseException {
		CronTrigger trigger = newTrigger().withIdentity(taskID, ScheduleConstants.TASKGROUP)
				.withSchedule(cronSchedule(getCronExpression(info, type))).build();
		return trigger;
	}
	
	protected static Trigger getTimePointTrigger(String taskID, TaskInfo info, QuartzType type) throws ParseException {
		
		String startime=info.getPlan().getSrcTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long srcMilliSeconds = df.parse(startime).getTime();
		if(startime != null){
			Log.debug("schedule核心 startime*&&&&&&&&&&&&&&&&&"+startime+
					"  毫秒级时间：   "+srcMilliSeconds+"    当前时间：："+ System.currentTimeMillis());
			if (srcMilliSeconds - System.currentTimeMillis() <= 3000) {	//计划时间距离开始时间不足100毫秒时，为了避免生成过期的计划，这种场景立即执行
				String messageCode = info.getPlan().getBusinessUnitID();
				TaskInfo taskInfo = TaskConfiguration.getInstance().getPeriodMap().get(messageCode);
				ScheduleTask scheduleTask = taskInfo.getScheduleTask();
				Log.info("ARSF SEND PeriodJob(INSTANT):" + scheduleTask.getHandlerId() + " - " + scheduleTask.getMessageCode());
				if(null!=scheduleTask.getHandlerId() && null!=scheduleTask.getMessageCode()){
					ARSFToolkit.sendEvent(scheduleTask.getHandlerId(), scheduleTask.getMessageCode());
				}
				return null;
			}
		}
		
		CronTrigger trigger = newTrigger().withIdentity(taskID, ScheduleConstants.TASKGROUP).startAt(df.parse(startime))
				.withSchedule(cronSchedule(getCronExpression(info.getPlan(), type))).build();
		return trigger;
	}
	

	public static Trigger getPeriodTrigger(String taskID, ScheduleInfo info) {
		Date startTime = getStartTime(info.getStartTime());
		int periodSecond = Integer.valueOf(info.getDisPeriod());
		return (SimpleTrigger) newTrigger()
				.withIdentity(taskID, ScheduleConstants.PERIOD_GROUP)
				.startAt(startTime)
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(periodSecond).repeatForever()
								.withMisfireHandlingInstructionNextWithRemainingCount()).build();
	}
	
	/**
	 * 获取分钟类型的周期复杂触发器
	 * 
	 * @param taskID 任务ID
	 * @param info 周期
	 * @return 触发器
	 * @throws ParseException
	 */
	protected static Trigger getMinutePeriodTrigger(String taskID, ScheduleInfo info) throws ParseException {
		System.out.println("info.getPeriodMinuteInterval()     TriggerFactory    "+  info.getPeriodMinuteInterval());
//		int test=0;
//		if("0".equals(info.getPeriodMinuteInterval())){
//			test=Integer.parseInt(info.getPeriodMinuteInterval()); //Integer.parseInt(info.getPeriodMinuteInterval())
//			test=1;
//		}
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(taskID, ScheduleConstants.TASKGROUP)
				.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(Integer.parseInt(info.getPeriodMinuteInterval())))
				.startAt(getStartTime(info.getPeriodStartTime())).build();
		return trigger;
	}
	
	/**
	 * 获取小时类型的周期复杂触发器
	 * modify by jiahao 重写小时周期触发，利用分钟配置小时触发时间有误差
	 * @param taskID 任务ID
	 * @param info 周期
	 * @return 触发器
	 * @throws ParseException
	 */
	protected static Trigger getHourPeriodTrigger(String taskID, ScheduleInfo info) throws ParseException {
		Trigger trigger = newTrigger().withIdentity(taskID, ScheduleConstants.TASKGROUP).startAt(NMDateUtil.dateString(info.getPeriodStartTime()))
				.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInHours(Integer.parseInt(info.getPeriodHourInterval()))).build();
		return trigger;
	}

	private static Date getStartTime(String startTime) {
		if (!NMCheckArgcUtil.checkString(startTime)) {
			return QuartzDateBuilder.evenMinuteDateAfterNow();
		}
		Date givenDate;
		try {
			givenDate = NMDateUtil.dateString(startTime);
		} catch (ParseException e) {
			Log.error("周期启动时间参数解析失败，默认下一分钟整点启动。", e);
			return QuartzDateBuilder.evenMinuteDateAfterNow();
		}
		if (givenDate.before(new Date())) {
			Log.warn("周期启动时间参数已经过期，按照下一分钟整点启动。");
			return QuartzDateBuilder.evenMinuteDateAfterNow();
		}
		return givenDate;
	}
	
	
	/**
	 * 按照顺序执行，遵循表达式的规则
	 * modify by jiahao 增加时间点执行计划，原来的只支持星期和月份计划
	 * @param 调度数据
	 * @return 调度表达式
	 */
	private static String getCronExpression(ScheduleInfo info, QuartzType type) {
		StringBuffer reStat = new StringBuffer();
		String minutes = info.getPlanMinute();
		minutes = minutes.charAt(59) + minutes.substring(0, 59);
		String minutesStr = getEachExpression(minutes, 60, true);
		String hours = info.getPlanHour();
		hours = hours.charAt(23) + hours.substring(0, 23);
		String hoursStr = getEachExpression(hours, 24, true);
		String dayWeekStr = null;
		String dayMonthStr = null;
		String dayYearStr = null;
		if (type.equals(QuartzType.Week)) {
			/*
			 * 变更顺序，把最后一位移到第一位。传入顺序：（如传入与假设不一致，需要更改）周一...周日 需要变更后的顺序为：周日周一...周六
			 */
			String weekStr = info.getPlanWeek();
			// subSequence : beginIndex - 起始索引（包括）。endIndex - 结束索引（不包括）。 
			String temp = weekStr.substring(0, 6);
			temp = weekStr.charAt(6) + temp;
			dayWeekStr = getEachExpression(temp, 7, false);
		} 
		else if(type.equals(QuartzType.Month)){
			dayMonthStr = getEachExpression(info.getPlanDay(), 31, false);
		}
		else if(type.equals(QuartzType.TimePoint)){//by jiahao 增加时间点
			dayMonthStr = getEachExpression(info.getPlanDay(), 31, false);
			dayYearStr = info.getPlanYear();
		}
		String monthStr = getEachExpression(info.getPlanMonth(), 12, false);

		// Seconds     0-59
		reStat.append("0 ");
		// Minutes     0-59
		if ("".equals(minutesStr)) {
			reStat.append("0 ");
		} else {
			reStat.append(minutesStr).append(' ');
		}
		// Hours       0-23 
		if ("".equals(hoursStr)) {
			reStat.append("0 ");
		} else {
			reStat.append(hoursStr).append(' ');
		}
		// Day-of-month        1-31  
		if (type.equals(QuartzType.Week)) {
			reStat.append("? ");
		} 
		else if(type.equals(QuartzType.Month)){
			if ("".equals(dayMonthStr)) {
				// '?'字符可以用在 day-of-month 及 day-of-week 域中，它用来表示“没有指定值”。 
				// 这对于需要指定一个或者两个域的值而不需要对其他域进行设置来说相当有用。
				reStat.append("* ");
			} else {
				reStat.append(dayMonthStr).append(' ');
			}
		}else if(type.equals(QuartzType.TimePoint)){//by jiahao 增加时间点
			reStat.append(dayMonthStr).append(' ');
		}
		// Month       1-12
		if ("".equals(monthStr)) {
			// 通配符（'*'）可以被用来表示域中“每个”可能的值。因此在"Month"域中的*表示 
			// 每个月，而在 Day-Of-Week 域中的*则表示“周中的每一天”。
			reStat.append("* ");
		} else {
			reStat.append(monthStr).append(' ');
		}
		// Day-of-Week     1-7 or SUN-SAT
		if (type.equals(QuartzType.Week)) {
			if ("".equals(dayWeekStr)) {
				// '?'字符可以用在 day-of-month 及 day-of-week 域中，它用来表示“没有指定值”。 
				// 这对于需要指定一个或者两个域的值而不需要对其他域进行设置来说相当有用。
				reStat.append("? ");
			} else {
				reStat.append(dayWeekStr).append(' ');
			}
		} 
		else if(type.equals(QuartzType.Month)){
			reStat.append("? ");
		}else if(type.equals(QuartzType.TimePoint)){//by jiahao 增加时间点
			reStat.append("? ").append(dayYearStr);
		}
		// Year (Optional)     empty, 1970-2199
		//reStat.append('?'); zhou remove it 年不能用 ？
		return reStat.toString();
	}

//	/**
//	 * 按照顺序执行，遵循表达式的规则
//	 * 
//	 * @param 调度数据
//	 * @return 调度表达式
//	 */
//	private static String getCronExpression(ScheduleInfo info, boolean isWeek) {
//		StringBuffer reStat = new StringBuffer();
//		String minutes = info.getPlanMinute();
//		minutes = minutes.charAt(59) + minutes.substring(0, 59);
//		String minutesStr = getEachExpression(minutes, 60, true);
//		String hours = info.getPlanHour();
//		hours = hours.charAt(23) + hours.substring(0, 23);
//		String hoursStr = getEachExpression(hours, 24, true);
//		String dayWeekStr = null;
//		String dayMonthStr = null;
//		if (isWeek) {
//			/*
//			 * 变更顺序，把最后一位移到第一位。传入顺序：（如传入与假设不一致，需要更改）周一...周日 需要变更后的顺序为：周日周一...周六
//			 */
//			String weekStr = info.getPlanWeek();
//			// subSequence : beginIndex - 起始索引（包括）。endIndex - 结束索引（不包括）。 
//			String temp = weekStr.substring(0, 6);
//			temp = weekStr.charAt(6) + temp;
//			dayWeekStr = getEachExpression(temp, 7, false);
//		} else {
//			dayMonthStr = getEachExpression(info.getPlanDay(), 31, false);
//		}
//		String monthStr = getEachExpression(info.getPlanMonth(), 12, false);
//
//		// Seconds     0-59
//		reStat.append("0 ");
//		// Minutes     0-59
//		if ("".equals(minutesStr)) {
//			reStat.append("0 ");
//		} else {
//			reStat.append(minutesStr).append(' ');
//		}
//		// Hours       0-23 
//		if ("".equals(hoursStr)) {
//			reStat.append("0 ");
//		} else {
//			reStat.append(hoursStr).append(' ');
//		}
//		// Day-of-month        1-31  
//		if (isWeek) {
//			reStat.append("? ");
//		} else {
//			if ("".equals(dayMonthStr)) {
//				// '?'字符可以用在 day-of-month 及 day-of-week 域中，它用来表示“没有指定值”。 
//				// 这对于需要指定一个或者两个域的值而不需要对其他域进行设置来说相当有用。
//				reStat.append("* ");
//			} else {
//				reStat.append(dayMonthStr).append(' ');
//			}
//		}
//		// Month       1-12
//		if ("".equals(monthStr)) {
//			// 通配符（'*'）可以被用来表示域中“每个”可能的值。因此在"Month"域中的*表示 
//			// 每个月，而在 Day-Of-Week 域中的*则表示“周中的每一天”。
//			reStat.append("* ");
//		} else {
//			reStat.append(monthStr).append(' ');
//		}
//		// Day-of-Week     1-7 or SUN-SAT
//		if (isWeek) {
//			if ("".equals(dayWeekStr)) {
//				// '?'字符可以用在 day-of-month 及 day-of-week 域中，它用来表示“没有指定值”。 
//				// 这对于需要指定一个或者两个域的值而不需要对其他域进行设置来说相当有用。
//				reStat.append("? ");
//			} else {
//				reStat.append(dayWeekStr).append(' ');
//			}
//		} else {
//			reStat.append("? ");
//		}
//		// Year (Optional)     empty, 1970-2199
//		//reStat.append('?'); zhou remove it 年不能用 ？
//		return reStat.toString();
//	}

	/**
	 * 生成针对每一条属性（分、小时）的子规则
	 * 
	 * @param expr 0和1组成的表达式
	 * @param length 表达式长度 
	 * @param isStartFromZero 是否从0开始,否则从1开始  zhou add 有些字段是从0开始的，有的则是从1开始
	 * @return 需要的Expression
	 */
	private static String getEachExpression(String expr, int length, boolean isStartFromZero) {
		StringBuffer reStat = new StringBuffer();
		boolean isBegin = true;
		char[] inExp = expr.toCharArray();
		for (int i = 0; i < length; i++) {
			if (inExp[i] == '0') {
				continue;
			}
			if (isBegin) {
				reStat.append(i + (isStartFromZero ? 0 : 1));
				isBegin = false;
			} else {
				reStat.append(',');
				reStat.append(i + (isStartFromZero ? 0 : 1));
			}
		}
		return reStat.toString();
	}

}
