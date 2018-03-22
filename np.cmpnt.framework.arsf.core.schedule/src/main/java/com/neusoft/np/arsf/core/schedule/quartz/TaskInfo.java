package com.neusoft.np.arsf.core.schedule.quartz;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 任务信息对象Bean<br>
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
public class TaskInfo {

	/**
	 * 任务内容
	 */
	private ScheduleTask scheduleTask;

	/**
	 * 任务调度计划
	 */
	private ScheduleInfo plan;

	protected TaskInfo(ScheduleTask scheduleTask, ScheduleInfo plan) {
		this.scheduleTask = scheduleTask;
		this.plan = plan;
	}

	protected ScheduleInfo getPlan() {
		return plan;
	}

	protected void setPlan(ScheduleInfo plan) {
		this.plan = plan;
	}

	protected ScheduleTask getScheduleTask() {
		return scheduleTask;
	}

	protected void setScheduleTask(ScheduleTask ScheduleTask) {
		this.scheduleTask = ScheduleTask;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
