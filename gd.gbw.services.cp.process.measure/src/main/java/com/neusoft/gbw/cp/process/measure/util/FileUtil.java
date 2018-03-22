package com.neusoft.gbw.cp.process.measure.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {
	
	@SuppressWarnings("resource")
	public String unZipFile(InputStream stream,String localPath) {
		String newFileName = null;
		File file = new File("");
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
		    ZipInputStream zipInputStream = new ZipInputStream(stream); 
	        ZipEntry zipEntry = null; 
	        while ((zipEntry = zipInputStream.getNextEntry()) != null) { 
	        	newFileName = zipEntry.getName(); 
	            File temp = new File(localPath + newFileName); 
	            if (! temp.getParentFile().exists()) 
	                temp.getParentFile().mkdirs(); 
	            OutputStream os = new FileOutputStream(temp); 
	            InputStream is = zipFile.getInputStream(zipEntry); 
	
	            int len = 0; 
	            byte[] bt = new byte[10240];
	            while ((len = is.read(bt)) != -1) 
		            os.write(bt,0,len); 
		            os.close(); 
		            is.close(); 
	        } 
	        zipInputStream.close();
		} catch (ZipException e) {
			return newFileName; 
		} catch (IOException e) {
			return newFileName;
		}
		return newFileName; 
	}
	
	public static String readFile(String filePath) {
		String line = null;
		StringBuffer buffer = new StringBuffer();
		File file = null;
		InputStream input = null;
		BufferedReader reader = null;
		try {
			file = new File(filePath);
			if(!file.exists())
				return null;
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
	
	public static void main(String[] arg0) {
		String filePath = "D:\\SmP05_780381683_20150615210744_20150615210846_549_R2.zip";
//		String msg = "manual_3=1\r\n";
//		System.out.println(clearFile(filePath));
//		System.out.println(writerFile(filePath,msg));
		System.out.println(readFile(filePath));
		
	}

}
