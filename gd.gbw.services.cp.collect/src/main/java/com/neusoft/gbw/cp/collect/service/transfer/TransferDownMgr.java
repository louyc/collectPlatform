package com.neusoft.gbw.cp.collect.service.transfer;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.collect.constants.CollectVariable;
import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.service.handle.CollectTaskDispatchHandler;
import com.neusoft.gbw.cp.collect.service.transfer.servlet.ServletCollectProcessor;
import com.neusoft.gbw.cp.collect.service.transfer.socket.SocketCollectProcessor;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.TransferType;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class TransferDownMgr {

	private BlockingQueue<CollectTask> taskQueue = null;
	private CollectTaskDispatchHandler dispatchHandler = null;
	private String COLLECT_DISPATCH_NAME = "采集分发总控制线程";
	private NMServiceCentre threadPool = null;
	
	public void init() {
		threadPool = new NMServiceCentre();
		taskQueue = ARSFToolkit.getBlockingQueue(CollectTask.class, "TaskQueue", CollectVariable.READ_TIME_QUEUE_SIZE);
		CollectTaskContext.getModel().add(TransferType.SOCKET, new SocketCollectProcessor());
		CollectTaskContext.getModel().add(TransferType.SERVLET, new ServletCollectProcessor());
	}

	/**
	 * 接收任务 
	 * @param task
	 */
	public void putTask(CollectTask task) {
		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			Log.debug("InterruptedException   中断异常      TransferDownMgr ");
		}
	}
	
	public CollectTask take() throws InterruptedException {
		return taskQueue.take();
	}
	
	public void start() {
		init();
		dispatchHandler = new CollectTaskDispatchHandler(this);
		dispatchHandler.setServiceName(COLLECT_DISPATCH_NAME);
		threadPool.addService(dispatchHandler);
//		/**
//		 * 启动实时采集线程池
//		 */
//		threadPool.startServicePool("readTimeTaskTPool", RealTransferDownHandler.class, readTimeTaskQueue ,CollectVariable.REAL_TIME_COLLECT_TASK_THREAD_POLL_SIZE);
//		/**
//		 * 启动计划周期采集线程池
//		 */
//		threadPool.startServicePool("cycleCollectTaskPool", CycleTransferDownHandler.class , planAndCycleTaskQueue ,CollectVariable.PLAN_CYCLE_COLLECT_TASK_THREAD_POLL_SIZE);
	}
	
	public void stop() {
		threadPool.removeServiceByName(COLLECT_DISPATCH_NAME);
		clear();
	}
	
	public void clear() {
		try{
		taskQueue.clear();
		}catch(UnsupportedOperationException e){
			Log.debug("UnsupportedOperationException  TransferDownMgr");
		}
	}
}
