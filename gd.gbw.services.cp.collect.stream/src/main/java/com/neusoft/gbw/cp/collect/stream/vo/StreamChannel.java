package com.neusoft.gbw.cp.collect.stream.vo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.neusoft.np.arsf.base.bundle.Log;

public class StreamChannel {

	private String channelCode = null;
	private BlockingQueue<StreamParam> queue = null;
	
	public StreamChannel(String channelCode) {
		this.channelCode = channelCode;
		this.queue = new ArrayBlockingQueue<StreamParam>(100);
	}
	
	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void put(StreamParam obj) throws InterruptedException {
		this.queue.put(obj);
	}
	
	public StreamParam take() throws InterruptedException {
		return this.queue.take();
	}
}
