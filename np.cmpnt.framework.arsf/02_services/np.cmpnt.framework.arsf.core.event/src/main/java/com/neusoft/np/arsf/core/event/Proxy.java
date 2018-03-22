package com.neusoft.np.arsf.core.event;

import org.osgi.service.event.EventAdmin;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseServicePool;
import com.neusoft.np.arsf.base.bundle.ServiceHandlerException;
import com.neusoft.np.arsf.service.log.Log;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: OSGi代理<br>
 * 功能描述: 用于获取OSGi提供的外部服务。<br>
 * 创建日期: 2012-6-13 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br>
 * 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * 
 *          <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-6-13       黄守凯        创建
 * </pre>
 */
public final class Proxy {

	private static BaseServicePool servicePool = BaseServicePool.getInstance();

	private Proxy() {
	}

	/**
	 * 获取log服务
	 */
	public static Log getLog() {
		Log log = ARSFToolkit.getLog();
		checkNeedArgument(log);
		return log;
	}

	public static EventAdmin getEventAdmin() {
		EventAdmin eventAdmin = null;
		try {
			eventAdmin = (EventAdmin) servicePool.getService(EventAdmin.class.getName());
		} catch (ServiceHandlerException e) {
			throw new NMServiceRuntimeException("[Proxy]OSGi获取EventAdmin服务出现问题，Bundle不能进行.");
		}
		checkNeedArgument(eventAdmin);
		return eventAdmin;
	}

	/**
	 * 验证对象是否为null
	 * 
	 * @param argument
	 */
	private static <T> void checkNeedArgument(T argument) {
		if (null == argument) {
			throw new NMServiceRuntimeException("[Proxy]获取采集服务失败，得到引用对象为空");
		}
	}
}
