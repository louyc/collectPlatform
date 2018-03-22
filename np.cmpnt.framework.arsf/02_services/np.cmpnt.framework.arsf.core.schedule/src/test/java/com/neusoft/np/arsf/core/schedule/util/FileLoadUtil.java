package com.neusoft.np.arsf.core.schedule.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoadUtil {

	public static String getDataFromXml(String fileName) throws FileLoadException {
		BufferedReader bur = null;
		StringBuffer buffer = new StringBuffer();
		try {
			bur = new BufferedReader(new InputStreamReader(getProjectFile(fileName), "UTF-8"));
			String line = null;
			while ((line = bur.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			throw new FileLoadException(e);
		} finally {
			try {
				if (bur != null) {
					bur.close();
				}
			} catch (IOException ex) {
				throw new FileLoadException(ex);
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取项目的文件的输入流，如果项目在jar中，则先在jar中查找文件，找不到再到jar外面查找文件
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private static InputStream getProjectFile(String name) throws IOException {
		return FileLoadUtil.class.getResourceAsStream(name);
	}

	public static void main(String[] args) throws FileLoadException {
		String xml = FileLoadUtil.getDataFromXml("plan_2.xml");
		System.out.println(xml);
	}
}
