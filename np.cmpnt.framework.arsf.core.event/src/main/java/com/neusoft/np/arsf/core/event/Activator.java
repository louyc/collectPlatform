package com.neusoft.np.arsf.core.event;

import java.util.Collection;

import org.osgi.framework.ServiceRegistration;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.log.Log;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 事件发布订阅服务<br>
 * 功能描述: 服务启动类<br>
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
public class Activator extends BaseActivator {
	
	private ServiceRegistration<?> service = null;

	@Override
	public void init() {
		bindService(Log.class);
		//自行注册Event服务，控制启动顺序
		registerServices();
	}
	
	private void registerServices() {
		String serviceName = NMSSubject.class.getName();
		service = getContext().registerService(serviceName, new NMSSubjectImpl(), null);
	}

	@Override
	public void stop() {
		if (service != null)
			service.unregister();
		unbindService(Log.class);
	}

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		return null;
	}
}
