package com.neusoft.gbw.cp.process.realtime.service.transfer.quality;

import java.io.IOException;
import java.net.Socket;

import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class FlexPolicyHandler extends NMService {
	
	private NMSSocketServer server = null;
	private String key = null;
	private Socket socket = null;
	
	public FlexPolicyHandler(NMSSocketServer server, String key, Socket socket) {
		this.server = server;
		this.key = key;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			byte[] bytes = new byte[22];
			String str = null;
			while(true) {
				socket.getInputStream().read(bytes);
				if (bytes == null) {
					Thread.sleep(100);
					continue;
				}
				str = new String(bytes);
				Log.debug(str);
				break;
			}
			
			if (str.trim().equals("<policy-file-request/>")) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("<?xml version=\"1.0\"?>");
				buffer.append("<!DOCTYPE cross-domain-policy SYSTEM \"http://www.adobe.com/xml/dtds/cross-domain-policy.dtd\">");
				buffer.append("<cross-domain-policy>");
				buffer.append("<site-control permitted-cross-domain-policies=\"all\"/>");
				buffer.append("<allow-access-from domain=\"*\" to-ports=\"*\"/>");
				buffer.append("</cross-domain-policy>");
				server.send(key, buffer.toString());
				Log.debug("线程(" + this.serviceName + ")接收到Flex安全信息验证，发送安全信息的回应，并启动正常推送数据");
			}
			socket.close();
			
			
		} catch (Exception e) {
			Log.error("连接通信客户端异常", e);
			return;
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
				}
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
