package com.neusoft.np.arsf.core.transfer.socket.recieve;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.np.arsf.core.transfer.exception.NMSocketException;
import com.neusoft.np.arsf.core.transfer.socket.SocketChannel;

public class SocketServer {
	
	/**
	 * 客户端监听任务执行器
	 */
	private ExecutorService executor = null;
	
	/**
	 * 对象锁
	 */
	private Lock listenerLock = new ReentrantLock();
	
	/**
	 * 服务端Socket监听
	 */
	private SocketServerListener listener = null;
	
	public SocketServer(SocketChannel channel) {
		listener = new SocketServerListener(channel);
		executor = Executors.newSingleThreadExecutor();
	}

	/**
	 * 启动Socket服务端的监听
	 */
	public void listen() throws NMSocketException {
		listenerLock.lock();
		try {
			listener.setServiceName("Socket服务端监听线程");
			listener.init();
//			new Thread(listener).start();
//			ARSFToolkit.getServiceCentre().addService(listener);
			executor.submit(listener);
		} finally {
			listenerLock.unlock();
		}
	}
	
	/**
	 * 停止Socket服务端的监听
	 */
	public void unlisten() throws NMSocketException {
		listenerLock.lock();
		try {
			listener.close();
			executor.shutdown();
		} finally {
			listenerLock.unlock();
		}
	}
}
