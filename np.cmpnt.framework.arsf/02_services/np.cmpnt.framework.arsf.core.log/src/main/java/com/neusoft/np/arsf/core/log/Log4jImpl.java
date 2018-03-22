package com.neusoft.np.arsf.core.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.osgi.service.component.ComponentContext;

import com.neusoft.np.arsf.common.util.NPPropConfig;
import com.neusoft.np.arsf.common.util.NPPropConfigException;
import com.neusoft.np.arsf.service.log.Log;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 采集平台日志服务Bundle<br>
* 功能描述: 日志log4j实现类<br>
* 创建日期: 2012-6-10 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1   2012-6-10       马仲佳        创建
* </pre>
 */
public class Log4jImpl implements Log {

	/**
	 * log类别标识
	 */
	private static final String UCPF = "UCPF";

	/**
	 * log4j配置文件地址
	 */
	// private static final String LOG4J_CONFIG_PATH = "log4j.properties";
	private static final String LOG4J_CONFIG_PATH = "config/log4j.properties";

	private static final String LOG4J_MAPPER_PATH = "config/log-mapper.properties";

	/**
	 * log内部对象
	 */
	private Category log;

	private Properties properties;

	private ConcurrentMap<String, Category> catagoryMap = new ConcurrentHashMap<String, Category>();

	@Override
	public void debug(Object message) {
		if (null != log) {
			log.debug(message);
		}
	}

	@Override
	public void info(Object message) {
		if (null != log) {
			log.info(message);
		}
	}

	@Override
	public void warn(Object message) {
		if (null != log) {
			log.warn(message);
		}
	}

	@Override
	public void error(Object message, Throwable t) {
		if (null != log) {
			if (null == t) {
				log.error(message);
			} else {
				log.error(message, t);
			}
		}
	}

	// -------------------- domain -------------------- 

	@Override
	public void debug(String domain, Object message) {
		Category domainLog = getLog(domain);
		if (domainLog.isDebugEnabled()) {
			domainLog.debug(message);
		}
	}

	@Override
	public void info(String domain, Object message) {
		Category domainLog = getLog(domain);
		if (domainLog.isInfoEnabled()) {
			domainLog.info(message);
		}
	}

	@Override
	public void warn(String domain, Object message) {
		getLog(domain).warn(message);
	}

	@Override
	public void error(String domain, Object message, Throwable t) {
		getLog(domain).error(message, t);
	}

	private Category getLog(String domain) {
		if (catagoryMap == null) {
			return log;
		}
		if (catagoryMap.containsKey(domain)) {
			return catagoryMap.get(domain);
		} else {
			synchronized (log) {
				try {
					Category customized = getCategory(domain);
					if (!catagoryMap.containsKey(domain)) {
						catagoryMap.put(domain, customized);
					}
					return customized;
				} catch (IOException e) {
					log.error("定制化日志失败：" + domain, e);
					return log;
				}
			}
		}
	}

	public Category getCategory(String domainName) throws IOException {
		Logger logger = Logger.getLogger(domainName);
		logger.setLevel(log.getLevel());
		logger.setAdditivity(log.getAdditivity());
		@SuppressWarnings("unchecked")
		Enumeration<Appender> e = log.getAllAppenders();
		while (e.hasMoreElements()) {
			Appender eApp = (Appender) e.nextElement();
			if (eApp.getClass().isAssignableFrom(RollingFileAppender.class)) {
				RollingFileAppender eFileApp = (RollingFileAppender) eApp;
				RollingFileAppender logFileApp = new RollingFileAppender();
				logFileApp.setFile("log/" + domainName + ".log", eFileApp.getAppend(), eFileApp.getBufferedIO(),
						eFileApp.getBufferSize());
				logFileApp.setLayout(eFileApp.getLayout());
				logFileApp.setMaxFileSize(String.valueOf(eFileApp.getMaximumFileSize()));
				logFileApp.setMaxBackupIndex(eFileApp.getMaxBackupIndex());
				logger.addAppender(logFileApp);
			} else {
				logger.addAppender(eApp);
			}
		}
		return logger;
	}

	public void activate(ComponentContext context) throws IOException, NPPropConfigException {
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

	public void deactivate(ComponentContext context) {
	}

	/**
	 * 读取Log配置文件
	 * 
	 * @return
	 * @throws IOException 
	 */
	private static Properties getProperties() throws IOException {
		Properties property = new Properties();
		// InputStream inputStream = getProjectFile(LOG4J_CONFIG_PATH);
		InputStream inputStream = NPPropConfig.getResourceInputStreamByName(Log4jImpl.class, LOG4J_CONFIG_PATH);
		if (inputStream != null) {
			property.load(inputStream);
		}
		return property;
	}

	/**
	 * 获取项目的文件的输入流，如果项目在jar中，则先在jar中查找文件，找不到再到jar外面查找文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	//	private static InputStream getProjectFile(String name) throws IOException {
	//		return Log4jImpl.class.getResourceAsStream(name);
	//	}
}
