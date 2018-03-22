package com.neusoft.np.arsf.core.schedule.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;

import java.text.ParseException;
import java.util.concurrent.ConcurrentMap;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 任务信息分类处理类。分别对即时、被动、周期、计划（星期）、计划（月份）任务信息进行处理<br>
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
public final class TaskClassify {

	private static PeriodCentre centre = PeriodCentre.getInstance();

	private static TaskConfiguration configuration = TaskConfiguration.getInstance();

	private TaskClassify() {
	}

	private static final String WARNLOG = "need necessary argument which used in plan schedule.";

	/**
	 * 对任务信息进行分类处理。
	 * 
	 * @param taskInfo 任务信息
	 * @throws NMFormateException
	 * @throws ParseException
	 * @throws SchedulerException
	 * @throws NMServerException 
	 * @throws NumberFormatException 
	 */
	protected static void processTaskInfo(TaskInfo taskInfo) throws NMFormateException, ParseException,
			SchedulerException, NumberFormatException, NMServerException {
		if (cheackTaskInfo(taskInfo)) {
			throw new NMFormateException("one of task or plan is null in given taskInfo");
		}
		switch (taskInfo.getPlan().getPlanType()) {
		case 0:
			// 即时任务,直接发送
			processImmediateTask(taskInfo);
			break;
		case 1:
			// 一般任务,直接发送(被动任务)
			processNormalTask(taskInfo);
			break;
		case 2:
			// 周期调度任务,周期调度处理流程
			processSchedulePeriodTask(taskInfo);
			break;
		case 3:
			// 计划任务,星期格式，每星期执行
			processSchedulePlanWeekTask(taskInfo);
			break;
		case 4:
			// 计划任务,月份格式，每月执行
			processSchedulePlanMonthTask(taskInfo);
			break;
		case 6:
			// 计划任务,月份格式，每月执行
			processScheduleQuartzPeriodTask(taskInfo);
			break;
		case 7:
			// 计划任务，时间点格式，某一完整时间点执行 如：2015-01-14 10:01:01 modify by jiahao
			processSchedulePlanTimePointTask(taskInfo);
			break;
		case 8:
			// 周期任务，在某一时间点开始，按照某分钟间隔周期执行    如：2015-01-14 10:01:01 开始 间隔10分钟 周期执行 modify by jiahao
			processScheduleMinutePeriodTask(taskInfo);
			break;
		case 9:
			// 周期任务，在某一时间点开始，按照某小时间隔周期执行    如：2015-01-14 10:01:01 开始 间隔1小时 周期执行 modify by jiahao
			processScheduleHourPeriodTask(taskInfo);
			break;
		default:
			throw new NMFormateException("unsupport taskScheduleType type of " + taskInfo.getPlan().getPlanType());
		}
	}

	private static boolean cheackTaskInfo(TaskInfo taskInfo) {
		return taskInfo.getPlan() == null || taskInfo.getScheduleTask() == null;
	}

	/**
	 * 即时任务,直接发送
	 */
	private static void processImmediateTask(TaskInfo taskInfo) {
		// 即时采集任务 直接发送
	}

	/**
	 * 一般任务,直接发送(被动任务)
	 */
	private static void processNormalTask(TaskInfo taskInfo) {
		// 被动采集方式 直接发送
	}

