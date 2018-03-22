package com.neusoft.gbw.cp.process.realtime.service.transfer.quality;

import java.net.Socket;

import com.neusoft.nms.common.net.socket.server.NMClientDispose;

public class ConnClientProcess extends NMClientDispose {

	private QualityTransferDispose transfer = null;
	
	public ConnClientProcess(QualityTransferDispose transfer) {
		this.transfer = transfer;
	}
	
	public boolean onConnect(Socket socket) {
		transfer.add2StartPolicyHandler(socket);
		return true;
	}
}
