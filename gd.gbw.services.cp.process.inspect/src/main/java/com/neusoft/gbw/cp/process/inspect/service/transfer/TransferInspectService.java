package com.neusoft.gbw.cp.process.inspect.service.transfer;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class TransferInspectService {

	private BlockingQueue<MonitorDevice> monitorQueue = null;
	private MoniTrasferDisposeHandler moniHandler = null;
	private NMServiceCentre pool = new NMServiceCentre();
	
	public void put(MonitorDevice device) {
		try {
			monitorQueue.put(device);
		} catch (InterruptedException e) {
			Log.debug("monitorQueue接收数据封装入列失败");
		}
	}
	
	public MonitorDevice getData() throws InterruptedException {
		return monitorQueue.take();
	} 
	
	public void init() {
		monitorQueue = ARSFToolkit.getBlockingQueue(MonitorDevice.class, "monitorInspect", 1000);
	}
	
	public void start(){
		//开通线程进行连通性校验
		for(int i=0;i<InspectConstants.INSPECT_TASK_DISPOSE_THREAD_NUM;i++){
			try{
				moniHandler = new MoniTrasferDisposeHandler(this);
				moniHandler.setServiceName("站点连通性校验_thread" + i);
				pool.addService(moniHandler);
			}catch(Exception e){
				Log.debug(this.getClass().getName()+"线程启动报错", e);
			}
		}
	}
	
	public void stop(){
		pool.stopAllServicePool();
	}
	
}
