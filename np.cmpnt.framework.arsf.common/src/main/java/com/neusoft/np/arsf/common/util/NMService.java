package com.neusoft.np.arsf.common.util;

/**
 * 
 * 项目名称: common<br>
 * 模块名称: common<br>
 * 功能描述: 线程基类，同NMServiceCentre一起使用<br>
 * 创建日期: 2012-11-30 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-11-30       黄守凯        创建
 * </pre>
 */
public abstract class NMService implements IService{

	protected String serviceName;

	protected boolean isThreadRunning = false;

	public abstract String getServiceName();

	public abstract void setServiceName(String serviceName);

	/**
	 * 当前线程是否处于运行状态。
	 */
	public boolean isThreadRunning() {
		return isThreadRunning;
	}

	/**
	 * 设置当前线程运行状态。
	 * 
	 * @param runThread
	 */
	public void setThreadRunning(boolean runThread) {
		this.isThreadRunning = runThread;
	}

	/**
	 * 停止当前线程。
	 */
	public void stopThreadRunning() {
		this.isThreadRunning = false;
	}
}
