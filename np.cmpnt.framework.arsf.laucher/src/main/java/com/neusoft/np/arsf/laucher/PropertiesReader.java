package com.neusoft.np.arsf.laucher;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
* 项目名称: 采集平台框架<br>
* 模块名称: 启动bundle<br>
* 功能描述: 文件读取工具类<br>
* 创建日期: 2012-3-28 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-3-28       黄守凯        创建
* </pre>
 */
public class PropertiesReader {

	private static final String CONFIG_PATH = "startconfig/launcher.properties";

	/**
	 * 以map形式获取启动优先级
	 * 
	 * @return
	 */
	protected static Map<String, Integer> getStartLevel() {
		Properties properties = getProperties();
		Iterator<Entry<Object, Object>> iter = properties.entrySet().iterator();
		Map<String, Integer> startLevelMap = new HashMap<String, Integer>();
		while (iter.hasNext()) {
			Entry<Object, Object> entry = iter.next();
			String key = String.valueOf(entry.getKey());
			String value = String.valueOf(entry.getValue());
			Integer level = Integer.valueOf(value);
			startLevelMap.put(key, level);
		}
		return startLevelMap;
	}

	/**
	 * 读取Log配置文件
	 * 
	 * @return
	 */
	private static Properties getProperties() {
		Properties property = new Properties();
		try {
			InputStream inputStream = getResourceInputStreamByName(CONFIG_PATH);
			if (inputStream != null) {
				property.load(inputStream);
			}
		} catch (IOException e) {
			System.out.println("launcher.properties IO exception");
			e.printStackTrace();
		}
		return property;
	}

	/**
	 * 获取指定资源名的输入流
	 * 
	 * @param filePath 文件名
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static InputStream getResourceInputStreamByName(String name) throws IOException {
		InputStream inputStream = null;
		String currentJarPath = URLDecoder.decode(PropertiesReader.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile(), "UTF-8"); // 获取当前Jar文件名
		String path = PropertiesReader.class.getResource("").getPath();
		if (path.indexOf("!") != -1) {// 在jar中
			JarFile currentJar = new JarFile(currentJarPath);
			JarEntry dbEntry = currentJar.getJarEntry(name);
			if (dbEntry != null)
				inputStream = currentJar.getInputStream(dbEntry);
		}
		if (inputStream == null) {
			String loaderPath = currentJarPath;
			String packPath = PropertiesReader.class.getPackage().toString();
			packPath = packPath.replace("package ", "");
			packPath = "/" + packPath.replace(".", "/");
			if (!loaderPath.endsWith("/"))
				loaderPath = loaderPath.substring(0, loaderPath.lastIndexOf("/")) + "/";
			loaderPath = loaderPath.replace(packPath, "");
			String tPath = loaderPath + name;
			inputStream = new FileInputStream(tPath);
		}
		return inputStream;
	}
}
