package com.neusoft.np.arsf.base.bundle.event;

import java.util.List;

import com.neusoft.np.arsf.base.bundle.BaseContextImpl;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

/**
 * 
 * 项目名称: IT监管处理平台<br>
 * 模块名称: 处理平台服务开发<br>
 * 功能描述: 事件处理类，将接收到的事件解析和处理<br>
 * 创建日期: 2012-12-17 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br>
 * 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * 
 *          <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-17       黄守凯        创建
 * </pre>
 */
public class NPEventListener extends NMService {

	public NPEventListener() {
	}

	public NPEventListener(String name) {
		this.serviceName = name;
	}

	@Override
	public void run() {
		BaseContextImpl pool = BaseContextImpl.getInstance();
		if (pool.isEventHandlerEmpty()) {
			Log.warn("线程:" + serviceName + "未发现需要监听的主题，线程终止。");
			return;
		}
		NPEventInfo eventInfo = null;
		try {
			while (isThreadRunning()) {
				eventInfo = pool.getEventQueue().take();
				List<BaseEventHandler> handlers = pool.getEventHandler(eventInfo.getTopic());
				for (BaseEventHandler handler : handlers) {
					handler.processEvent(eventInfo.getDataInfo());
				}
			}
		} catch (InterruptedException e) {
			Log.warn("线程:" + serviceName + "出现中断异常");
		}
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
