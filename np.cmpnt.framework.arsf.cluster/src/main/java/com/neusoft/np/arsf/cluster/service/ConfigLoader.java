package com.neusoft.np.arsf.cluster.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.cluster.constant.ConfigVariable;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NPPropConfig;

public class ConfigLoader {
	
	private static final String BUS_CONFIG_FILE_NAME = "startconfig/bus_laucher.properties";
	
	public void loadConfig() {
		//获取配置文件，配置文件所在位置com.neusoft.np.arsf.core.config工程下的config包配置文件，自己新建配置文件，然后再fileListConfig文件中将配置文件写入
		Log.info("初始化配置文件信息开始");
		Map<String, String> configMap = ARSFToolkit.getConfiguration().getAllBusinessProperty("arsf_cluster");
		try {
			NMBeanUtils.fillClassStaticFields(ConfigVariable.class, configMap);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		Log.info("初始化配置文件信息完成");
	}
	
	public Map<String, Integer> loadBussinessConfig() {
		InputStream in = null;
		List<String> list = null;
		Map<String, Integer> levelMap = null;
		try {
			in = getResourceInputStreamByName(BUS_CONFIG_FILE_NAME);
			// XXX 修改为自动扫描文件夹路径
			list = getProperties(in);
		} catch (IOException e) {
			Log.error("加载" + BUS_CONFIG_FILE_NAME + "失败。", e);
			return levelMap;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		
		levelMap = new HashMap<String, Integer>();
		for(String line : list) {
			String[] array = line.trim().split("=");
			levelMap.put(array[0], Integer.valueOf(array[1]));
		}
		
		return levelMap;
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
	
	private InputStream getResourceInputStreamByName(String name) throws IOException {
		return NPPropConfig.getResourceInputStreamByName(ConfigLoader.class, name);
	}
}
