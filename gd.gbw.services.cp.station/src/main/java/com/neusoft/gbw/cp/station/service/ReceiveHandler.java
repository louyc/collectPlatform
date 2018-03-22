package com.neusoft.gbw.cp.station.service;

import com.neusoft.gbw.cp.station.service.transfer.socket.SocketServerProcess;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;

public class ReceiveHandler implements Runnable{
	
	private SocketServerProcess process = null;
	private String socketKey = null;
	
	public ReceiveHandler(String socketKey, SocketServerProcess process) {
		this.socketKey = socketKey;
		this.process = process;
	}

	@Override
	public void run() {
			
		NMSSocketServer server = process.getServer();
		String result = null;
		StringBuffer buffer = new StringBuffer();
		try {
			while(true) {
				result = server.receive(socketKey);
				if (result == null) {
					continue;
				}
				if (!result.startsWith("<?xml")) {
					continue;
				}
				buffer.append(result);
				while(true) {
					result = server.receive(socketKey);
					if (result == null) {
						break;
					}
					
					if (result.startsWith("</Msg>")) {
						buffer.append(result);
						break;
					}
					buffer.append(result);
				}
				break;
			}
			server.send(socketKey, "OK");
			System.out.println("获取:" + buffer.toString());
		} catch (NMSSocketException e2) {
			e2.printStackTrace();
		}
		process.put(buffer.toString());
	}
}
