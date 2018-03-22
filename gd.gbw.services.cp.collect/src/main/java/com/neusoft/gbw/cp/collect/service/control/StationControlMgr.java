package com.neusoft.gbw.cp.collect.service.control;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.collect.constants.Constants;
import com.neusoft.gbw.cp.collect.vo.SiteConfig;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.CollectTaskType;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControl;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControlMode;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class StationControlMgr {

	private String monitorID;
	private SiteConfig config = null;
	private BlockingQueue<CollectTask> taskQueue = null;
//	private BlockingQueue<CollectTask> changeStatusQueue = null;
	private EquDispatchHandler dispatchHandler = null;
//	private SyntStopTaskStatusHandler stopHandler = null;
	private EquCollectControl equCtrl = null;
	private EquStatusControl statusCtrl = null;
	private NMServiceCentre threadPool = null;
	
	public StationControlMgr(SiteConfig config) {
		this.config = config;
		this.monitorID = config.getMonitorID();
	}
	
	public void init() {
		equCtrl = new EquCollectControl(); //初始化接收机控制
		threadPool = new NMServiceCentre();
		taskQueue = ARSFToolkit.getBlockingQueue(CollectTask.class, Constants.QueueName.STATION_DIS_QUEUE, Constants.EQU_QUEUE_SIZE);
//		changeStatusQueue = ARSFToolkit.getBlockingQueue(CollectTask.class, Constants.QueueName.STATION_CHANGE_STATUS_QUEUE, Constants.EQU_CHANGE_STATUS_QUEUE_SIZE);
	}
	
	public void put(CollectTask task) {
		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			Log.debug("队列抛出异常"+e);
		}
	}
	
	protected CollectTask take() throws InterruptedException {
		return taskQueue.take();
	}
	
//	protected CollectTask takeStop() throws InterruptedException {
//		return changeStatusQueue.take();
//	}
	
	protected void startEquCollect(String key, CollectTask task) {
		long monitorId = task.getBusTask().getMonitor_id();
		statusCtrl = new EquStatusControl();
		statusCtrl.addEquStatusCotrol(key);
		statusCtrl.addCurrentCollectTask(key, task);
		EquStatusControlMode.getInstance().addStation(monitorId, statusCtrl);
		
		this.monitorID = String.valueOf(monitorId);  //20160824   刷选后的站点id  
		
		EquCollectCotrolMgr equCollectMgr = new EquCollectCotrolMgr(key, this);
		equCollectMgr.init(monitorId);
		equCollectMgr.start();
		putTask(equCollectMgr, task);
		equCtrl.addEquCollectCotrol(key, equCollectMgr);
	}
	
	protected void putTask(EquCollectCotrolMgr equCollectMgr, CollectTask task) {
		CollectTaskType type = task.getAttrInfo().getColTaskType();
		try {
			switch(type) {
//			case general:
//				equCollectMgr.otherPut(task);
//				break;
//			case stop:
//				equCollectMgr.otherPut(task);
////				changeStatusQueue.put(task);
//				break;
			case occup:
				equCollectMgr.occupPut(task);
				break;
			default:
				equCollectMgr.otherPut(task);
				break;
			}
		}catch(Exception e) {
			Log.debug("队列存放已满"+type+"   错误原因："+e);
		}
	}
	
	protected EquCollectControl getEquStatusControl() {
		return this.equCtrl;
	}
	
	protected NMServiceCentre getNMServiceCentre() {
		return this.threadPool;
	}
	
	public void start() {
		dispatchHandler = new EquDispatchHandler(this);
		dispatchHandler.setServiceName(Constants.HandlerName.EQU_CTL_THREAD_NAME + "#" + monitorID);
		ARSFToolkit.getServiceCentre().addService(dispatchHandler);
		
//		stopHandler = new SyntStopTaskStatusHandler(this);
//		stopHandler.setServiceName(Constants.HandlerName.SYNT_CHANGE_THREAD_NAME + "#" + monitorID);
//		ARSFToolkit.getServiceCentre().addService(stopHandler);
	}
	
	public void stop() {
		ARSFToolkit.getServiceCentre().removeServiceByName(Constants.HandlerName.EQU_CTL_THREAD_NAME + "#" + monitorID);
		equCtrl.clear();
	}
	
	protected SiteConfig getConfig() {
		return this.config;
	}
	
	public String getMonitorID() {
		return monitorID;
	}
}
