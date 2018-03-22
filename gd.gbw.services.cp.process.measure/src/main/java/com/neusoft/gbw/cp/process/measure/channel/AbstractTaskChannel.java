package com.neusoft.gbw.cp.process.measure.channel;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public abstract class AbstractTaskChannel implements Channel {

	private BlockingQueue<CollectData> dataQueue = null;
	protected ChannelType type;
	private int size = 10000;
	
	public void init() {
		dataQueue = ARSFToolkit.getBlockingQueue(type.name() + "_queue", size);
	}
	
	public AbstractTaskChannel(ChannelType type) {
		this.type = type;
	}

	public void put(Object obj) {
		if (obj instanceof CollectData) {
			try {
				dataQueue.put((CollectData)obj);
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错中断",e);
			}
		}
	}
	
	public ChannelType getType() {
		return type;
	}

	public CollectData take() throws InterruptedException {
		return dataQueue.take();
	}
	
	public void clear() {
		this.dataQueue.clear();
	}
}
