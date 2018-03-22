package com.neusoft.np.arsf.core.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 计划任务的动作执行类。Quartz在设定的时间，触发Job子类的execute方法，执行相应动作。
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
public class PlanJob implements Job {

	private static final TaskConfiguration CONFIG = TaskConfiguration.getInstance();

	@Override
	public void execute(final JobExecutionContext arg0) throws JobExecutionException {
		if (CONFIG.isStopPeriod()) {
			// for management action
			return;
		}
		String key = arg0.getJobDetail().getJobDataMap().getString(ScheduleConstants.JOBKEY);
		if (key == null || "".equals(key)) {
			Log.warn("PlanJob key is empty!");
			return;
		}
		TaskInfo taskInfo = CONFIG.getPlanMap().get(key);
		ScheduleTask scheduleTask = taskInfo.getScheduleTask();
		Log.info("ARSF SEND PlanJob:" + scheduleTask);
		ARSFToolkit.sendEvent(scheduleTask.getHandlerId(), scheduleTask.getMessageCode());
	}
}
