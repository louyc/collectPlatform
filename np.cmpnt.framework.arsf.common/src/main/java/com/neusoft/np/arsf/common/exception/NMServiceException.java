package com.neusoft.np.arsf.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * 项目名称: IT监管处理平台<br>
 * 模块名称: 处理平台服务开发<br>
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
public class NMServiceException extends RuntimeException {

	private static final long serialVersionUID = 7576522334384598896L;

	public NMServiceException(String message) {
		super(message);
	}

	public NMServiceException(String message, Throwable e) {
		super(message, e);
	}

	public NMServiceException(Exception e) {
		super(e);
	}

	public NMServiceException(Throwable e) {
		super(e);
	}

	/**
	 * 获得堆栈信息串。
	 * 
	 * @return 堆栈信息串
	 */
	public String getStackTraceString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		this.printStackTrace(pw);
		return sw.toString();
	}
}
