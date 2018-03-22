package com.neusoft.np.arsf.core.config;

/**
* 项目名称: 采集平台框架<br>
* 模块名称: 配置文件服务Bundle<br>
* 功能描述: 服务调用异常<br>
* 创建日期: 2012-6-11 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-11       马仲佳       创建
* </pre>
 */
public class NMServerException extends Exception {

	private static final long serialVersionUID = 3019967877566833434L;

	public NMServerException() {
		super();
	}

	public NMServerException(String msg) {
		super("[manage.dispose]服务引用异常：" + msg);
	}

	public NMServerException(String msg, Throwable cause) {
		super("[manage.dispose]服务引用异常：" + msg, cause);
	}
}