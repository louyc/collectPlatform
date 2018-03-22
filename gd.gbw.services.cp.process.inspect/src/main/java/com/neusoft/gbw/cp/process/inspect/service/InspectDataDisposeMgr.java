package com.neusoft.gbw.cp.process.inspect.service;


import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.process.ITaskProcess;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class InspectDataDisposeMgr {
	
	private BlockingQueue<CollectData> dataQueue = null;
	private InspectDataDisposeHandler handler = null;
	private NMServiceCentre pool = new NMServiceCentre();
	
	public void put(CollectData data) {
		try {
			dataQueue.put(data);
		} catch (InterruptedException e) {
			Log.debug(this.getClass().getName()+"队列存储报错",e);
		}
	}
	
	public BlockingQueue<CollectData> getDataQueue() {
		return dataQueue;
	}

	public CollectData take() throws InterruptedException {
		return dataQueue.take();
	}
	
	public ITaskProcess getInspectDispose(String name) {
		return InspectTaskContext.getInstance().getTaskProcessor(name);
	}

	public void init() {
		dataQueue = ARSFToolkit.getBlockingQueue(CollectData.class, "inpectTaskQueue", 1000);
	}
	
	public void start() {
		init();
		for(int i=0;i<InspectConstants.INSPECT_TASK_DISPOSE_THREAD_NUM;i++){
			try{
				handler = new InspectDataDisposeHandler(this);
				handler.setServiceName("巡检任务处理_thread" + i);
				pool.addService(handler);
			}catch(Exception e){
				Log.debug(this.getClass().getName()+"线程启动报错", e);
			}
		}
	}
	
	public void stop() {
		pool.stopAllServicePool();
	}
}
