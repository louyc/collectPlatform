//package com.neusoft.gbw.cp.collect.service.control;
//
//import com.neusoft.np.arsf.common.util.NMService;
//
//public class SyntStopTaskStatusHandler extends NMService {
//	
////	private StationControlMgr ctrlMgr = null;
////	private EquStatusControl statusCtrl = null;
//	
//	public SyntStopTaskStatusHandler(StationControlMgr ctrlMgr) {
//		this.ctrlMgr = ctrlMgr;
//	}
//
//	@Override
//	public void run() {
////		CollectTask task = null;
////		while(isThreadRunning()) {
////			try {
////				task = ctrlMgr.takeStop();
////			} catch (InterruptedException e) {
////				break;
////			}
//			
////			String id = getCollectOccupID(task);
////			if (id == null) {
////				Log.warn("[采集服务]当前任务没有查询到占用属性COLLECT_OCCUP_KEY");
////				continue;
////			}
////			
////			String equCode = task.getAttrInfo().getFirstEquCode();
////			if (equCode == null) {
////				Log.warn("[采集服务]当前任务没有查询处理应用的接收机编码。 MonitorID=" + ctrlMgr.getMonitorID());
////				continue;
////			}
////			
////			EquStatusControlMode mode = EquStatusControlMode.getInstance();
////			int monitorId = Integer.parseInt(ctrlMgr.getMonitorID());
////			this.statusCtrl = mode.getStationControl(monitorId);
////			EquOccupStatus equStatus = statusCtrl.getEquOccupStatus(equCode);
////			if (equStatus == null) {
////				Log.warn("[采集服务]没有查询到当前任务应用接收机编码对应的状态信息。MonitorID=" + ctrlMgr.getMonitorID() + ", EquCode=" + equCode);
////				continue;
////			}
////			
////			String lastId = equStatus.getOccupKey();
////			if (lastId!= null && !lastId.equals(id)) {
////				Log.warn("[采集服务]当前任务与占用任务的唯一ID无法对应，不能置换占用任务的状态。{" + task.toString() + "}, {lastId=" + lastId + "}" + "}, {Id=" + id + "}");
////				continue;
////			}
////			
////			Timer time = CollectTaskContext.getModel().getTimer(id);
////			if (time == null) 
////				Log.info("[采集服务]当前采集接收机无法查找对应的OccupKey，代表已经关闭此超时计时器");
////			else
////				time.cancel();
////			statusCtrl.setFreeStatusByEquCode(equCode);
////			statusCtrl.removeCollectTaskByEquCode(equCode);
//			//Log.info("");
//			
//			
////		}
//	}
//	
////	private String getCollectOccupID(CollectTask task) {
////		Object id = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY);
////		return id == null ? null : id.toString();
////	}
//
//	@Override
//	public String getServiceName() {
//		return this.serviceName;
//	}
//
//	@Override
//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}
//
//}
