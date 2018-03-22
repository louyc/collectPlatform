package com.neusoft.np.arsf.core.schedule.quartz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 任务管理控制变量集合<br>
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
public class TaskConfiguration {

	private static class TaskConfigurationHolder {
		private static final TaskConfiguration INSTANCE = new TaskConfiguration();
	}

	private TaskConfiguration() {
		initConfiguration();
	}

	private boolean stopPeriod = false;

	/**
	 * 单例模式
	 */
	protected static TaskConfiguration getInstance() {
		return TaskConfigurationHolder.INSTANCE;
	}

	/**
	 * 周期调度方式的，任务信息存放集合
	 */
	private ConcurrentMap<String, TaskInfo> periodMap;

	/**
	 * 计划调度方式的，任务信息存放集合
	 */
	private ConcurrentMap<String, TaskInfo> planMap;
	
	private Lock periodMapLocker = new ReentrantLock();

	/**
	 * 初始化队列，需要在发送线程初始化前进行初始化
	 */
	private void initConfiguration() {
		setPeriodMap(new ConcurrentHashMap<String, TaskInfo>());
		setPlanMap(new ConcurrentHashMap<String, TaskInfo>());
	}

	protected ConcurrentMap<String, TaskInfo> getPlanMap() {
		return planMap;
	}

	protected void setPlanMap(ConcurrentMap<String, TaskInfo> planMap) {
		this.planMap = planMap;
	}

	protected ConcurrentMap<String, TaskInfo> getPeriodMap() {
		return periodMap;
	}

	protected void setPeriodMap(ConcurrentMap<String, TaskInfo> periodMap) {
		this.periodMap = periodMap;
	}

	protected boolean isStopPeriod() {
		return stopPeriod;
	}

	protected void setStopPeriod(boolean stopPeriod) {
		this.stopPeriod = stopPeriod;
	}
	
	public Lock getPeriodMapLocker() {
		return periodMapLocker;
	}

	public void setPeriodMapLocker(Lock periodMapLocker) {
		this.periodMapLocker = periodMapLocker;
	}

}
