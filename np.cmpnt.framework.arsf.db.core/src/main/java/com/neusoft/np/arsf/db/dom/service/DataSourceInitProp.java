package com.neusoft.np.arsf.db.dom.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.neusoft.np.arsf.common.util.NPPropConfig;
import com.neusoft.np.arsf.common.util.NPPropConfigException;

public class DataSourceInitProp {

	static final String DB_CONFIG_PATH = "cfg/db-config.properties";

	private Map<String, String> mapper;

	private Map<String, Map<String, Map<String, String>>> pmap = new HashMap<String, Map<String, Map<String, String>>>();

	public Map<String, Map<String, Map<String, String>>> initProperties() throws DataSourceInitException {
		try {
			mapper = NPPropConfig.getAllProp(DB_CONFIG_PATH, DataSourceInitProp.class);
			processProperties(mapper);
			return pmap;
		} catch (NPPropConfigException e) {
			throw new DataSourceInitException("配置文件读取失败.", e);
		} catch (Exception e) {
			throw new DataSourceInitException("配置文件读取失败.", e);
		}
	}

	private void processProperties(Map<String, String> mapper) throws NPPropConfigException {
		Iterator<Entry<String, String>> mapperIter = mapper.entrySet().iterator();
		while (mapperIter.hasNext()) {
			Entry<String, String> entry = mapperIter.next();
			String k = entry.getKey();
			String[] kitems = StringUtils.split(k, ".");
			if (kitems.length < 3) {
				throw new NPPropConfigException("配置参数错误，不能解析。" + entry);
			}
			if (!pmap.containsKey(kitems[0])) {
				pmap.put(kitems[0], new HashMap<String, Map<String, String>>());
			}
			Map<String, Map<String, String>> typeMap = pmap.get(kitems[0]);
			if (!typeMap.containsKey(kitems[1])) {
				typeMap.put(kitems[1], new HashMap<String, String>());
			}
			Map<String, String> dbMap = typeMap.get(kitems[1]);
			int start = kitems[0].length() + kitems[1].length() + 2;
			if (k.length() < start) {
				throw new NPPropConfigException("配置参数错误，不能解析。" + entry);
			}
			String dbValue = k.substring(start);
			dbMap.put(dbValue, entry.getValue());
		}
	}
}
