package com.neusoft.np.arsf.core.schedule.quartz;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.math.BigDecimal;
import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台任务管理服务<br>
 * 功能描述: 调度器工厂，提供全局唯一调度器<br>
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
public class ScheduleFactory {

	private static class ScheduleFactoryHolder {
		private final static ScheduleFactory INSTANCE = new ScheduleFactory();
	}

	/**
	 * 私有构造器
	 * 
	 * @coustructor
	 */
	private ScheduleFactory() {
		init();
	}

	public void init() {
		platformStartData = new Date(System.currentTimeMillis() + 10000);
		setPlatformStartTime(platformStartData.getTime());
	}

	protected static ScheduleFactory getInstance() {
		return ScheduleFactoryHolder.INSTANCE;
	}

	/**
	 * 调度器内部类
	 */
	private Scheduler schedule;

	/**
	 * 平台启动时间，用来控制周期调度，Data形式。
	 */
	private Date platformStartData;

	/**
	 * 平台启动时间，用来控制周期调度，System.current形式。
	 */
	private BigDecimal platformStartTime;

	/**
	 * 获取周期参数，即平台启动时间。
	 * 
	 * @return 平台启动时间
	 */
	protected BigDecimal getPlatformStartTime() {
		return platformStartTime;
	}

	/**
	 * 设置周期参数，即平台启动时间。
	 * 
	 * @param platformStartTime
	 */
	protected void setPlatformStartTime(final long platformStartTime) {
		this.platformStartTime = new BigDecimal(platformStartTime);
	}

	/**
	 * 创建调度器，单例函数 ,创建失败返回空
	 * 
	 * @return 调度器
	 * @throws SchedulerException 
	 */
	protected Scheduler createJobSchedule() throws SchedulerException {
		synchronized (ScheduleFactory.class) {
			if (schedule == null) {
				final SchedulerFactory factory = new StdSchedulerFactory();
				schedule = factory.getScheduler();
			}
			return schedule;
		}
	}

	/**
	 * 获得周期调度类型的触发器。
	 * 
	 * @param taskID 任务ID
	 * @param info 周期
	 * @return 触发器
	 */
	protected Trigger buildPeriodTrigger() {
		return (SimpleTrigger) newTrigger()
				.withIdentity(ScheduleConstants.PERIOD_NAME, ScheduleConstants.PERIOD_GROUP)
				.startAt(platformStartData).withSchedule(simpleSchedule()
				.withIntervalInSeconds(ScheduleVariable.stepPeriodSecond).repeatForever()
				.withMisfireHandlingInstructionNextWithRemainingCount()).build();
	}
}
