package com.neusoft.np.arsf.core.schedule.quartz;

import static org.quartz.JobBuilder.newJob;

import java.util.concurrent.BlockingQueue;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 调度组件控制<br>
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
public class SchedulerCentre extends NMService {

	private static final ScheduleFactory SCHEDULEFACTORY = ScheduleFactory.getInstance();

	private static Scheduler schedule;

	private BlockingQueue<TaskInfo> input;

	private BlockingQueue<ScheduleTask> output;

	public SchedulerCentre() {
	}

	public SchedulerCentre(BlockingQueue<TaskInfo> input, BlockingQueue<ScheduleTask> output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * 线程初始化部分。
	 * 
	 * @return
	 */
	public boolean initPeriod() {
		Log.info("Task Service : TaskControlThread init");
		try {
			schedule = SCHEDULEFACTORY.createJobSchedule();
			schedule.start();
			JobDetail job = newJob(PeriodJob.class).withIdentity(ScheduleConstants.PERIOD_NAME,
					ScheduleConstants.PERIOD_GROUP).build();
			Trigger trigger = SCHEDULEFACTORY.buildPeriodTrigger();
			schedule.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			Log.error("init schedule exception. ", e);
			return false;
		} catch (Exception e) {
			Log.error("init schedule unknow exception. ", e);
			return false;
		}
		return true;
	}

	public BlockingQueue<TaskInfo> getInput() {
		return input;
	}

	public void setInput(BlockingQueue<TaskInfo> input) {
		this.input = input;
	}

	public BlockingQueue<ScheduleTask> getOutput() {
		return output;
	}

	public void setOutput(BlockingQueue<ScheduleTask> output) {
		this.output = output;
	}

	@Override
	public void run() {
		TaskInfo taskInfo = null;
		while (!Thread.interrupted()) {
			try {
				taskInfo = input.take();
				TaskClassify.processTaskInfo(taskInfo);
			} catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				Log.error("", e);
			}
		}
	}

	@Override
	public String getServiceName() {
		return "arsf.core.schedule.SchedulerCentre";
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
