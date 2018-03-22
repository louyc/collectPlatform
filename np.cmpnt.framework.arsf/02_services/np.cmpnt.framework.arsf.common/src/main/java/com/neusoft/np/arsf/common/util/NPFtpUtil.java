package com.neusoft.np.arsf.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.eclipse.equinox.internal.util.ref.Log;

public class NPFtpUtil {
	
	private FTPClient ftp = null;
	
	public boolean connect(String ip, String username, String password) throws Exception {
		return connect(ip, 22, username, password);
	}
	
	public boolean connect(String ip, int port, String username, String password) throws Exception{
		boolean success = false;
		int reply;
		ftp = new FTPClient();
		ftp.setConnectTimeout(3000);
		ftp.connect(ip);
		ftp.setSoTimeout(3000);
		ftp.setControlEncoding("UTF-8");
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		ftp.login(username, password);
		// 看返回的值是不是230，如果是，表示登陆成功
		reply = ftp.getReplyCode();
		// 以2开头的返回值就会为真
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return success;
		}
		
		return success;
	}
	
	public boolean disconnect() {
		boolean success = true;
		// 退出FTP
		try {
			ftp.logout();
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					success = false;
				}
			}
		} catch (IOException e) {
			success = false;
		}
		return success;
	}
	public void closePut() {
		try {
			if (ftp != null) {
				ftp.disconnect();
				ftp = null;
			}
		} catch (Exception e) {
		}
	}
	
	public boolean uploadFile(String filePath, String fileName, InputStream input) {
		boolean success = false;
		try {
			createAndchangeFilePath(filePath);
			// 将上传文件存储到指定目录
			ftp.storeFile(fileName, input);
			// 关闭输入流
			input.close();
			// 表示上传成功
			success = true;
		} catch (IOException e) {
		} finally {
			disconnect();
		}
		return success;
	}
	
	public boolean downFile(String remotePath, String fileName, String localPath) {
		boolean success = false;
		try {
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftp.listFiles();
			for(FTPFile ff : fs) {
				if (ff.getName().trim().equals(fileName.trim())) {
					File localFile = new File(localPath + "/" + fileName);
					OutputStream out = new FileOutputStream(localFile);
//					ftp.retrieveFile(fileName, out);
					//第二种方式
					InputStream stream = ftp.retrieveFileStream(fileName);
					byte[] b = new byte[10240];
					int num = 0;
					while((num = stream.read(b)) != -1) {
						out.write(b,0,num);
					}
					out.flush();
					out.close();
					stream.close();
					// 表示上传成功
					success = true;
				}
			}
			
		} catch (IOException e) {
			System.out.println("ftp文件下载异常, " + e.getMessage().toString());
		} finally {
			disconnect();
		}
		return success;
	}
	
	private void createAndchangeFilePath(String filePath) throws IOException {
		// linux 场景
		String[] dirArr = filePath.split("/"); 
		// windows 场景
//		String[] dirArr = filePath.split("\\\\"); 
		
		int i = 0;
		for (; i < dirArr.length; i++) {
			if (dirArr[i] != null && !dirArr[i].isEmpty()) {
				String dirStr = createDirStr(dirArr, i);
				if (!ftp.changeWorkingDirectory(dirStr)) {
					ftp.makeDirectory(dirStr);
					ftp.changeWorkingDirectory(dirStr);
				}
			}
		}
	}

	private String createDirStr(String[] dirArr, int length) {
		int i = 0;
		StringBuilder dirStr = new StringBuilder("");
		for (; i < length + 1; i++) {
			// linux 场景
			dirStr.append(dirArr[i]).append("/");
			// windows 场景
//			dirStr.append(dirArr[i]).append("\\");
		}
		return dirStr.toString();
	}

//	public static void main(String[] args) throws Exception {
//		String ip = "10.10.90.150";
//		String username = "user";
//		String password = "user";
//		NPFtpUtil util = new NPFtpUtil();
//		util.connect(ip, username, password);
//		
//		String remotePath = "/upload/";
//		String fileName = "9_StreamHistoryReport_193_SmP37_20150820.xml.zip";
////		String fileName = "TzD04A_1000528822_20120718154936_20120718154946_639_R1.zip";
////		String localPath = "Y:\\";
//		String localPath = "D:\\";
//		System.out.println(util.downFile(remotePath, fileName, localPath));
//	}
}
