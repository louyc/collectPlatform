package com.neusoft.gbw.cp.build.application;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;


public class TaskReceiveMgr {

	private BlockingQueue<MessageTask> queue = null;
	private NMServiceCentre servicePool = null;
	private TaskReceiveProcess process = null;
	
	public void init() {
		queue = ARSFToolkit.getBlockingQueue(MessageTask.class, "messageTaskQueue", 1000);
		servicePool = new NMServiceCentre();
	}

	public void put(MessageTask task) throws InterruptedException {
		queue.put(task);
	}
	
	public MessageTask take() throws InterruptedException {
		return queue.take();
	}

	public void start() {
		for(int i = 0 ; i < 20 ; i++) {
			process = new TaskReceiveProcess(this);
			process.setServiceName("messageTaskThread#" + i);
			servicePool.addService(process);
		}
	}
	
	public void stop() {
		queue.clear();
		servicePool.stopAllServicePool();
	}
}
