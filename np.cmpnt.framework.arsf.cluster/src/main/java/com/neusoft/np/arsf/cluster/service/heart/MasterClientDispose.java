package com.neusoft.np.arsf.cluster.service.heart;

import java.net.Socket;

import com.neusoft.nms.common.net.socket.server.NMClientDispose;

public class MasterClientDispose extends NMClientDispose {

	private HeartbeatListenerMgr mgr = null;
	
	public MasterClientDispose(HeartbeatListenerMgr mgr) {
		this.mgr = mgr;
	}
	
	public boolean onConnect(Socket socket) {
		mgr.startMasterResponse(socket);
		return true;
	}
}