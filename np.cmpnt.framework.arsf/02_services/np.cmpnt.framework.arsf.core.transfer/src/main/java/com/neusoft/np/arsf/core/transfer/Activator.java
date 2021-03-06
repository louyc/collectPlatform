package com.neusoft.np.arsf.core.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.ServiceRegistration;

import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.core.transfer.event.TransferInitConfHandler;
import com.neusoft.np.arsf.core.transfer.service.ChannelPool;
import com.neusoft.np.arsf.core.transfer.service.NPTransferServiceImpl;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.transfer.NPTransferService;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: 发送服务启动类<br>
* 创建日期: 2012-6-28 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-28下午12:29:41      马仲佳       创建
* </pre>
 */
public class Activator extends BaseActivator {
	
	private ServiceRegistration<?> service;

	/**
	 * 初始化
	 */
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		registerServices();
		
	}
	
	private void registerServices() {
		String serviceName = NPTransferService.class.getName();
		service = getContext().registerService(serviceName, new NPTransferServiceImpl(), null);
	}

	/**
	 * 关闭资源
	 */
	public void stop() {
		ChannelPool.getInstance().close();
		if (service != null)
			service.unregister();
		unbindService(NMSSubject.class);
		unbindCoreServices();
	}

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new TransferInitConfHandler());
		return list;
	}
}
