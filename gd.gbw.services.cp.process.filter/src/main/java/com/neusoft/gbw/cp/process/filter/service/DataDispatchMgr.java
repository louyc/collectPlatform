package com.neusoft.gbw.cp.process.filter.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class DataDispatchMgr {
	
	private BlockingQueue<CollectData> dataDispatchorQueue;
	private final int DATA_RECOVERY_QUEUE_SIZE = 10000;
	private DataDispatcherHandler handler;
	
	private void init() {
		dataDispatchorQueue  = ARSFToolkit.getBlockingQueue(CollectData.class,"storeInfoQueue", DATA_RECOVERY_QUEUE_SIZE);
	}
	
	public void recieveDate(CollectData obj) {
		try {
			dataDispatchorQueue.put(obj);
		} catch (InterruptedException e) {
			Log.error("dataDispatchorQueue队列接收数据失败", e);
		}catch(Exception e){
			Log.debug("NullPointerException    IllegalArgumentException  ClassCastException       DataDispatchMgr ");
		}
	}
	
	
	public void start() {
		init();
		handler = new DataDispatcherHandler(dataDispatchorQueue);
		handler.setServiceName("control服务数据分发线程");
		ARSFToolkit.getServiceCentre().addService(handler);
	}
	
	public void stop() {
		clear();
		ARSFToolkit.getServiceCentre().removeServiceByName(handler.getServiceName());
	}
	
	public void clear() {
		dataDispatchorQueue.clear();
	}
}
