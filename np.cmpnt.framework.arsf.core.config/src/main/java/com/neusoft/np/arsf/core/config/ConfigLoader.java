package com.neusoft.np.arsf.core.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPPropConfig;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 配置文件服务Bundle<br>
* 功能描述: 配置文件加载类<br>
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
public class ConfigLoader {

	/**
	 * instance
	 */
	private static ConfigLoader instance = new ConfigLoader();

	private ConfigLoader() {
	}

	public static ConfigLoader getInstance() {
		return instance;
	}

	/**
	 * 系统配置文件列表文件名
	 */
	private static final String FILE_LIST_CONFIG_FILE_NAME = "config/fileListConfig.properties";

	/**
	 * 统一配置文件列表文件名
	 */
	private static final String UNIFY_CONFIG_FILE_NAME = "config/appContext.properties";

	/**
	 * 平台配置文件关键字
	 */
	private static final String PLATFORM_CONTEXT = "platformContext";

	/**
	 * 业务配置文件关键字
	 */
	private static final String BUSINESS_CONTEXT = "businessContext";

	/**
	 * 配置文件名列表（业务+框架）
	 */
	private List<String> propertieFileNameList;

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

	/**
	 * 初始化
	 * 
	 * @throws NMServerException
	 */
	public synchronized void init() throws NMServerException {
		InputStream in = null;
		try {
			in = getResourceInputStreamByName(FILE_LIST_CONFIG_FILE_NAME);
			// XXX 修改为自动扫描文件夹路径
			propertieFileNameList = getProperties(in);
		} catch (IOException e) {
			Log.error("加载" + FILE_LIST_CONFIG_FILE_NAME + "失败。", e);
			throw new NMServerException("初始化服务出错。", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		//加载配置文件内容
		load();
	}

	private List<String> getProperties(InputStream in) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader bw = null;
		String line = null;
		try {
			bw = new BufferedReader(new InputStreamReader(in));
			while ((line = bw.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				if (null == line || "".equals(line)) {
					continue;
				}
				list.add(line);
			}
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}

		return list;
	}

	/**
	 * 获取框架配置集合
	 * 
	 * @return
	 */
	public synchronized Map<String, String> getAllFrameInfo() {
		return Collections.unmodifiableMap(this.frameMap);
	}

	/**
	 * 获取业务配置项集合
	 * 
	 * @return
	 */
	public synchronized Map<String, Map<String, String>> getAllBusinessInfo() {

		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		Set<Entry<String, Map<String, String>>> set = this.businessMap.entrySet();

		Iterator<Entry<String, Map<String, String>>> it = set.iterator();

		while (it.hasNext()) {
			Entry<String, Map<String, String>> entry = it.next();
			String key = entry.getKey();
			Map<String, String> value = entry.getValue();
			Map<String, String> copyMap = Collections.unmodifiableMap(value);
			result.put(key, copyMap);
		}
		return Collections.unmodifiableMap(this.businessMap);
	}

	/**
	 * 加载配置文件内容
	 * 
	 * @throws NMServerException 
	 */
	private void load() throws NMServerException {
		//Log.info("开始加载配置内容。");
		final List<String> list = this.propertieFileNameList;

		//框架配置信息Map
		frameMap = new HashMap<String, String>();
		//业务配置信息Map
		businessMap = new HashMap<String, Map<String, String>>();
		for (String fileName : list) {
			Map<String, String> result = getProperties(fileName);
			if (result == null || result.size() == 0)
				continue;
			//框架配置文件
			if (fileName.indexOf(PLATFORM_CONTEXT) != -1) {
				//框架配置文件目前全存放在一起
				frameMap.putAll(result);
			}
			//业务配置文件
			if (fileName.indexOf(BUSINESS_CONTEXT) != -1) {
				int startIndex = fileName.indexOf("_");
				int lastIndex = fileName.indexOf(".");
				String bussieScope = fileName.substring(startIndex + 1, lastIndex);
				Map<String, String> bussieScopeMap = businessMap.get(bussieScope);
				if (bussieScopeMap == null) {
					bussieScopeMap = new HashMap<String, String>();
					businessMap.put(bussieScope, bussieScopeMap);
				}
				bussieScopeMap.putAll(result);
			}
		}
		fillUnifyProperties();
		// Log.info("[框架配置服务]成功加载配置内容。");
	}

	private void fillUnifyProperties() throws NMServerException {
		Map<String, String> properties = getProperties(UNIFY_CONFIG_FILE_NAME);
		Iterator<Entry<String, String>> mapIter = properties.entrySet().iterator();
		while (mapIter.hasNext()) {
			Entry<String, String> entry = mapIter.next();
			String[] keyItems = entry.getKey().split("\\|");
			if (keyItems.length != 2) {
				continue;
			}
			if (!businessMap.containsKey(keyItems[0])) {
				businessMap.put(keyItems[0], new HashMap<String, String>());
			}
			businessMap.get(keyItems[0]).put(keyItems[1], entry.getValue());
		}
	}

	private Map<String, String> getProperties(String fileName) throws NMServerException {
		Map<String, String> result = new HashMap<String, String>();
		BufferedReader bw = null;
		try {
			bw = new BufferedReader(new InputStreamReader(getResourceInputStreamByName(fileName), "UTF-8"));
			String thisLine = null;
			String key = null, value = null;
			while ((thisLine = bw.readLine()) != null) {
				if (thisLine.startsWith("#"))
					continue;
				int index = thisLine.indexOf("=");
				if (index == -1)
					continue;
				key = thisLine.substring(0, index);
				value = thisLine.substring(index + 1);
				result.put(key, value);
			}
		} catch (IOException e) {
			Log.error("加载" + fileName + "失败。", e);
			throw new NMServerException("初始化服务出错。", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
		return result;
	}

	/**
	 * 获取指定资源名的输入流
	 * 
	 * @param filePath 文件名
	 * @return
	 * @throws IOException
	 */
	private InputStream getResourceInputStreamByName(String name) throws IOException {
		return NPPropConfig.getResourceInputStreamByName(ConfigLoader.class, name);
	}
}
