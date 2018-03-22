package com.neusoft.gbw.cp.store.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.store.constants.Constants;
import com.neusoft.gbw.cp.store.service.handler.BatchStoreHandler;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.cp.store.vo.StoreOperType;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class Channel {

	private BlockingQueue<StoreInfo> queue = null;
	
	private String channelName; 
	
	private StoreOperType operType;
	
	private BatchStoreHandler handler = null;
	
	private StoreDBExecutor executor = null;
	
	public Channel(String channelName) {
		this.channelName = channelName;
	}
	
	public void init() {
		String queueName = channelName + "_Queue";
		queue = ARSFToolkit.getBlockingQueue(queueName, Constants.QUEUE_SIZE);
	}
	
	public void put(StoreInfo info) {
		operType = info.getOperType();
		queue.add(info);
	}
	
	public void open() {
		init();
		try{
			handler = new BatchStoreHandler(this);
			handler.setServiceName(channelName + "#批量入库线程");
			ARSFToolkit.getServiceCentre().addService(handler);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"批量入库线程报错", e);
		}
		executor = new StoreDBExecutor();
	}
	
	public void close() {
		ARSFToolkit.getServiceCentre().removeServiceByName(handler.getServiceName());
		queue.clear();
	}
	
	public void store(List<Map<String, Object>> infoList) {
		executor.store(channelName, operType, infoList);
	}

	public BlockingQueue<StoreInfo> getQueue() {
		return queue;
	}
}
