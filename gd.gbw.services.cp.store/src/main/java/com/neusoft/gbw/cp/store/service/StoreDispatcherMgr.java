package com.neusoft.gbw.cp.store.service;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.store.constants.Constants;
import com.neusoft.gbw.cp.store.service.handler.StoreDispatcherHandler;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class StoreDispatcherMgr {
	
	private BlockingQueue<StoreInfo> storeInfoQueue;
	
	private NMService handler;
	
	
	public void init() {
		new ConfigLoader().loadConfig();
		storeInfoQueue  = ARSFToolkit.getBlockingQueue("storeInfoQueue", Constants.QUEUE_SIZE);
		
	}
	
	public void recieveData(StoreInfo info) {
		try {
			storeInfoQueue.put(info);
		} catch (InterruptedException e) {
			Log.debug(this.getClass().getName()+"队列storeInfoQueue存储报错",e);
		}
	}
	
	public void start() {
		init();
		try{
			handler = new StoreDispatcherHandler(storeInfoQueue);
			handler.setServiceName("store服务数据分发线程");
			ARSFToolkit.getServiceCentre().addService(handler);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"store线程启动报错",e);
		}
	}
	
	public void stop() {
		clear();
		ARSFToolkit.getServiceCentre().removeServiceByName(handler.getServiceName());
	}
	
	public void clear() {
		storeInfoQueue.clear();
		ChannelPool.getInstance().clear();
	}
}
