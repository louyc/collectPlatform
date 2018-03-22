package com.neusoft.gbw.cp.collect.service.transfer;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.collect.constants.CollectVariable;
import com.neusoft.gbw.cp.collect.service.handle.RecoveryTransferUpHandler;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class TransferUpMgr {
	
	private BlockingQueue<String> recoveryDateQueue;
	private NMServiceCentre threadPool = null;
	
	
	public void recieveRecovertDate(String protcolXml) {
		try {
			recoveryDateQueue.put(protcolXml);
		} catch (InterruptedException e) {
		}
	}
	
	
	public void init() {
		recoveryDateQueue = ARSFToolkit.getBlockingQueue("recoveryDateQueue", CollectVariable.RECOVERY_DATE_QUEUE_SIZE);
		threadPool = new NMServiceCentre();
	}
	
	public void start() {
		init();
		//启动回收回收数据线程池
		threadPool.startServicePool("recoveryDatePool", RecoveryTransferUpHandler.class, recoveryDateQueue,CollectVariable.RECOVERY_DATE_THREAD_POLL_SIZE);
	}
	
	
	public void stop() {
		clear();
	}
	
	
	public void clear() {
		recoveryDateQueue.clear();
		ARSFToolkit.getServiceCentre().removeServicePoolByName("recoveryDatePool");
	}
}
