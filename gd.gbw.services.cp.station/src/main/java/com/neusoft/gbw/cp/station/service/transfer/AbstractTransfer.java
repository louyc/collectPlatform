package com.neusoft.gbw.cp.station.service.transfer;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractTransfer implements ITransfer{
	
	private BlockingQueue<String> queue;
	
	public void init(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
	public void start() {
		init();

	}
	
	public void put(Object xml) {
		try {
			if(xml instanceof String) {
				queue.put(xml.toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String take() throws InterruptedException {
		return queue.take();
	}
	
	public void stop() {
		queue.clear();
	}
}
