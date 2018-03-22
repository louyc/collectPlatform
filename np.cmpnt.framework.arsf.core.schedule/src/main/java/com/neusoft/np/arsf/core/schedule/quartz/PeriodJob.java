package com.neusoft.np.arsf.core.schedule.quartz;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 周期任务的动作执行类。Quartz在设定的时间，触发Job子类的execute方法，执行相应动作。
 * 	根据quartz说明，本类处理的动作不应该过分消耗时间，时间延长到一定时间后，会影响其他Job类执行execute方法<br>
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
public class PeriodJob implements Job {

	private static final TaskConfiguration CONFIG = TaskConfiguration.getInstance();

	private static final PeriodCentre PERIODCENTRE = PeriodCentre.getInstance();

	private static final ScheduleFactory SCHEDULEFACTORY = ScheduleFactory.getInstance();

	/**
	 * 预留标签，用于判断当前Job是否存在延时的情况。
	 * Quartz设定时间过小（如：每一秒判断一次是否符合周期），未经过测试。
	 */
	private static long lastStepPeriod = -1;

	@Override
	public void execute(final JobExecutionContext context) {
		try {
			process();
		} catch (Exception e) {
			Log.error("ARSF:PeriodJob执行过程中出现调度异常。", e);
		} catch (Error e) {
			Log.error("ARSF:PeriodJob执行过程中出现调度错误。", e);
		}
	}

	private void process() {
//		Log.info("执行arsf.core.schedule任务调度。" + (new Date()));
		if (CONFIG.isStopPeriod()) {
			// for management action.
			// 管理任务处理动作，停止周期任务任务和计划任务的发送动作。
			return;
		}
		long beginTime = System.currentTimeMillis();
		final long period = getPeriod();
		// Log.info("PeriodJob trigger. Now period is " + period);
		if (period == lastStepPeriod) {
			Log.warn("周期调度Job执行过程中，两次时间点计算一致，需要检查是否为：步进时间过小而导致的并发错误");
		} else {
			lastStepPeriod = period;
			final Set<String> result = PERIODCENTRE.getTriggerKeysByPeriod(period);
			if (result != null) {
				buildAndProcessTask(result);
			}
			//清除周期调度内存
			result.clear();
		}
		long lastTime = System.currentTimeMillis() - beginTime;
		if (lastTime > 500) {
			Log.warn("PeriodJob  process a job need " + lastTime);
		}
	}

	/**
	 * 获取当前周期参数。计算规则：（当前系统时间 - 系统启动时间）/1000
	 * 即：系统启动了多少秒。根据这个周期参数，判断是否触发任务。 
	 * 
	 * @return 周期参数
	 */
	private long getPeriod() {
		BigDecimal nt = new BigDecimal(System.currentTimeMillis());
		BigDecimal subt = nt.subtract(SCHEDULEFACTORY.getPlatformStartTime());
		BigDecimal pt = subt.divide(new BigDecimal(1000)).setScale(0, BigDecimal.ROUND_HALF_UP);
		return pt.longValue();
	}

	/**
	 * 根据业务规则，创建传输任务规范对象。
	 * 
	 * @param result 传输规范对象 
	 */
	private void buildAndProcessTask(final Set<String> result) {
		final Iterator<String> resultIter = result.iterator();
		while (resultIter.hasNext()) {
			// 发送
			String messageCode = resultIter.next();
			TaskInfo taskInfo = CONFIG.getPeriodMap().get(messageCode);
			ScheduleTask scheduleTask = taskInfo.getScheduleTask();
			Log.info("ARSF SEND PeriodJob:" + scheduleTask.getHandlerId() + " - " + scheduleTask.getMessageCode());
			ARSFToolkit.sendEvent(scheduleTask.getHandlerId(), scheduleTask.getMessageCode());
		}
	}
	
}
