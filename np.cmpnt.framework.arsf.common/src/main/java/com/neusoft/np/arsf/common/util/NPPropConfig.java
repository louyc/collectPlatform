package com.neusoft.np.arsf.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

public class NPPropConfig {

	public static Map<String, String> getAllProp(String fileName, Class<?> clazz) throws NPPropConfigException {
		Map<String, String> prop = null;
		try {
			InputStream in = getResourceInputStreamByName(clazz, fileName);
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load(in);
			prop = new LinkedHashMap<String, String>(); // 有序map
			Iterator<String> iter = config.getKeys();
			while (iter.hasNext()) {
				String key = iter.next();
				prop.put(key, config.getString(key));
			}
		} catch (ConfigurationException e) {
			throw new NPPropConfigException(e);
		} catch (IOException e) {
			throw new NPPropConfigException(e);
		}
		return prop;
	}

	public static String getValue(String fileName, Class<?> clazz, String keyName) throws NPPropConfigException {
		try {
			InputStream in = getResourceInputStreamByName(clazz, fileName);
			PropertiesConfiguration config = new PropertiesConfiguration();
			config.load(in);
			return config.getString(keyName);
		} catch (ConfigurationException e) {
			throw new NPPropConfigException(e);
		} catch (IOException e) {
			throw new NPPropConfigException(e);
		}
	}

	@SuppressWarnings("resource")
	public static InputStream getResourceInputStreamByName(Class<?> clazz, String name) throws IOException {
		String currentJarPath = URLDecoder.decode(clazz.getProtectionDomain().getCodeSource().getLocation().getFile(),
				"UTF-8"); // 获取当前Jar文件名
		String loaderPath = getFilePathFromJarPath(clazz, currentJarPath);
		// Jar包外部同级目录
		InputStream inputStream = getInputStream(loaderPath, name);
		if (inputStream != null) {
			return inputStream;
		}
		// Jar包内部路径
		String path = clazz.getResource("").getPath();
		if (path.indexOf("!") != -1) {// 在jar中
			JarFile currentJar = new JarFile(currentJarPath);
			JarEntry dbEntry = currentJar.getJarEntry(name);
			if (dbEntry != null) {
				// System.out.println("配置文件路径：" + currentJar);
				return currentJar.getInputStream(dbEntry);
			}
		}
		// Jar包外部父级目录
		return getFurFatherPath(loaderPath, name);
	}

	private static InputStream getInputStream(String loaderPath, String name) throws FileNotFoundException {
		String tPath = loaderPath + name;
		if (fileExist(tPath)) {
			// System.out.println("配置文件路径：" + tPath);
			return new FileInputStream(tPath);
		}
		return null;
	}

	private static String getFilePathFromJarPath(Class<?> clazz, String currentJarPath) {
		String loaderPath = currentJarPath;
		String packPath = clazz.getPackage().toString();
		packPath = packPath.replace("package ", "");
		packPath = "/" + packPath.replace(".", "/");
		if (!loaderPath.endsWith("/"))
			loaderPath = loaderPath.substring(0, loaderPath.lastIndexOf("/")) + "/";
		loaderPath = loaderPath.replace(packPath, "");
		return loaderPath;
	}

	private static InputStream getFurFatherPath(String loaderPath, String name) throws FileNotFoundException {
		String fpath = loaderPath;
		while (true) {
			fpath = getFatherPath(fpath);
			// System.out.println("配置文件路径更改：" + fpath);
			if (fpath == null) {
				break;
			}
			InputStream is = getInputStream(fpath, name);
			if (is != null) {
				return is;
			}
		}
		return null;
	}

	private static String getFatherPath(String loaderPath) {
		int index = StringUtils.lastIndexOf(loaderPath.substring(0, loaderPath.length() - 1), "/");
		if (index < 0) {
			return null;
		}
		return loaderPath.substring(0, index) + "/";
	}

	private static boolean fileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

}
