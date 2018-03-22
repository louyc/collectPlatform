package com.neusoft.np.arsf.core.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.neusoft.np.arsf.common.util.NPPropConfig;

public class LogLoader {

	private static class Holder {
		private static final LogLoader INSTANCE = new LogLoader();
	}

	private LogLoader() {
	}

	public static LogLoader getInstance() {
		return Holder.INSTANCE;
	}
	
	/**
	 * log类别标识
	 */
	private final String UCPF = "UCPF";

	/**
	 * log4j配置文件地址
	 */
	private final String LOG4J_CONFIG_PATH = "config/log4j.properties";

	private final String LOG4J_MAPPER_PATH = "config/log-mapper.properties";

	/**
	 * log内部对象
	 */
	private Category log;

	private Properties properties;

	private ConcurrentMap<String, Category> catagoryMap = new ConcurrentHashMap<String, Category>();
	
	protected void init() throws Exception {
		// 初始化默认日志记录方式
		properties = getProperties();
		PropertyConfigurator.configure(properties);
		log = Logger.getLogger(UCPF);

		// 初始化定制日志记录方式
		Map<String, String> mapper = NPPropConfig.getAllProp(LOG4J_MAPPER_PATH, Log4jImpl.class);
		Iterator<Entry<String, String>> mapperIter = mapper.entrySet().iterator();
		while (mapperIter.hasNext()) {
			Entry<String, String> entry = mapperIter.next();
			String domain = entry.getKey();
			String logTitle = entry.getValue();
			if (catagoryMap.containsKey(domain)) {
				continue;
			}
			Category category = Logger.getLogger(logTitle);
			catagoryMap.put(domain, category);
		}
	}
	
	private Properties getProperties() throws IOException {
		Properties property = new Properties();
		// InputStream inputStream = getProjectFile(LOG4J_CONFIG_PATH);
		InputStream inputStream = NPPropConfig.getResourceInputStreamByName(Log4jImpl.class, LOG4J_CONFIG_PATH);
		if (inputStream != null) {
			property.load(inputStream);
		}
		return property;
	}

	protected Category getLog() {
		return log;
	}

	protected ConcurrentMap<String, Category> getCatagoryMap() {
		return catagoryMap;
	}
}
