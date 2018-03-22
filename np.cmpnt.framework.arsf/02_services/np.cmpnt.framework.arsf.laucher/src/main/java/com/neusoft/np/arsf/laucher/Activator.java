package com.neusoft.np.arsf.laucher;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 启动bundle<br>
 * 功能描述: 服务启动类<br>
 * 创建日期: 2012-9-6 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-9-6       黄守凯        创建
 * </pre>
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	private ServiceRegistration<?> service = null;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("com.neusoft.nms.cp.framework.laucher Bundle starting");
		Activator.context = bundleContext;
		Map<String, Bundle> bundleMap = StartLevelProcess.changeStartLevel(bundleContext);
		StartLevelProcess.showStartLevel(bundleContext);
		StartLevelProcess.startBundles(bundleMap);
		registerServices();
		System.out.println("com.neusoft.nms.cp.framework.laucher Bundle start finish");
	}
	
	private void registerServices() {
		String serviceName = NPLauncherService.class.getName();
		service = getContext().registerService(serviceName, new NPLauncherServiceImpl(), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("com.neusoft.nms.cp.framework.laucher Bundle stop");
		if (service != null)
			service.unregister();
		Activator.context = null;
	}
}
