package com.neusoft.np.arsf.core.event;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
 
import com.neusoft.np.arsf.service.event.NMSObserver;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 事件发布订阅服务<br>
 * 功能描述: 事件处理器，用于代理包装、转发通知事件<br>
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
public class EventHandlerImpl implements EventHandler {

	/**
	 * NMS事件观察者对象
	 */
	private NMSObserver observer;

	public EventHandlerImpl(NMSObserver observer) {
		this.observer = observer;
	}

	@Override
	public void handleEvent(Event event) {
		observer.notifyEvent(new NMSEventImpl(event));
	}
}
