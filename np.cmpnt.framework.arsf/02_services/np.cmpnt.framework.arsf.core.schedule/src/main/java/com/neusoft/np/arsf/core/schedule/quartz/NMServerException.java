package com.neusoft.np.arsf.core.schedule.quartz;

import com.neusoft.np.arsf.common.exception.NMException;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台任务管理控制Bundle<br>
 * 功能描述: 服务调用异常<br>
 * 创建日期: 2012-3-7 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-3-7       黄守凯        创建
 * </pre>
 */
public class NMServerException extends NMException {

	private static final long serialVersionUID = -8719362407472174583L;

	public NMServerException() {
		super();
	}

	public NMServerException(final String msg) {
		super("[arsf.core.schedule]服务启动异常，框架出现启动或运行问题：" + msg);
	}

	public NMServerException(final String msg, final Throwable cause) {
		super("[arsf.core.schedule]服务引用异常，框架出现启动或运行问题：" + msg, cause);
	}
}
