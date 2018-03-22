package com.neusoft.np.arsf.core.config;

import java.util.Map;

import com.neusoft.np.arsf.service.config.Configuration;

/**
  * 项目名称: 采集平台框架<br>
  * 模块名称: 配置文件服务Bundle<br>
  * 功能描述: 服务实现类<br>
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
public class ConfigurationImpl implements Configuration {
	
	/**
	 * 框架配置集合
	 * 
	 * Map<Key,Value>
	 */
	private Map<String, String> frameMap;

	/**
	 * 业务配置集合
	 * 
	 * Map<业务范围,<Key,Value>>
	 */
	private Map<String, Map<String, String>> businessMap;
	
	public ConfigurationImpl() {
		ConfigLoader configReader = ConfigLoader.getInstance();
		frameMap = configReader.getAllFrameInfo();
		businessMap = configReader.getAllBusinessInfo();
	}

	@Override
	public String getFrameProperty(String key) {
		return frameMap.get(key);
	}

	@Override
	public String getBusinessProperty(String businessScope, String key) {
		Map<String, String> businessScopeMap = businessMap.get(businessScope);
		if (businessScopeMap == null)
			return null;
		return businessScopeMap.get(key);
	}

	@Override
	public Map<String, String> getAllFrameProperty() {
		return frameMap;
	}

	@Override
	public Map<String, String> getAllBusinessProperty(String businessScope) {
		return businessMap.get(businessScope);
	}

//	public void activate(ComponentContext context) throws Exception {
//		ConfigLoader configReader = ConfigLoader.getInstance();
//		configReader.init();
//		frameMap = configReader.getAllFrameInfo();
//		businessMap = configReader.getAllBusinessInfo();
//
//		Iterator<Entry<String, Map<String, String>>> businessIter = businessMap.entrySet().iterator();
//		StringBuffer buffer = new StringBuffer();
//
//		while (businessIter.hasNext()) {
//			Entry<String, Map<String, String>> entry = businessIter.next();
//			buffer.append("-- file : " + entry.getKey() + "\n");
//			Map<String, String> item = entry.getValue();
//			Iterator<Entry<String, String>> itemIter = item.entrySet().iterator();
//			while (itemIter.hasNext()) {
//				Entry<String, String> itemEntry = itemIter.next();
//				buffer.append("-- -- key : " + itemEntry.getKey() + "\n");
//				buffer.append("-- -- -- value : " + itemEntry.getValue() + "\n");
//			}
//		}
//		//		System.out.println(buffer.toString());
//	}
//
//	public void deactivate(ComponentContext context) {
//		// DO NOTHING
//	}

}
