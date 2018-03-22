package com.neusoft.gbw.cp.collect.stream.service.transfer;

import java.net.Socket;

import com.neusoft.nms.common.net.socket.server.NMClientDispose;

public class StreamClientDispose extends NMClientDispose {
	
	private CollectStreamListenerMgr mgr = null;

	public StreamClientDispose(CollectStreamListenerMgr mgr) {
		this.mgr = mgr;
	}
	
	public boolean onConnect(Socket socket) {
		mgr.startCollectResquest(socket);
		return true;
	}
}
