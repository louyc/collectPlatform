package com.neusoft.np.arsf.service.event;

import java.util.Map;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 核心接口服务-发布订阅事件类<br>
* 功能描述: 发布订阅服务接口，通常由发布订阅服务自身实现<br>
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
public interface NMSSubject {

	/**
	 * 向指定主题注册一个指定的观察者
	 * 
	 * @param topic 主题
	 * @param observer 观察者对象
	 */
	void registerNMSObserver(String topic, NMSObserver observer);

	/**
	 * 向指定主题取消注册一个指定的观察者
	 * 
	 * @param topic 主题
	 * @param observer 观察者对象
	 */
	void unregisterNMSObserver(String topic, NMSObserver observer);

	/**
	 * 向指定主题的所有已注册观察者发送数据
	 *
	 * @param topic 主题
	 * @param properties 数据集合
	 * @param isSync 是否同步发送数据。true:同步，false:异步。同步异步表现在数据发送端是否等待所有观察者收到数据后返回
	 */
	void notifyNMSObservers(String topic, Map<String, ?> properties, boolean isSync);

}
