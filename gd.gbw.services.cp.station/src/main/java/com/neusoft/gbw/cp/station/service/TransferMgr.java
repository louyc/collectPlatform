package com.neusoft.gbw.cp.station.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.station.service.transfer.ITransfer;
import com.neusoft.gbw.cp.station.service.transfer.servlet.ServletServerProcess;
import com.neusoft.gbw.cp.station.service.transfer.socket.SocketServerProcess;


public class TransferMgr {
	private BlockingQueue<String> queue;
	private ITransfer socketServer = null;
	private ITransfer servletServer = null;
	
	public void init() {
		
		queue = new ArrayBlockingQueue<String>(1000);
		new ConfigLoader().loadConfig();
		DataProcessHandler handler = new DataProcessHandler(queue);
		new Thread(handler).start();
	
	}
	
	public void start() {
		init();
		
		if(socketServer == null) {
			socketServer = new SocketServerProcess(queue);
			socketServer.start();
		}
		
		if(servletServer == null) {
			servletServer = new ServletServerProcess(queue);
			servletServer.start();
		}
	}
	
	public void stop() {
		if(socketServer != null) {
			socketServer.stop();
		}
		
		if(servletServer != null) {
			servletServer.stop();
		}
	}
	

}
