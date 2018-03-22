package com.neusoft.np.arsf.base.bundle;

import org.osgi.framework.BundleContext;

import com.neusoft.np.arsf.common.util.NMOSGiServicePool;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: 自定义服务上下文Context<br>
 * 创建日期: 2013年8月29日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年8月29日       黄守凯        创建
 * </pre>
 */
public interface BaseContext {

	/**
	 * 获取当前服务名称
	 */
	String getBundleName();

	/**
	 * 
	 */
	String getSymbolicName();

	/**
	 * 
	 */
	String getSymbolicVersion();

	/**
	 * 
	 */
	String getStortName();

	/**
	 * 获取当前服务OSGi上下文
	 */
	BundleContext getContext();

	/**
	 * 获取OSGi服务池
	 */
	NMOSGiServicePool getServicePool();

	/**
	 * 获取线程控制中心
	 */
	NMServiceCentre getServiceCentre();

}
