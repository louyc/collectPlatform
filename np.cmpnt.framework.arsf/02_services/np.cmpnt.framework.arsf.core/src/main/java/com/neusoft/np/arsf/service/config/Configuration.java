package com.neusoft.np.arsf.service.config;

import java.util.Map;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 框架提供配置服务的接口<br>
* 功能描述: 对外暴露框架提供配置服务的接口<br>
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
public interface Configuration {

	/**
	 * 获取框架配置文件中指定属性值
	 */
	String getFrameProperty(String key);

	/**
	 * 获取指定业务范围配置文件中指定属性值
	 * 
	 * @param businessScope 业务范围
	 * @param key 属性名
	 */
	String getBusinessProperty(String businessScope, String key);

	/**
	 * 获取所有框架属性-值集合
	 */
	Map<String, String> getAllFrameProperty();

	/**
	 * 获取指定业务范围配置文件的所有属性-值集合
	 * 
	 * @param businessScope 业务范围
	 */
	Map<String, String> getAllBusinessProperty(String businessScope);
}
