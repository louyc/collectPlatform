package com.neusoft.gbw.cp.collect.service.control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.neusoft.gbw.cp.collect.constants.Constants;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class EquCollectCotrolMgr {
	
	private String monitorId = null;
	private String equCode = null;
	private EquCollectControl statusCtrl = null;
	private PriorityBlockingQueue<CollectTask> occupTaskPriQueue = null;
	private PriorityBlockingQueue<CollectTask> otherTaskPriQueue = null;
	private BlockingQueue<CollectTask> collectQueue = null;
	private EquOccupTaskPriAssignHandler equOccupTaskPriHandler = null;
	private EquOtherTaskPriAssignHandler equOtherTaskPriHandler = null;
	private EquCollectHandler collectHandler = null;
	private NMServiceCentre threadPool = null;
	
	public EquCollectCotrolMgr(String equCode, StationControlMgr stationCtrlMgr) {
		this.equCode = equCode;
		this.monitorId = stationCtrlMgr.getMonitorID();
		this.threadPool = stationCtrlMgr.getNMServiceCentre();
		this.statusCtrl = stationCtrlMgr.getEquStatusControl();
	}
	
	protected void init(long monitorId) {
		collectQueue = ARSFToolkit.getBlockingQueue(CollectTask.class, Constants.QueueName.EQU_COLLECT_QUEUE + "_" + monitorId, Constants.EQU_QUEUE_SIZE);
		occupTaskPriQueue = new PriorityBlockingQueue<CollectTask>(Constants.OCCUP_QUEUE_SIZE, new TaskPriorityControlHandler(1));
		//此队列处理除了占用接收机任务的所有任务，加上音频的stop任务
		otherTaskPriQueue = new PriorityBlockingQueue<CollectTask>(Constants.PRIORITY_CTL_DATA_QUEUE, new TaskPriorityControlHandler(2));
	}
	
	protected void occupPut(CollectTask task) {
		occupTaskPriQueue.put(task);
	}
	
	protected CollectTask occupTake() throws InterruptedException {
		return occupTaskPriQueue.take();
	}
	
	protected CollectTask occupPeek() {
		return occupTaskPriQueue.peek();
	}
	
	protected void otherPut(CollectTask task) {
		otherTaskPriQueue.put(task);
	}
	
	protected CollectTask otherTake() throws InterruptedException {
		return otherTaskPriQueue.take();
	}
	
	protected CollectTask otherPeek() {
		return otherTaskPriQueue.peek();
	}
	
	protected void putCollect(CollectTask task) {
		try {
			collectQueue.put(task);
		} catch (InterruptedException e) {
			Log.debug(this.getClass().getName()+"队列存取失败",e);
		}
	}
	
	protected CollectTask takeCollect() throws InterruptedException {
		return collectQueue.take();
	}
	

	protected void start() {
		equOccupTaskPriHandler = new EquOccupTaskPriAssignHandler(this);
		equOccupTaskPriHandler.setServiceName(Constants.HandlerName.EQU_OCCUP_ASSIGN_THREAD_NAME + "#" + equCode);
		threadPool.addService(equOccupTaskPriHandler);
		
		equOtherTaskPriHandler = new EquOtherTaskPriAssignHandler(this);
		equOtherTaskPriHandler.setServiceName(Constants.HandlerName.EQU_OTHER_ASSIGN_THREAD_NAME + "#" + equCode);
		threadPool.addService(equOtherTaskPriHandler);
		
		collectHandler = new EquCollectHandler(this);
		collectHandler.setServiceName(Constants.HandlerName.EQU_COLLECT_THREAD_NAME + "#" + equCode);
		threadPool.addService(collectHandler);
	}
	
	protected void stop() {
		ARSFToolkit.getServiceCentre().removeServiceByName(Constants.HandlerName.EQU_OCCUP_ASSIGN_THREAD_NAME + "#" + equCode);
		ARSFToolkit.getServiceCentre().removeServiceByName(Constants.HandlerName.EQU_OTHER_ASSIGN_THREAD_NAME + "#" + equCode);
		ARSFToolkit.getServiceCentre().removeServiceByName(Constants.HandlerName.EQU_COLLECT_THREAD_NAME + "#" + equCode);
	}
	
	protected EquCollectControl getEquStatusControl() {
		return this.statusCtrl;
	}

	protected String getEquCode() {
		return equCode;
	}

	public String getMonitorId() {
		return monitorId;
	}
	
}
