package com.neusoft.np.arsf.common.util;

import com.neusoft.np.arsf.common.exception.NMException;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 任务信息初始化服务<br>
 * 功能描述: 格式异常<br>
 * 创建日期: 2012-6-27 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-6-27       刘勃宏        创建 
 * </pre>
 */
public class NMFormateException extends NMException {

	private static final long serialVersionUID = 1199787563754644161L;

	public NMFormateException() {
		super();
	}

	public NMFormateException(Throwable cause) {
		super(cause);
	}

	public NMFormateException(final String msg) {
		super("[ARSF.Common]数据格式处理异常：" + msg);
	}

	public NMFormateException(final String msg, final Throwable cause) {
		super("[ARSF.Common]数据格式处理异常：" + msg, cause);
	}
}
