package com.neusoft.np.arsf.core.schedule;

import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.BaseServicePool;
import com.neusoft.np.arsf.core.schedule.app.NPScheduleServiceImpl;
import com.neusoft.np.arsf.core.schedule.app.ScheduleControl;
import com.neusoft.np.arsf.core.schedule.infra.constants.ScheduleDeclare;
import com.neusoft.np.arsf.core.schedule.intf.event.EventHandlerRegister;
import com.neusoft.np.arsf.service.schedule.NPScheduleService;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 服务启动类<br>
 * 创建日期: 2013年11月8日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年11月8日       黄守凯        创建
 * </pre>
 */
public class Activator extends BaseActivator {

	private static BundleContext bundleContext;
	private ServiceRegistration<?> service = null;

	@Override
	public void init() {
		bindCoreServices();
		bundleContext = getContext();
		BaseServicePool.getInstance().setBundleContext(getContext());
		ScheduleControl control = ScheduleControl.getInstance();
		control.init();
		control.setBaseContext(getBaseContext());
		control.start();
		sleep();
		registerServices();
		// ScheduleTest.test();
	}
	
	private void sleep() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void registerServices() {
		String serviceName = NPScheduleService.class.getName();
		service = getContext().registerService(serviceName, new NPScheduleServiceImpl(), null);
	}

	@Override
	public void stop() {
		if (service != null)
			service.unregister();
		unbindCoreServices();
	}

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		return EventHandlerRegister.getEventHandler();
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public String getBundleName() {
		return ScheduleDeclare.BUNDLE_NAME;
	}
}
