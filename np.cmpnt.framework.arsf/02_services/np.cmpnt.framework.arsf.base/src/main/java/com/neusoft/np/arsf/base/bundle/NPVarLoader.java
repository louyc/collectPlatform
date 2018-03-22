package com.neusoft.np.arsf.base.bundle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class NPVarLoader {

	public static final int LINE_MAX = 20;

	/**
	 * 采集服务参数初始化：
	 * 	1.从配置服务读取数据;
	 * 	2.初始化变量，向AgentVariable类中赋值;
	 * 	3.输出运行变量;
	 * 
	 * [NOTICE] 
	 * 		本类不负载拦截异常，如果参数配置存在问题，会影响bundle启动
	 * 		如果拦截异常，使用默认设置，可以运行，但是会影响系统的正确设置，不应该继续执行。
	 */
	public static void initConfig(String propertiesName, Class<?> clazz) {
		try {
			Map<String, String> dataMap = initConfigurationService(propertiesName);
			initVariable(dataMap, clazz);
			showVariable(clazz);
		} catch (Exception e) {
			Log.error("加载配置文件出现异常", e);
		}
	}

	/**
	 * 初始化部分
	 */
	private static Map<String, String> initConfigurationService(String propertiesName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		Map<String, String> addressing = ARSFToolkit.getConfiguration().getAllBusinessProperty(propertiesName);
		if (addressing != null) {
			dataMap.putAll(addressing);
		}
		return dataMap;
	}

	/**
	 * 从addressingServiceMap中查找变量，如果未找到使用原有变量运行。
	 * 
	 * @throws NMBeanUtilsException 
	 */
	private static void initVariable(Map<String, String> dataMap, Class<?> clazz) throws NMBeanUtilsException {
		NMBeanUtils.fillClassStaticFields(clazz, dataMap);
	}

	/**
	 * 服务参数输出
	 * 
	 * @throws NMBeanUtilsException 
	 */
	private static void showVariable(Class<?> clazz) throws NMBeanUtilsException {
		StringBuffer buffer = new StringBuffer();
		Map<String, Object> map = NMBeanUtils.getClassStaticFieldNames(clazz);
		if (map == null) {
			Log.warn("配置参数处理后无参数");
			return;
		}
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			if (entry.toString().length() > LINE_MAX) {
				buffer.append("\n");
			}
			buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append(";");
		}
		Log.info("配置文件：\n" + buffer.toString());
	}
}
