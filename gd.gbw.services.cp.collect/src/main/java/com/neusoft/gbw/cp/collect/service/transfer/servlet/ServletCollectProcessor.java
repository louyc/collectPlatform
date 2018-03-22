package com.neusoft.gbw.cp.collect.service.transfer.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.neusoft.gbw.cp.collect.service.transfer.ICollect;
import com.neusoft.gbw.cp.core.collect.CollectAttrInfo;
import com.neusoft.gbw.cp.core.collect.ServletTransferAttr;
import com.neusoft.np.arsf.base.bundle.Log;

public class ServletCollectProcessor implements ICollect {

	@Override
	public boolean checkInfo(CollectAttrInfo attrInfo) {
		ServletTransferAttr servletAttr = (ServletTransferAttr)attrInfo.getTransferAttr();
		if(servletAttr.getUrl() == null) {
			return false;
		}
		return true;
	}

	//	@Override
	//	public boolean collect(CollectAttrInfo attrInfo, String xml) {
	//		  boolean flag = false;
	//		  ServletTransferAttr attr = ((ServletTransferAttr)attrInfo.getTransferAttr());
	//		  String urlStr = attr.getUrl();
	//	        try {
	//	        	URL url  = new URL(urlStr);// 服务器地址
	//	        	URLConnection con = url.openConnection();// 打开地址
	//	            HttpURLConnection httpurlcon=(HttpURLConnection)con;
	//	            httpurlcon.setConnectTimeout(9000);
	//	            httpurlcon.setReadTimeout(9000);
	//	            httpurlcon.setDoInput(true);// 指示应用程序要从 URL 连接读取数据
	//	            httpurlcon.setDoOutput(true);// 指示应用程序要将数据写入 URL 连接
	//	            con.setUseCaches(false); // 取消高速缓存
	//	            httpurlcon.setRequestMethod("POST");
	//	            httpurlcon.setRequestProperty("Content-type", "application/octest-stream"); // 设置一般请求属性
	////	            httpurlcon.setRequestProperty("User-Agent", "Mozilla/4.0(compatible:MSIE:500;Windows NT;DigExt)");
	//	            httpurlcon.connect();
	//	            OutputStream out  = con.getOutputStream();
	//	            // 将之前设置的好的对象传入给服务器
	//	            out.write(xml.getBytes("GB2312"));
	//	            out.flush();
	//	            Log.info("[servlet发送]任务发送完成  协议：" + xml);
	//	            InputStream in = con.getInputStream();
	//	            flag = true;
	//	            out.close();
	//	            in.close();
	//	        } catch (Exception e) {
	//	        	 Log.error("[servlet客户端连接服务端错误] url地址：" + urlStr, e);
	//	        	 flag = false;
	//	        } 
	////	        finally {
	////	        	try {
	////	        		if(out != null) {
	////	        			out.close();
	////	        		}
	////				} catch (IOException e) {
	////				}
	////	        }
	//		return flag;
	//	}

	@Override
	public boolean collect(CollectAttrInfo attrInfo, String xml) {
		boolean flag = false;
		HttpURLConnection httpConnection = null;
		ServletTransferAttr attr = ((ServletTransferAttr)attrInfo.getTransferAttr());
		String urlStr = attr.getUrl();
		try {
			URL url = new URL(urlStr);
			httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setReadTimeout(8000);
			StringBuffer sbxml = new StringBuffer(xml);
			String sb ="";
			if(sbxml.toString().contains("QualityTaskSet") || 
					sbxml.toString().contains("SpectrumTaskSet")||
					sbxml.toString().contains("QualityHistoryQuery")
					||sbxml.toString().contains("StreamHistoryQuery")
					||sbxml.toString().contains("EquipmentInitParamSet")
					||sbxml.toString().contains("StreamTaskSet")
					||sbxml.toString().contains("ProgramCommand")){
				Log.info("[servlet发送]：  修改协议内容   加空格");
				sb=sbxml.toString().replaceAll("/>"," />");
			}else{
				Log.info("[servlet发送]：  不用修改协议");
				sb=sbxml.toString();
			}
			OutputStream os = httpConnection.getOutputStream();
			Log.info("[servlet发送]任务发送完成  协议：" + sb);
			os.write(sb.getBytes("GB2312"));
			os.flush();
			os.close();
			flag = true;

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				Log.info("任务发送成功，返回：" + line);
			}
		} catch (Exception e) {
			Log.error("[servlet客户端连接服务端错误] url地址：" + urlStr, e);
			flag = false;
		} 
		finally {
			if(httpConnection!= null) {
				httpConnection.disconnect();
				httpConnection = null;
			}
		}
		return flag;
	}

}
