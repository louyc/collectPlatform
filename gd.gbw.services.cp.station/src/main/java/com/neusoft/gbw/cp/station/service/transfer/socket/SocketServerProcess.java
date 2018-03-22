package com.neusoft.gbw.cp.station.service.transfer.socket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.station.constants.ConfigVariable;
import com.neusoft.gbw.cp.station.service.ReceiveHandler;
import com.neusoft.gbw.cp.station.service.transfer.AbstractTransfer;
import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;

public class SocketServerProcess extends AbstractTransfer{

	private Set<String> socketKeySet = null;
	private NMSSocketServer server = null;
	private List<Thread> threadPool = null;
	private BlockingQueue<String> queue = null;
	

	public SocketServerProcess(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void init() {
		super.init(queue);
		socketKeySet = new HashSet<String>();
		threadPool = new ArrayList<Thread>();
	}
	
	public void addKey(String str) {
		this.socketKeySet.add(str);
	}
	
	public Set<String> getSocketKeySet() {
		return this.socketKeySet;
	}

	public NMSSocketServer getServer() {
		return this.server;
	}
	
	public void start() {
		init();
		server = NMSSocketFactory.createSocketServer(ConfigVariable.PORT, new ClientProcess(this));
		try {
			server.listen();
		} catch (NMSSocketException e) {
			e.printStackTrace();
		}
	}
	
	public void startListener(String socketKey) {
		ReceiveHandler handler = new ReceiveHandler(socketKey, this);
		Thread thread = new Thread(handler);
		thread.start();
		this.threadPool.add(thread);
		System.out.println("Start Listener Key=" + socketKey);
	}
	
	public void stop() {
//		try {
//			server.unlisten();
//		} catch (NMSSocketException e) {
//		}
//		
//		if(dataProcessThread.isAlive()) {
//			dataProcessThread.interrupt();
//		}
		
		for(Thread thread : threadPool) {
			if(thread.isAlive()) {
				thread.interrupt();
			}
		}
	}

}
