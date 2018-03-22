package com.neusoft.gbw.cp.process.realtime.service.transfer.quality;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.Log;

public class QualityTransferDispose {

	private NMSSocketServer policyServer = null;
	private final int FLEX_POLICY = 843;
	
	public void init() {
		policyServer = NMSSocketFactory.createSocketServer(FLEX_POLICY, new ConnClientProcess(this));
		try {
			policyServer.listen();
		} catch (NMSSocketException e) {
			Log.error("Flex安全策略端口启动失败", e);
		}
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			Log.info("Flex安全策略端口，IP=" + ip + "， PORT=" + FLEX_POLICY + "，启动完成");
		} catch (UnknownHostException e) {
		}
	}
	
	protected void add2StartPolicyHandler(Socket socket) {
		String socketKey = getSocketKey(socket);
		Log.info("Flex安全策略端口客户端连接，通信信息[" + socketKey + "]");
		FlexPolicyHandler handler = new FlexPolicyHandler(policyServer, socketKey, socket);
		Thread thread = new Thread(handler);
		thread.setName("Flex安全策略通信#" + socketKey);
		thread.start();
	}
	
	private String getSocketKey(Socket socket) {
	    return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}

	protected NMSSocketServer getPolicyServer() {
		return policyServer;
	}
}
