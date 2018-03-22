package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPFtpUtil;

public class FtpDownProcess {

	public String ftpDownDataFile(String url) {
		NPFtpUtil ftpUtil = new NPFtpUtil();
		FileVO vo = getFileVOByUrl(url);
		String value = CollectTaskModel.getInstance().getFtpServerByIp(vo.ip);
		if (value == null) {
			Log.warn("[Ftp音频下载]下载的数据文件，所连接Ftp服务暂时不存在对应的用户名和密码，Server_IP=" + vo.ip);
			return null;
		}
		String[] array = value.split("#SEP#");
		try {
			ftpUtil.connect(vo.ip, array[0], array[1]);
		} catch (Exception e) {
			Log.error("[Ftp音频下载]连接Ftp服务异常，Server_IP=" + vo.ip + ", user=" + array[0] + ", password=" + array[1], e);
			ftpUtil.disconnect();
			return null;
		}
		
		String localPath = getAndCreatLocalPath();
		Log.debug("ftp数据文件下载开始，path = " + vo.remotePath + vo.zipFileName);
		boolean isOver = ftpUtil.downFile(vo.remotePath, vo.zipFileName, localPath);
		if(isOver)
			Log.debug("ftp数据文件下载成功，path = " + vo.remotePath + vo.zipFileName);
		else Log.debug("ftp数据文件下载失败，path = " + vo.remotePath + vo.zipFileName);
		try {
			return readZipFile(localPath + vo.zipFileName);
		} catch (Exception e) {
			return null;
		}
	}
	
	private class FileVO {
		private String ip;
		private String remotePath;
		private String zipFileName;
		private String fileName;
		@Override
		public String toString() {
			return "FileVO [ip=" + ip + ", remotePath=" + remotePath
					+ ", zipFileName=" + zipFileName + ", fileName=" + fileName
					+ "]";
		}
	}
	
	private FileVO getFileVOByUrl(String url) {
		String tmp = url.replaceFirst("ftp://", "");
		String[] array = tmp.split("/");
		FileVO vo = new FileVO();
		vo.ip = array[0].split(":").length >= 2 ? array[0].split(":")[0] : array[0];
		vo.zipFileName = array[array.length - 1];
		vo.remotePath = tmp.replaceAll(array[0], "").replaceAll(array[array.length - 1], "");
		
		return vo;
	}
	
	private String getAndCreatLocalPath() {
		String path = getBaseSaveRecordPath() + getYMdTime() + "\\";
		File file = new File(path);
		if (file.exists()) {
			return path;
		}
		
		file.mkdirs();
		return path;
	}
	
	private static String getBaseSaveRecordPath() {
		String rootPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.RECORDING_STORE_ADDRESS);
		String subPath = CollectTaskModel.getInstance().getRecordAddr(ProcessConstants.COLLECT_SUBPATH);
		return rootPath + subPath + "\\";
	}

	private String getYMdTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date()).toString();
	}
	
	@SuppressWarnings("resource")
	private String readZipFile(String file) throws Exception { 
		StringBuffer xml = new StringBuffer();
        ZipFile zf = new ZipFile(file);  
        InputStream in = new BufferedInputStream(new FileInputStream(file));  
        ZipInputStream zin = new ZipInputStream(in); 
        ZipEntry ze;  
        while ((ze = zin.getNextEntry()) != null) {  
            if (ze.isDirectory()) {
            } else {  
//                System.err.println("file - " + ze.getName() + " : "  
//                        + ze.getSize() + " bytes");
            	String zeName = new String(ze.getName().getBytes("iso-8859-1"),"utf-8");
                System.out.println("获取文件名称："+zeName);
                long size = ze.getSize();  
                if (size > 0) {  
                    BufferedReader br = new BufferedReader(  
                            new InputStreamReader(zf.getInputStream(ze))); 
                    String line;  
                    while ((line = br.readLine()) != null) {  
                    	xml.append(line);
                    }  
                    br.close();  
                }  
            }  
        }  
        zin.closeEntry();  
        return xml.toString();
    }  


}
