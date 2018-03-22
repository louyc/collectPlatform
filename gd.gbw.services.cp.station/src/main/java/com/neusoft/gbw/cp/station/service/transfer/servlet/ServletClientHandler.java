package com.neusoft.gbw.cp.station.service.transfer.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.neusoft.gbw.cp.station.constants.ConfigVariable;

public class ServletClientHandler{
	
   public void dispachDate(String report) {
		String httpUrl = null;
		String[] ips = ConfigVariable.REPORT_DEVICE_IPS.split(";");
		for(String ip : ips) {
			httpUrl = "http://" + ip + "/gbw/mon";
			sendReport(report, httpUrl);
		}
	}
   
   
   private void sendReport(String xml,String ServletUrl) {
		   OutputStream out = null;
	        try {
	            URL url = new URL(ServletUrl); //服务器地址
	            URLConnection con = url.openConnection();// 打开地址
	            HttpURLConnection httpurlcon=(HttpURLConnection)con;
	            httpurlcon.setConnectTimeout(5000);
	            httpurlcon.setReadTimeout(5000);
	            httpurlcon.setDoInput(true);// 指示应用程序要从 URL 连接读取数据
	            httpurlcon.setDoOutput(true);// 指示应用程序要将数据写入 URL 连接
	            con.setUseCaches(false); // 取消高速缓存
	            httpurlcon.setRequestProperty("Content-type", "application/octest-stream"); // 设置一般请求属性
	            httpurlcon.connect();
	            out = con.getOutputStream();
	            // 将之前设置的好的对象传入给服务器
	            out.write(xml.getBytes("GB2312"));
	            out.flush();
	            System.out.println("date send is OK");
	            con.getInputStream();
	            
	        } catch (Exception e) {
	        	System.out.println("date send [" + ServletUrl + "] error=" + e);
	        } finally {
	        	try {
	        		if(out != null) {
	        			out.close();
	        		}
				} catch (IOException e) {
				}
	        }
	    }



}
