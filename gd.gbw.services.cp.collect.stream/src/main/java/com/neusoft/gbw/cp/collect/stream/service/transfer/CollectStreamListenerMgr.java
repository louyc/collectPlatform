package com.neusoft.gbw.cp.collect.stream.service.transfer;

import java.net.Socket;

import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class CollectStreamListenerMgr {
	
	private final int DEFUALT_PORT = 50006;
	private NMSSocketServer server = null;
	private NMServiceCentre pool = new NMServiceCentre();
	private int count = 1;

	public void openListener() {
		server = NMSSocketFactory.createSocketServer(DEFUALT_PORT, new StreamClientDispose(this));
		try {
			server.listen();
		} catch (NMSSocketException e) {
			Log.error("平台集群心跳端口启动失败", e);
			return;
		}
//		Log.info("[音频采集]平台音频采集端口启动，IP=" + ip + "， PORT=" + DEFUALT_PORT + "，启动完成");
	}
	/**
	 * 创建发送服务端
	 * @param socket
	 */
	protected void startCollectResquest(Socket socket) {
		String socketKey = getSocketKey(socket);
		Log.info("平台采集音频接收到客户端的连接，需要进行身份验证。通信信息[" + socketKey + "]");
		try{
			CollectStreamResquestHandler transferHandler = new CollectStreamResquestHandler(socketKey, this);
			transferHandler.setServiceName("平台采集音频请求线程#" + socketKey + "_" + count++);
			pool.addService(transferHandler);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"线程启动报错", e);
		}
	}
	
	/**
	 * 创建接收服务
	 * @param socketKey
	 */
	protected void startCollectVoiceStream(String socketKey) {
		try{
			CollectVoiceStreamHandler streamHandler = new CollectVoiceStreamHandler(socketKey, server);
			streamHandler.setServiceName("平台采集音频线程#" + socketKey + "_" + count);
			pool.addService(streamHandler);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"线程启动报错",e);
		}
//		return streamHandler;
	}
	
	private String getSocketKey(Socket socket) {
	    return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}
	
	public NMSSocketServer getServer() {
		return server;
	}

	public void closeListener() {
		if (server != null)
			try {
				server.unlisten();
			} catch (NMSSocketException e) {
				e.printStackTrace();
			}
		pool.stopAllThreads();
	}
}
