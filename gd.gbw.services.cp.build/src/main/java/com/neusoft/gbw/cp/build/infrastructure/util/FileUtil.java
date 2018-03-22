package com.neusoft.gbw.cp.build.infrastructure.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

	public static String readFile(String filePath) {
		String line = null;
		StringBuffer buffer = new StringBuffer();
		File file = null;
		InputStream input = null;
		BufferedReader reader = null;
		try {
			file = new File(filePath);
			if(!file.exists()) {
				file.createNewFile();
				return null;
			}
			input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(input, "GB2312"));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}finally {
			if (null != reader && null != input) {
				try {
					reader.close();
					input.close();
				} catch (IOException e) {
				}
			}
		}
		String str = buffer.toString();
		return str;
	}
	
	public static boolean writerFile(String filePath, String str){
		FileOutputStream out = null;
		File file = null;
		try {
        file = new File(filePath);
        if(!file.exists())
			file.createNewFile();
    	out = new FileOutputStream(file,true);        
        out.write(str.getBytes("GB2312"));
        
		} catch (IOException e) {
			return false;
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
    }
	
	public static boolean clearFile(String filePath){
		File file = null;
		FileWriter fw = null;
		try {
	        file = new File(filePath);
	        if(!file.exists())
				file.createNewFile();
	        fw = new FileWriter(file);
	        fw.write("");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
       
    }
	
	public static boolean isFileStatus(String path) {
		boolean flag = false;
		String status = FileUtil.readFile(path);
		clearFile(path);
		if(status == null || status.length() <= 0)
			flag = false;
		if(status.trim().contains("true"))
			flag = true;
		return flag;
	}
	
//	private static 
	
	public static void main(String[] arg0) {
		String filePath = "D:\\unit_status_file.txt";
//		String msg = "manual_3=1\r\n";
//		System.out.println(clearFile(filePath));
//		System.out.println(writerFile(filePath,msg));
//		System.out.println(readFile(filePath));
		System.out.println(isFileStatus(filePath));
		
	}

}
