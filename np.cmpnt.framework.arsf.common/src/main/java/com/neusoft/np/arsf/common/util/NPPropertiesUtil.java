package com.neusoft.np.arsf.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class NPPropertiesUtil {

	public static Properties readPropertiesFile(String filename) throws NMFormateException {
		Properties properties = new Properties();
		try {
			InputStream inputStream = new FileInputStream(filename);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new NMFormateException("文件路径处理错误：" + filename);
		}
		return properties;
	}

	public static Properties readPropertiesFileFromXML(String filename) throws NMFormateException {
		Properties properties = new Properties();
		try {
			InputStream inputStream = new FileInputStream(filename);
			properties.loadFromXML(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new NMFormateException();
		}
		return properties;
	}

	public static void writePropertiesFile(String filename, Properties properties) throws NMFormateException {
		try {
			OutputStream outputStream = new FileOutputStream(filename);
			properties.store(outputStream, "");
			outputStream.close();
		} catch (IOException e) {
			throw new NMFormateException();
		}
	}

	public static void writePropertiesFileToXML(String filename, Properties properties) throws NMFormateException {
		try {
			OutputStream outputStream = new FileOutputStream(filename);
			properties.storeToXML(outputStream, "");
			outputStream.close();
		} catch (IOException e) {
			throw new NMFormateException();
		}
	}
}
