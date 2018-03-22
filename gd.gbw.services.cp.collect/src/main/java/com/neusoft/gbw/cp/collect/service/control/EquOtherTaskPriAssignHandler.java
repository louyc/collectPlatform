package com.neusoft.gbw.cp.collect.service.control;

import java.util.Timer;

import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.CollectTaskType;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControl;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControlMode;
import com.neusoft.gbw.cp.load.data.build.domain.vo.EquOccupStatus;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class EquOtherTaskPriAssignHandler extends NMService{
	
	private EquCollectCotrolMgr ctrlMgr = null;
	private EquStatusControl statusCtrl = null;
	
	public EquOtherTaskPriAssignHandler(EquCollectCotrolMgr ctrlMgr) {
		this.ctrlMgr = ctrlMgr;
	}

	@Override
	public void run() {
		CollectTask task = null;
		while(isThreadRunning()) {
			//实际获取采集任务，下发采集执行
			try {
				task = ctrlMgr.otherTake();
			} catch (InterruptedException e) {
				Log.error("", e);
				break;
			}
			
			//判断任务中采集任务类型，如果为一般，可以不设置状态直接下发，如果为占用，再设置状态
			CollectTaskType taskType = task.getAttrInfo().getColTaskType();
			switch(taskType) {
			//注掉该逻辑，该逻辑在构建服务中做
//			case general:
////				//对连续任务进行延时，避免过于频繁无法处理,此逻辑控制在构建里
////				sleep(CollectVariable.COLLECT_SEP_TIME);
////				ctrlMgr.putCollect(task);
//				break;
			case stop:
				
				String id = getCollectOccupID(task);
				if (id == null) {
					Log.warn("[采集服务]当前任务没有查询到占用属性COLLECT_OCCUP_KEY");
					continue;
				}
				
				String equCode = task.getAttrInfo().getFirstEquCode();
				if (equCode == null) {
					Log.warn("[采集服务]当前任务没有查询处理应用的接收机编码。 MonitorID=" + ctrlMgr.getMonitorId());
					continue;
				}
				
				EquStatusControlMode mode = EquStatusControlMode.getInstance();
//				long monitorId = Long.parseLong(ctrlMgr.getMonitorId());
				long monitorId = task.getBusTask().getMonitor_id();
				this.statusCtrl = mode.getStationControl(monitorId);
				equCode = equCode +monitorId;
				EquOccupStatus equStatus = statusCtrl.getEquOccupStatus(equCode);
				if (equStatus == null) {
					Log.warn("[采集服务]没有查询到当前任务应用接收机编码对应的状态信息。MonitorID=" + monitorId + ", EquCode=" + equCode);
					continue;
				}
				
//				String lastId = equStatus.getOccupKey();
//				if (lastId!= null && !lastId.equals(id)) {
//					Log.warn("[采集服务]当前任务与占用任务的唯一ID无法对应，不能置换占用任务的状态。" + "{lastId=" + lastId + "}" + "}, {Id=" + id + "}");
//					continue;
//				}
				
				Timer time = CollectTaskContext.getModel().getTimer(id);
				if (time == null) 
					Log.info("[采集服务]当前采集接收机无法查找对应的OccupKey，代表已经关闭此超时计时器");
				else
					time.cancel();
				statusCtrl.setFreeStatusByEquCode(equCode);
				statusCtrl.removeCollectTaskByEquCode(equCode);
				
				TaskType type = task.getTaskType();
				Log.info("[采集服务]获取当前任务为类型，判断是否向站点下发stop消息，TaskType=" + type);
				//根据任务是否是系统任务或者人为操作任务，系统任务收测单元等关闭操作，stop不进行下发 add by jiahao
				if(type != null && !type.equals(TaskType.temporary)) {
					continue;
				}
				break;
			default:
				break;
			}
			ctrlMgr.putCollect(task);
		}
	}
	
	private String getCollectOccupID(CollectTask task) {
		Object id = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY);
		return id == null ? null : id.toString();
	}

//	private void sleep(long millis) {
//		try {
//			Thread.sleep(millis);
//		} catch (InterruptedException e) {
//		}
//	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
