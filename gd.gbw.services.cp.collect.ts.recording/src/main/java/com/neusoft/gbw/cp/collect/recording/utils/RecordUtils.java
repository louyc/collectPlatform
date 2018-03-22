package com.neusoft.gbw.cp.collect.recording.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.collect.recording.constant.ConfigVariable;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordUtils {
	
	public static void writeStatus() {
		String path = ConfigVariable.RECORD_STATUS_FILE_PATH;
		String time = getCurrentTime();
		String threadId = getCurrentThreadId();
		String str = time + "#" + threadId;
		Log.debug("录音平台 启动过的所有进程号(记录到文件中的)：："+threadId);
		clearFile(path);
		writerFile(path, str);
	}
	
	public static void refreshHeartbit() {
		String path = ConfigVariable.RECORD_STATUS_FILE_PATH.replace(".txt", ".status");
		String time = String.valueOf(System.currentTimeMillis());
		Log.debug("录音平台 更新心跳状态文件(" + path + ")：" + time);
		clearFile(path);
		writerFile(path, time);
	}
	
	public static Map<String, String> readAllFilePath(String filePath) throws NullPointerException{
		Map<String, String> fileMap = new HashMap<String, String>();
		String name = null;
		String path = null;
		File root = new File(filePath);
		File[] files = root.listFiles();
		for(int i=0;i<files.length;i++) {
			name = files[i].getName();
			path = filePath + name;
			fileMap.put(name, path);
		}
		return fileMap;
	}
	
	public static String readFile(String filePath) {
		String line = null;
		StringBuffer buffer = new StringBuffer();
		File file = null;
		InputStream input = null;
		BufferedReader reader = null;
		try {
			file = new File(filePath);
			if(!file.exists()){
				Log.debug("读取文件不存在");
				return null;
			}
			input = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(input, "GB2312"));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			Log.debug("读取文件失败IOException",e);
			return null;
		} catch (Exception e) {
			Log.debug("读取文件失败Exception",e);
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
        out.flush();
        
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
	        fw.flush();
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
	
	private static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	private  static String getCurrentThreadId() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		return name.substring(0, name.indexOf("@"));
	}
	
	public static void main(String[] arg0) {
//		String filePath = "D:\\SmP05_780381683_20150615210744_20150615210846_549_R2.zip";
//		String msg = "manual_3=1\r\n";
//		System.out.println(clearFile(filePath));
//		System.out.println(writerFile(filePath,msg));
		String ddd="C:\\\\gbwProduct\\\\gbw\\\\MonitorStatus\\\\RmD09.txt";
		String path = ddd.replace(".txt", ".status");
		System.out.println(path);
		
	}

}
