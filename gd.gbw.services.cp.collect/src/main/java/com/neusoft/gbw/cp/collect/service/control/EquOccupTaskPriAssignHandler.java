package com.neusoft.gbw.cp.collect.service.control;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;

import com.neusoft.gbw.cp.collect.constants.Constants;
import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.service.build.ReportDataBuilder;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControl;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControlMode;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class EquOccupTaskPriAssignHandler extends NMService {
	
	private EquCollectCotrolMgr ctrlMgr = null;
	private EquStatusControl statusCtrl = null;
	private CollectTask lastTask = null;
	private String equCode = null;
	
	public EquOccupTaskPriAssignHandler(EquCollectCotrolMgr ctrlMgr) {
		this.ctrlMgr = ctrlMgr;
		this.equCode = ctrlMgr.getEquCode();
		EquStatusControlMode mode = EquStatusControlMode.getInstance();
		long monitorId = Long.parseLong(ctrlMgr.getMonitorId());
		this.statusCtrl = mode.getStationControl(monitorId);
	}

	
	@Override
	public void run() {
		CollectTask task = null;
		while(isThreadRunning()) {
			//首先获取优先级队列的第一个数据索引
			task = ctrlMgr.occupPeek();
			if (task == null) {
				sleep(200);
				continue;
			}
			if (lastTask != null) {
				//#########################################第一种方案，实时音频监测，可以返回接收机正在占用-start##############################################
				//如果是强占任务，则直接下发任务
				//如果不是强占任务，1、接收机占用着并且任务是检测占用任务，则返回占用信息
				//2、接收机占用着并且任务不是检测占用任务，返回重新拿任务
				//3、接收机是空闲的，直接下发任务
				String force_type = task.getBusTask().getIs_force();
				if(force_type.equals(Constants.IS_FORCE)) {
					Log.warn("[采集服务]当前获取的任务优先级高，直接顶掉在运行的任务。taskPriority=" + task.getTaskPriority().getMeasurePriority());
				}else {
					if(!statusCtrl.isFreeByEquCode(equCode) && CheckOccupy(task)) {
						//发送处理filter
						sendOccupyData(task);
						//清掉该任务
						try {
							task = ctrlMgr.occupTake();
						} catch (InterruptedException e) {
						}
						continue;
					}else if (!statusCtrl.isFreeByEquCode(equCode) ) {
						sleep(2000);
						continue;
					}
				}
				//#########################################第一种方案，实时音频直接顶掉收测任务-end##############################################
				
				//#########################################第二种方案，没有音频监测-start##############################################				
				//查看是否是前台需要发起的实时访问，是则直接顶掉任务
				//如果是实时音频，频谱，频偏，则顶掉任务，如果是优先级高的收测任务，则继续重新获取任务
//				String force_type = task.getBusTask().getIs_force();
//				if(force_type.equals(Constants.IS_FORCE)) {
//					
//					Log.warn("[采集服务]当前获取的任务优先级高，直接顶掉在运行的任务。taskPriority=" + task.getTaskPriority().getMeasurePriority());
//				}else {
//					//根据类型比较判断优先级
////					String monitorID = task.getBusTask().getMonitor_id() + "";
////					int type = TaskPriorityCompare.compare(task, lastTask);
////					Log.debug("[采集服务]采集任务优先级比较="+ type +"，MonitorID=" + monitorID + ", EquCode=" + equCode + "," + printnLogContent(task, lastTask));
//					//Type=1 表示需要进行顶掉当前运行任务,目前优先级
//					//Type=0 表示需要进行等待当前运行任务完成
////					if (type == Constants.CompareType.TASK_WAIT) {
//						//查看当前状态，空闲继续执行，运行重新获取任务
//					if (!statusCtrl.isFreeByEquCode(equCode)) {
//						continue;
//					}
////					}
//					Log.debug("任务类型，type= " +  task.getData().getType() + ",priority=" + task.getTaskPriority().getMeasurePriority());
//				}
				//#########################################第二种方案，没有音频监测-end##############################################	
			}
			
			//实际获取采集任务，下发采集执行
			try {
				task = ctrlMgr.occupTake();
			} catch (InterruptedException e) {
				break;
			}
			this.lastTask = task;
			//添加唯一 id 等待回收数据寻找
			statusCtrl.addCurrentCollectTask(equCode, task);
			//设置接收机为运行状态 
			statusCtrl.setRunStatusByEquCode(equCode);
			//设置任务占用超时
			startTimer(equCode, task);
			//任务下发
			ctrlMgr.putCollect(task);
		}
	}
	
	private void startTimer(final String equCode, final CollectTask task) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Log.info("[采集服务]当前采集接收机已到占用超时，释放当前采集接收机的占用状态。" + getLogMsg(task) );
				statusCtrl.removeCollectTaskByEquCode(equCode);
				statusCtrl.setFreeStatusByEquCode(equCode);
				CollectTaskContext.getModel().removeTime(getCollectOccupID(task));
			}
			
		}, getOutTime(task));
		
		CollectTaskContext.getModel().add(getCollectOccupID(task), timer);
	}
	
	private String getLogMsg(CollectTask task) {
		StringBuffer log = new StringBuffer();
		log.append("monitorCode=");
		log.append(task.getBusTask().getMonitor_code());
		log.append(",equCode=");
		log.append(task.getAttrInfo().getFirstEquCode());
		return log.toString();
	}
	
	private String getCollectOccupID(CollectTask task) {
		Object id = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY);
		return id == null?null : id.toString();
	}
	
	private int getOutTime(CollectTask task) {
		int equOccupyTime = task.getAttrInfo().getEquTimeOut() * 1000;
		return equOccupyTime;
	}
	/**
	 * 监测任务是否检测接收机占用  占用 true 非占用 false
	 * @param task
	 * @return
	 */
    private boolean CheckOccupy(CollectTask task)
    {
      return task.getAttrInfo().isCheckOccpy();
    }

    private void sendOccupyData(CollectTask task)
    {
	    CollectData date = ReportDataBuilder.buildRecoveryData(task, null, ReportStatus.date_collect_occupy);
	    ARSFToolkit.sendEvent("REPORT_COLLECT_DATA_TOPIC", date);
    }
	
//	private String printnLogContent(CollectTask task, CollectTask lastTask) {
//		StringBuffer buffer = new StringBuffer();
//		
//		buffer.append("Current={" + task.getTaskPriority().toString() + "},");
//		buffer.append("Last={" + lastTask.getTaskPriority().toString() + "}");
//		
//		return buffer.toString();
//	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
