package com.neusoft.np.arsf.service.event;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 核心接口服务-发布订阅事件类<br>
* 功能描述: 发布订阅通知事件类，通常有发布订阅服务自身实现<br>
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
public interface NMSEvent {

	/**
	 * 是否包含指定属性
	 * 
	 * @param propertyName 属性名
	 * @return
	 */
	boolean containsProperty(String propertyName);

	/**
	 * 获取指定属性名的属性值
	 * 
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	Object getProperty(String propertyName);

	/**
	 * 获取通知事件中属性名数组
	 * 
	 * @return
	 */
	String[] getPropertyNames();

	/**
	 * 获取通知事件来自的主题
	 * 
	 * @return
	 */
	String getTopic();
}
