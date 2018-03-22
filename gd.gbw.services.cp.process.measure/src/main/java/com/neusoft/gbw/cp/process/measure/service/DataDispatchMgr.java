package com.neusoft.gbw.cp.process.measure.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.service.handler.DataDispatcherHandler;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class DataDispatchMgr {
	
	private BlockingQueue<CollectData> dataDispatchorQueue;
	
	private DataDispatcherHandler handler;
	
	
	private void init() {
		dataDispatchorQueue  = ARSFToolkit.getBlockingQueue(CollectData.class,"storeInfoQueue", ProcessConstants.DATA_RECOVERY_QUEUE_SIZE);
	}
	
	public void recieveDate(CollectData obj) {
		try {
			dataDispatchorQueue.put(obj);
		} catch (InterruptedException e) {
			Log.error("dataDispatchorQueue队列接收数据失败", e);
		}
	}
	
	
	public void start() {
		init();
		try{
			handler = new DataDispatcherHandler(dataDispatchorQueue);
			handler.setServiceName("control服务数据分发线程");
			ARSFToolkit.getServiceCentre().addService(handler);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"线程启动报错", e);
		}
	}
	
	public void stop() {
		clear();
		ARSFToolkit.getServiceCentre().removeServiceByName(handler.getServiceName());
	}
	
	public void clear() {
		dataDispatchorQueue.clear();
	}
}
