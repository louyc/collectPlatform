package com.neusoft.gbw.cp.store.service.handler;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.store.service.Channel;
import com.neusoft.gbw.cp.store.service.ChannelPool;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class StoreDispatcherHandler extends NMService{
	
	private BlockingQueue<StoreInfo> queue = null;
	
	private ChannelPool pool = null;
	
	public StoreDispatcherHandler(BlockingQueue<StoreInfo> queue) {
		this.queue = queue;
		pool = ChannelPool.getInstance();
	}

	@Override
	public void run() {
		StoreInfo info = null;
		while(isThreadRunning()) {
			try {
				info = queue.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列queue存储报错中断",e);
				break;
			}
			dispatch(info);
		}
	}
	
	/**
	 * 数据分发方法
	 * @param info
	 */
	private void dispatch(StoreInfo info) {
		String label = info.getLabel();
		Channel channel = null;
		if (pool.containKey(label)) {
			channel = pool.getChannel(label);
		} else {
			channel = new Channel(label);
			channel.open();
			pool.putChannel(label, channel);
		}
		
		channel.put(info);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}

}