	/**
	 * 周期任务，需要进行调度。
	 * 
	 * @param taskInfo 任务信息
	 * @throws NMFormateException
	 * @throws NMServerException 
	 * @throws NumberFormatException 
	 */
	private static void processSchedulePeriodTask(TaskInfo taskInfo) throws NMFormateException, NumberFormatException,
			NMServerException {
		centre = PeriodCentre.getInstance();
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		boolean isAddOper = getOperate(operation);
		if (isAddOper) {
			if (!ScheduleUtil.checkString(taskInfo.getPlan().getDisPeriod())) {
				throw new NMFormateException("the disPeriod in taskInfo is null or empty");
			}
			String period = taskInfo.getPlan().getDisPeriod();
			if (!ScheduleUtil.checkArgument(secondaryKey, period, operation)) {
				throw new NMFormateException("one of primaryKey, secondaryKey, period or operation is null or empty");
			}
			operatedPeriodCentre(isAddOper, secondaryKey, Integer.valueOf(period), taskInfo);
		} else {
			// 删除周期任务时，没有周期参数
			if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
				throw new NMFormateException("one of primaryKey, secondaryKey or operation is null or empty");
			}
			operatedPeriodCentre(isAddOper, secondaryKey, 0, taskInfo);
		}
	}

	/**
	 * [current notice] The only method to modify PeriodCentre and currentMap of period schedule in Configuration.
	 * 
	 * @param operate 操作类型
	 * @param primaryKey 相似标识：用于合并任务
	 * @param secondaryKey 业务单元ID：标识此任务信息操作的最小单元。
	 * @param period 发现周期（以秒为单位）
	 * @param taskInfo 任务信息
	 */
	private static void operatedPeriodCentre(boolean operate, String secondaryKey, int period, TaskInfo taskInfo)
			throws NMServerException {
		String key = secondaryKey;
		configuration.getPeriodMapLocker().lock();
		try {
			ConcurrentMap<String, TaskInfo> periodMap = configuration.getPeriodMap();
			TaskInfo formerInfo = periodMap.get(key);
			// operate == true → add
			if (operate) {
				if (null != formerInfo) {
					int formerPeriod = Integer.valueOf(formerInfo.getPlan().getDisPeriod());
					centre.delPeriodInfo(secondaryKey, formerPeriod);
				}
				periodMap.put(key, taskInfo);
				centre.addPreriodInfo(secondaryKey, period);
			} else {
				// operate == false → del
				if (null != formerInfo) {
					int formerPeriod = Integer.valueOf(formerInfo.getPlan().getDisPeriod());
					centre.delPeriodInfo(secondaryKey, formerPeriod);
					Log.info("del Period. secondaryKey : " + secondaryKey + ", formerPeriod : " + formerPeriod);
				} else {
					throw new NMServerException("secondaryKey: " + secondaryKey + ", is not existing.");
				}
				periodMap.remove(key);
			}
		} finally {
			configuration.getPeriodMapLocker().unlock();
		}
	}

	/**
	 * 处理计划任务,星期格式，每星期执行
	 * 
	 * @param taskInfo 任务信息 
	 * @throws NMFormateException
	 * @throws ParseException
	 * @throws SchedulerException
	 */
	private static void processSchedulePlanWeekTask(TaskInfo taskInfo) throws NMFormateException, ParseException,
			SchedulerException {
		// 发送
		centre = PeriodCentre.getInstance();
		// 必填项为：planType、planMonth、planHour、planDay、planMinute
		if (!ScheduleUtil.checkArgument(taskInfo.getPlan().getPlanMonth(), taskInfo.getPlan().getPlanHour(), taskInfo
				.getPlan().getPlanWeek(), taskInfo.getPlan().getPlanMinute())) {
			throw new NMFormateException(WARNLOG);
		}
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
			throw new NMFormateException(WARNLOG);
		}
		String key = secondaryKey;
		operatedPlanCentre(getOperate(operation), key, taskInfo, QuartzType.Week);
	}

	private static void processSchedulePlanMonthTask(TaskInfo taskInfo) throws NMFormateException, ParseException,
			SchedulerException {
		// 发送
		centre = PeriodCentre.getInstance();
		// 必填项为：planType、planMonth、planHour、planWeek、planMinute
		if (!ScheduleUtil.checkArgument(taskInfo.getPlan().getPlanMonth(), taskInfo.getPlan().getPlanHour(), taskInfo
				.getPlan().getPlanDay(), taskInfo.getPlan().getPlanMinute())) {
			throw new NMFormateException(WARNLOG);
		}
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
			throw new NMFormateException(WARNLOG);
		}
		String key = secondaryKey;
		operatedPlanCentre(getOperate(operation), key, taskInfo, QuartzType.Month);
	}

	private static void processScheduleQuartzPeriodTask(TaskInfo taskInfo) throws NMFormateException, ParseException,
			SchedulerException {
		// 发送
		centre = PeriodCentre.getInstance();
		// 必填项为：planType、planMonth、planHour、planDay、planMinute
		if (!ScheduleUtil.checkArgument(taskInfo.getPlan().getDisPeriod())) {
			throw new NMFormateException(WARNLOG);
		}
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
			throw new NMFormateException(WARNLOG);
		}
		String key = secondaryKey;
		operatedPlanCentre(getOperate(operation), key, taskInfo, QuartzType.Period);
	}
	
	/**
	 * 增加调度计划时间点任务
	 * @param taskInfo
	 * @throws NMFormateException
	 * @throws ParseException
	 * @throws SchedulerException
	 */
	private static void processSchedulePlanTimePointTask(TaskInfo taskInfo) throws NMFormateException, ParseException, SchedulerException {
		// 发送
		centre = PeriodCentre.getInstance();
		// 必填项为：planType、planYear、planMonth、planDay、planHour、planMinute
		if (!ScheduleUtil.checkArgument(taskInfo.getPlan().getPlanYear(),taskInfo.getPlan().getPlanMonth(), taskInfo.getPlan().getPlanHour(), taskInfo
				.getPlan().getPlanDay(), taskInfo.getPlan().getPlanMinute())) {
			throw new NMFormateException(WARNLOG);
		}
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
			throw new NMFormateException(WARNLOG);
		}
		String key = secondaryKey;
		operatedPlanCentre(getOperate(operation), key, taskInfo,QuartzType.TimePoint);
	}
	
	private static void processScheduleMinutePeriodTask(TaskInfo taskInfo) throws NMFormateException, ParseException,SchedulerException {
		// 发送
		centre = PeriodCentre.getInstance();
		// 必填项为：periodStartTime,periodMinuteInterval
		if (!ScheduleUtil.checkArgument(taskInfo.getPlan().getPeriodStartTime(),taskInfo.getPlan().getPeriodMinuteInterval())) {
			throw new NMFormateException(WARNLOG);
		}
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
			throw new NMFormateException(WARNLOG);
		}
		String key = secondaryKey;
		operatedPlanCentre(getOperate(operation), key, taskInfo,QuartzType.MinutePeriod);
	}
	
	
	private static void processScheduleHourPeriodTask(TaskInfo taskInfo) throws NMFormateException, ParseException,SchedulerException {
		// 发送
		centre = PeriodCentre.getInstance();
		// 必填项为：periodStartTime,periodHourInterval
		if (!ScheduleUtil.checkArgument(taskInfo.getPlan().getPeriodStartTime(),taskInfo.getPlan().getPeriodHourInterval())) {
			throw new NMFormateException(WARNLOG);
		}
		String secondaryKey = taskInfo.getPlan().getBusinessUnitID();
		String operation = taskInfo.getPlan().getTaskOperatorType();
		if (!ScheduleUtil.checkArgument(secondaryKey, operation)) {
			throw new NMFormateException(WARNLOG);
		}
		String key = secondaryKey;
		operatedPlanCentre(getOperate(operation), key, taskInfo,QuartzType.HourPeriod);
	}

	/**
	 * [current notice] 根据计划任务中的唯一标识，处理计划任务。需要注意本方法中可能出现并发问题。
	 * 
	 * @param operate 用于针对任务管理中任务的添加和删除操作的标识。
	 * @param primaryKey 相似标识：用于合并任务
	 * @param secondaryKey 业务单元ID：标识此任务信息操作的最小单元。
	 * @param period 周期
	 * @param taskInfo 任务信息
	 */
	private static void operatedPlanCentre(boolean operate, String key, TaskInfo taskInfo, QuartzType type)
			throws ParseException, SchedulerException {
		synchronized (TaskClassify.class) {
			ConcurrentMap<String, TaskInfo> planMap = configuration.getPlanMap();
			Scheduler schedule = ScheduleFactory.getInstance().createJobSchedule();
			// operate == true → add
			if (operate) {
				// 像调度器和Configuration对象中，同时添加一份
				JobDetail job = newJob(PlanJob.class).withIdentity(key, ScheduleConstants.TASKGROUP).build();
				job.getJobDataMap().put(ScheduleConstants.JOBKEY, taskInfo.getPlan().getBusinessUnitID());
				Trigger trigger;
				switch (type) {
				case Week:
					trigger = TriggerFactory.getPlanTrigger(key, taskInfo.getPlan(), type);
					break;
				case Month:
					trigger = TriggerFactory.getMonthTrigger(key, taskInfo.getPlan(), type);
					break;
				case TimePoint://增加固定时间点执行计划
//					trigger = TriggerFactory.getTimePointTrigger(key, taskInfo.getPlan(), type);
					trigger = TriggerFactory.getTimePointTrigger(key, taskInfo, type);
					break;
				case Period:
					trigger = TriggerFactory.getPeriodTrigger(key, taskInfo.getPlan());
					break;
				case MinutePeriod:					
					// 处理 不合法参数     CalendarIntervalScheduleBuilder类       throw new IllegalArgumentException("Interval must be a positive value.");
					if("0".equals(taskInfo.getPlan().getPeriodMinuteInterval())){
						return;
					}
					trigger = TriggerFactory.getMinutePeriodTrigger(key, taskInfo.getPlan());
					break;
				case HourPeriod:
					trigger = TriggerFactory.getHourPeriodTrigger(key, taskInfo.getPlan());
					break;
				default:
					throw new IllegalArgumentException("内部错误，参数配置未统一:" + type);
				}
				if (schedule.checkExists(job.getKey())) {
					schedule.deleteJob(job.getKey());
					Log.warn("overwrite the job");
				}
				if (trigger != null) {
					schedule.scheduleJob(job, trigger);
					// last process planMap, quartz may throw exception
					planMap.put(key, taskInfo);
				}
			} else {
				// operate == false → del
				// first process planMap, quartz may throw exception
				if (planMap.containsKey(key)) {
					planMap.remove(key);
				} else {
					Log.warn("can't found the key in planMap" + key);
				}
				JobKey jobKey = jobKey(key, ScheduleConstants.TASKGROUP);
				if (schedule.checkExists(jobKey)) {
					schedule.deleteJob(jobKey);
				} else {
					Log.warn("can't found the job" + key);
				}
			}
		}
	}

	/**
	 * 获取操作类型，0代表添加，返回true，1代表删除，返回true。其他抛出异常。
	 * 
	 * @param operate 操作类型
	 * @return 0代表添加，返回true，1代表删除，返回true。
	 * @throws NMFormateException
	 */
	private static boolean getOperate(String operate) throws NMFormateException {
		if (ScheduleConstants.TASKOPERATORADD.equals(operate)) {
			return true;
		} else if (ScheduleConstants.TASKOPERATORDEL.equals(operate)) {
			return false;
		} else {
			throw new NMFormateException("the operate is not support now. The opertae is " + operate);
		}
	}

	/**
	 * 增加时间点类型，分钟周期类型和小时周期类型
	 */
	public enum QuartzType {
		Week, Month, Period,TimePoint,MinutePeriod,HourPeriod
	}
}
