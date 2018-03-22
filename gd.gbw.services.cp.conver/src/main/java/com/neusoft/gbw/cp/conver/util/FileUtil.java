package com.neusoft.gbw.cp.conver.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class FileUtil {

	public static String fileToString(String dir) {
		BufferedReader bur = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 数据源
			FileInputStream fis = new FileInputStream(dir);
			InputStreamReader reader = new InputStreamReader(fis, Charset.forName("GB2312"));
			bur = new BufferedReader(reader);
			// 数据汇，按照指定编码格式存储到文本文件
			String line = null;
			while ((line = bur.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (IOException e) {
			e.toString();
		} finally {
			try {
				if (bur != null) {
					bur.close();
				}
			} catch (IOException ex) {
				ex.toString();
			}
		}
		return null;
	}

	public static void stringToFile(String data, String dir) {
		try {
			FileOutputStream fos = new FileOutputStream(dir);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "GB2312");
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(data);
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
