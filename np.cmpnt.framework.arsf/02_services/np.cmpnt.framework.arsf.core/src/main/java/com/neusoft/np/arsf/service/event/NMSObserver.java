package com.neusoft.np.arsf.service.event;

import com.neusoft.np.arsf.service.event.NMSEvent;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 核心接口服务-发布订阅事件类<br>
* 功能描述: 发布订阅中观察者接口，通常由使用发布订阅服务的客户端实现<br>
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
public interface NMSObserver {

	/**
	 * 观察者有感兴趣的通知事件的回调方法
	 * 
	 * @param event 通知事件对象
	 */
	void notifyEvent(NMSEvent event);
}
