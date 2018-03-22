package com.neusoft.gbw.cp.station.service.transfer.socket;

import java.net.Socket;

import com.neusoft.nms.common.net.socket.server.NMClientDispose;

public class ClientProcess extends NMClientDispose {
	
	private SocketServerProcess mgr = null;
	
	public ClientProcess(SocketServerProcess mgr) {
		this.mgr = mgr;
	}

	public boolean onConnect(Socket socket) {
		String socketKey = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
		mgr.startListener(socketKey);
		return true;
	}
}

