package com.neusoft.np.arsf.common.util;

import com.neusoft.np.arsf.common.exception.NMException;

/**
 * 
 * 项目名称: common<br>
 * 模块名称: common<br>
 * 功能描述: 运行时异常封装，用于服务初始化阶段使用。<br>
 * 创建日期: 2012-12-19 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-19       黄守凯        创建
 * </pre>
 */
public class NMOSGiServiceException extends NMException {

	private static final long serialVersionUID = 1L;

	public NMOSGiServiceException() {
		super();
	}

	public NMOSGiServiceException(final String msg) {
		super("[ARSF.Common]服务启动异常，服务出现启动或运行问题：" + msg);
	}

	public NMOSGiServiceException(final String msg, final Throwable cause) {
		super("[ARSF.Common]服务引用异常，服务出现启动或运行问题：" + msg, cause);
	}
}
