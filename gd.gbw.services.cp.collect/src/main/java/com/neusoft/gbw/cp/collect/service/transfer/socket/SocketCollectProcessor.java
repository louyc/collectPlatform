package com.neusoft.gbw.cp.collect.service.transfer.socket;

import java.io.IOException;
import java.net.UnknownHostException;

import com.neusoft.gbw.cp.collect.service.transfer.ICollect;
import com.neusoft.gbw.cp.core.collect.CollectAttrInfo;
import com.neusoft.gbw.cp.core.collect.ServletTransferAttr;
import com.neusoft.np.arsf.base.bundle.Log;

public class SocketCollectProcessor implements ICollect {

	@Override
	public boolean checkInfo(CollectAttrInfo attrInfo) {
		ServletTransferAttr servletAttr = (ServletTransferAttr)attrInfo.getTransferAttr();
		if(servletAttr.getUrl() == null) {
			return false;
		}
		return true;
	}
	
//	@SuppressWarnings("resource")
//	@Override
//	public boolean collect(CollectAttrInfo attrInfo, String xml) {
//		boolean isSendMsg = true;
//		URL url = null;
//		Socket socket = null;
//		OutputStream out = null;
//		InputStream in = null;
//		String newTask = null;
//		ServletTransferAttr attr = ((ServletTransferAttr)attrInfo.getTransferAttr());
//		try {
//			url = new URL(attr.getUrl());
//			socket = new Socket(url.getHost(),url.getPort());
//			socket.setSoTimeout(8000);
//			newTask = BuildHttpPack(url,xml);
//			socket.setSendBufferSize(xml.length());
//		    out = socket.getOutputStream();
//		    byte[] byOutStream = newTask.getBytes("GB2312");
//		    out.write(byOutStream, 0, byOutStream.length);
//		    Log.debug("[socket发送]数据发送成功，task=" + newTask);
//		    in = socket.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			String line;
//			while ((line = reader.readLine()) != null)
//			{
//				System.out.println("任务发送成功，返回：" + line);
//			}
//		} catch (MalformedURLException e) {
//			Log.error("", e);
//		} catch (SocketException e) {
//			Log.error("", e);
//		} catch (IOException e) {
//			Log.error("", e);
//		}finally {
//			try {
//				if(in != null)
//					in.close();
//				if(out != null)
//					out.close();
//				if(socket != null) {
//					socket.shutdownOutput();
//					socket.shutdownInput();
//				}
//			} catch (IOException e) {
//			}
//		}
//		
//		return isSendMsg;
//	}
	
//	@Override
	public boolean collect(CollectAttrInfo attrInfo, String xml) {
		SocketUtils process = null;
		boolean isSendMsg = true;
		ServletTransferAttr attr = ((ServletTransferAttr)attrInfo.getTransferAttr());
		try {
			StringBuffer sbxml = new StringBuffer(xml);
			String sb ="";
			if(sbxml.toString().contains("QualityTaskSet") || 
					sbxml.toString().contains("SpectrumTaskSet")||
					sbxml.toString().contains("QualityHistoryQuery")
					||sbxml.toString().contains("StreamHistoryQuery")
					||sbxml.toString().contains("EquipmentInitParamSet")
					||sbxml.toString().contains("ProgramCommand")){
				Log.info("[Socket发送]：  修改协议内容   加空格");
				sb=sbxml.toString().replaceAll("/>"," />");
			}else{
				Log.info("[Socket发送]：  不用修改协议");
				sb=sbxml.toString();
			}
			process = new SocketUtils(attr.getUrl());
			process.postURLRequest(sb);
			process.getURLResponse();
			process.close();
		} catch (UnknownHostException e) {
			Log.error("", e);
			isSendMsg = false;
		} catch (IOException e) {
			Log.error("", e);
			isSendMsg = false;
		}
		return isSendMsg;
	}
	
//	@SuppressWarnings("finally")
//	@Override
//	public boolean collect(CollectAttrInfo attrInfo, String xml) {
//		NMSSocketClient socketClient = null;
//		boolean isSendMsg = false;
//		String addressIp = null;
//		String oKmsg = null;
//		int addressPort = 800;
//		ServletTransferAttr socketAttr = (ServletTransferAttr)attrInfo.getTransferAttr();
//		URL m_url = null;
//		try {
//			m_url = new URL(socketAttr.getUrl());
//		} catch (MalformedURLException e1) {
//		}
//		try {	
//		    addressIp = m_url.getHost();
//			addressPort = m_url.getPort();
//			socketClient = NMSSocketFactory.createSocketClient(addressIp, addressPort);
//			socketClient.connect();
//			socketClient.send(BuildHttpPack(addressIp, addressPort, xml, m_url.getPath()).getBytes("GB2312"));
//			Log.debug("[socket发送]采集任务发送成功，IP=" + addressIp + ",potocol=" + BuildHttpPack(addressIp, addressPort, xml, m_url.getPath()));
//			oKmsg = socketClient.receive();
//			isSendMsg = true;
//		} catch (Exception e) {
//			Log.error("发送采集任务失败：连接信息：IP："+ addressIp + "  PORT:" + addressPort, e);
//		}finally {
////			if(oKmsg != null && oKmsg.contains("OK")) {
////				sleep();
////				socketClient.disconnect();
////			}
//			if(oKmsg != null) {
//				socketClient.disconnect();
//			}
////			else {
////				Timer timer = new Timer(true);
////				TimeOutTask task = new TimeOutTask(socketClient);
////				timer.schedule(task, 1000);
////			}
//		return isSendMsg;
//	}
//	}
//	
//	private String BuildHttpPack(URL url, String strContent){
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("POST " + url.getPath() + " HTTP/1.1\r\n");
//		buffer.append("Referer: " +  url.getPort()  + "\r\n");
//		buffer.append("User-Agent: Mozila\r\n");
//		buffer.append("Connection: close\r\n");
//		buffer.append("Host: " + url.getHost() + ":" + url.getPort() + "\r\n");
//	    try{
//	    	buffer.append("Content-Length: " + strContent.getBytes("GB2312").length + "\r\n\r\n");
//			buffer.append(strContent);
//	    }
//	    catch (UnsupportedEncodingException ex){
//	    	Log.error("", ex);
//	    }
//	    return buffer.toString();
//	  }
	
//	private 
	
//	@Override
//	public boolean checkInfo(CollectAttrInfo attrInfo) {
//		SocketTransferAttr socketAttr = (SocketTransferAttr)attrInfo.getTransferAttr();
//		boolean ischeckResult = false;
//		if(socketAttr.getIpAddress() == null) {
//			return ischeckResult;
//		}
//		String  addressIp = socketAttr.getIpAddress().trim();
//		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
//		Matcher matcher = pattern.matcher(addressIp); //以验证127.400.600.2为例
//		ischeckResult =matcher.matches();
//		return ischeckResult;
//	}

//	@SuppressWarnings("finally")
//	@Override
//	public boolean collect(CollectAttrInfo attrInfo, String xml) {
//		NMSSocketClient socketClient = null;
//		boolean isSendMsg = false;
//		String addressIp = null;
//		String oKmsg = null;
//		int addressPort = 800;
//		SocketTransferAttr socketAttr = (SocketTransferAttr)attrInfo.getTransferAttr();
//		try {	
//		    addressIp = socketAttr.getIpAddress()==null ? "" : socketAttr.getIpAddress();
//			addressPort = socketAttr.getPort();
//			socketClient = NMSSocketFactory.createSocketClient(addressIp, addressPort);
//			socketClient.connect();
//			socketClient.send(xml.getBytes("GB2312"));
//			Log.debug("[socket发送]采集任务发送成功，IP=" + socketAttr.getIpAddress() + ",potocol=" + xml);
//			oKmsg = socketClient.receive();
//			isSendMsg = true;
//		} catch (Exception e) {
//			Log.error("发送采集任务失败：连接信息：IP："+ socketAttr.getIpAddress() + "  PORT:" + socketAttr.getPort(), e);
//		}finally {
////			if(oKmsg != null && oKmsg.contains("OK")) {
////				sleep();
////				socketClient.disconnect();
////			}
//			if(oKmsg != null) {
//				sleep();
//				socketClient.disconnect();
//			}
////			else {
////				Timer timer = new Timer(true);
////				TimeOutTask task = new TimeOutTask(socketClient);
////				timer.schedule(task, 1000);
////			}
//		return isSendMsg;
//	}
//	}
//	
//	private void sleep() {
//		try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			Log.error("", e);
//		}
//	}
//	
//	
////	private class TimeOutTask extends TimerTask {
////		
////		private NMSSocketClient socketClient;
////		
////		public TimeOutTask(NMSSocketClient socketClient) {
////			this.socketClient = socketClient;
////		}
////
////		@Override
////		public void run() {
////			if(socketClient != null)
////				socketClient.disconnect();
////		}
////	}
	
	public static void main(String[] args) {
		String url = "http://10.13.2.10:8080".replaceAll("//", "");
		System.out.println(url.split(":")[1]);
		System.out.println(url.split(":")[2]);
	}
	
}
