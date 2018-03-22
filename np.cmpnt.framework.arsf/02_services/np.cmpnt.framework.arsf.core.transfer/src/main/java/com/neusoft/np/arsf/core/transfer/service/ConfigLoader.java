package com.neusoft.np.arsf.core.transfer.service;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.core.transfer.contants.Constants;
import com.neusoft.np.arsf.core.transfer.vo.conf.ITransferAttrConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.JMSConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.SocketConfig;
import com.neusoft.np.arsf.service.config.Configuration;

/**
 * 
 * 项目名称: 处理平台<br>
 * 模块名称: 数据接收服务<br>
 * 功能描述: 通过配置服务获取配置文件的内容，并且解析填充内部对象集合<br>
 * 创建日期: 2012-12-11 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: liubohong@neusoft.com">刘勃宏</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-12-11       刘勃宏        创建 
 * </pre>
 */
public class ConfigLoader {

	private Configuration config = null;

	private static class ConfigLoaderHolder {
		private static final ConfigLoader INSTANCE = new ConfigLoader();
	}

	private ConfigLoader() {
		config = Proxy.getConfiguration();
	}

	public static ConfigLoader getInstance() {
		return ConfigLoaderHolder.INSTANCE;
	}

	/**
	 * 加载配置文件，并填充通信配置对象
	 * 
	 * @return
	 */
	public Map<String, ITransferAttrConfig> load() {
		Map<String, ITransferAttrConfig> result = new HashMap<String, ITransferAttrConfig>();
		Map<String, String> configMap = config.getAllBusinessProperty(Constants.BUSINESS_SCOPE);
		if (configMap.size() == 0) {
			return null;
		}

		Map<String, Map<String, String>> configDataMap = parseConfig(configMap);
		Iterator<Entry<String, Map<String, String>>> it = configDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Map<String, String>> entry = it.next();
			Map<String, String> map = entry.getValue();
			String key = entry.getKey();
			ITransferAttrConfig config = null;
			if (Constants.RECEIVE_TYPE_JMS.equals(map.get(Constants.RECEIVE_TYPE))) {
				config = new JMSConfig();
			} else if (Constants.RECEIVE_TYPE_SOCKET.equals(map.get(Constants.RECEIVE_TYPE))) {
				config = new SocketConfig();
			}
			if (config == null) {
				Log.warn(Constants.SERVICE_NAME + "尚不支持发送类型:" + map.get(Constants.RECEIVE_TYPE));
				continue;
			}
			fillPropertys(map, config);
			if (!config.validate()) {
				Log.warn(Constants.SERVICE_NAME + "配置文件存在错误的条目，内容是:" + key + "=" + config.toString());
				continue;
			}
			result.put(key, config);
		}

		return result;
	}

	/**
	 * 解析配置文件中value部分
	 * 
	 * 说明：1.配置文件中key通常无用，在此解析过程中忽略
	 * 	   2.此解析是针对目前结构的配置文件而定制化开发的，如果配置文件结构发生变化，需要修改此处对应地 解析逻辑
	 * @param sendConfig
	 * @return
	 */
	private Map<String, Map<String, String>> parseConfig(Map<String, String> configMap) {
		int count = 0;
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		Iterator<Entry<String, String>> it = configMap.entrySet().iterator();
		while (it.hasNext()) {
			Map<String, String> map = new HashMap<String, String>();
			Entry<String, String> entry = it.next();
			String data = entry.getValue();
			if (data.indexOf(Constants.RECEIVE_TYPE) == -1) {
				//由于配置文件不只配置通讯信息，也配置其他应用的属性，所以遇到其他属性需要跳过
				count++;
				continue;
			}

			String[] groups = data.split(",");
			for (String group : groups) {
				String[] item = group.split(":");
				if (item.length != 2) {
					break;
				}
				map.put(item[0], item[1]);
			}
			if (map.size() != groups.length) {
				Log.warn(Constants.SERVICE_NAME + "配置信息在解析过程中出现问题，存在问题的配置为:" + data);
				continue;
			}
			result.put(entry.getKey(), map);
		}
		if (result.size() + count != configMap.size()) {
			Log.warn(Constants.SERVICE_NAME + "配置信息存在问题，请注意！");
		} else {
			Log.info(Constants.SERVICE_NAME + "配置信息完成解析！");
		}
		return result;
	}

	/**
	 * 填充对象属性
	 * 
	 * @param dataMap
	 * @param object
	 */
	private void fillPropertys(Map<String, String> dataMap, Object object) {
		try {
			// object = ConfigConvert.fillAttribute(object, dataMap);
			NMBeanUtils.createObject(object, dataMap);
		} catch (NMBeanUtilsException e) {
			Log.error(Constants.SERVICE_NAME + "异常： " + e.getMessage(), e);
		}
	}
}
