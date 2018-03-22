package com.neusoft.np.arsf.core.schedule.app;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.neusoft.np.arsf.base.bundle.BaseContext;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.core.schedule.infra.config.ScheduleVariable;
import com.neusoft.np.arsf.core.schedule.quartz.ScheduleTask;
import com.neusoft.np.arsf.core.schedule.quartz.SchedulerCentre;
import com.neusoft.np.arsf.core.schedule.quartz.TaskInfo;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 启动控制<br>
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
public class ScheduleControl {

	private static class ScheduleControlHolder {
		private final static ScheduleControl INSTANCE = new ScheduleControl();
	}

	private ScheduleControl() {
	}

	public static ScheduleControl getInstance() {
		return ScheduleControlHolder.INSTANCE;
	}

	private BaseContext baseContext;

	private SchedulerCentre centre;

	private BlockingQueue<TaskInfo> input;

	private BlockingQueue<ScheduleTask> output;

	public void init() {
		input = new ArrayBlockingQueue<TaskInfo>(ScheduleVariable.queueSize);
		output = new ArrayBlockingQueue<ScheduleTask>(ScheduleVariable.queueSize);
	}

	public void start() {
		centre = new SchedulerCentre(input, output);
		centre.initPeriod();
		baseContext.getServiceCentre().addService(centre);
		Log.info("调度[arsf.core.schedule]服务启动结束");
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

	public BaseContext getBaseContext() {
		return baseContext;
	}

	public void setBaseContext(BaseContext baseContext) {
		this.baseContext = baseContext;
	}

}
