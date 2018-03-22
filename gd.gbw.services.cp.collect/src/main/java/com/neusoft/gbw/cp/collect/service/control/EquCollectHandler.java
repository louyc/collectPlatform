package com.neusoft.gbw.cp.collect.service.control;

import java.util.Timer;
import java.util.TimerTask;

import com.neusoft.gbw.cp.collect.constants.CollectVariable;
import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.model.CollectTaskModel;
import com.neusoft.gbw.cp.collect.service.build.QueryDataBuilder;
import com.neusoft.gbw.cp.collect.service.build.ReportDataBuilder;
import com.neusoft.gbw.cp.collect.service.transfer.ICollect;
import com.neusoft.gbw.cp.collect.vo.CollectTimeOutInfo;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.CollectTaskType;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.core.collect.TransferType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.load.data.build.domain.control.EquStatusControlMode;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class EquCollectHandler extends NMService {
	
	private EquCollectCotrolMgr ctrlMgr = null;
	
	public EquCollectHandler(EquCollectCotrolMgr ctrlMgr) {
		this.ctrlMgr = ctrlMgr;
	}
	
	@Override
	public void run() {
		CollectTask task = null;
		while(isThreadRunning()) {
			try {
				task = ctrlMgr.takeCollect();
			} catch (InterruptedException e) {
				Log.error("[采集服务]接收任务失败", e);
				break;
			}
			collect(task);
		}
	}
	
	private String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
//		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
//		buffer.append("TaskFreq=" + task.getBusTask().getFreq() + ",");
		if(null!=task.getBusTask()){
			buffer.append("MonitorID=" + task.getBusTask().getMonitor_id() + ",");
			buffer.append("MonitorCode=" + task.getBusTask().getMonitor_code() + ",");
			buffer.append("taskType=" + task.getData().getType() + "");
		}
		
		return buffer.toString();
	}
	
	private void collect(CollectTask task) {
		//选择采集方式Socket Servlet
		//下发采集任务
		//采集处理方式和原有方式一致
		String protocolXML = null;
		Log.debug("任务下发 length"+task.getBusTask().getRecordLength()+"任务下发 taskId"+task.getBusTask().getTask_id()+
				"  "+task.getBusTask().getMonitor_id()+"   "+task.getBusTask().getFreq());
		try {
			//获取版本
			int version = task.getData().getProVersion();
			switch (version) {
			case 8:
				protocolXML = QueryDataBuilder.buildQueryDataV8(task);
				break;
			case 7:
				protocolXML = QueryDataBuilder.buildQueryDataV7(task);
				break;
			case 6:
				protocolXML = QueryDataBuilder.buildQueryDataV6(task);
				break;
			case 5:
				protocolXML = QueryDataBuilder.buildQueryDataV5(task);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.error("转换采集协议对象失败，" + protocolXML, e);
			builderAndPulisherData(task, ReportStatus.collect_task_analytical_failure,EventServiceTopic.REPORT_COLLECT_DATA_TOPIC);
		}
		//获取通信方式
		TransferType type = task.getAttrInfo().getTransferType();
		//获取通信
		ICollect collect = CollectTaskContext.getModel().getCollectProcessor(type);
		if (collect == null) {
			Log.warn("出现不支持的下行采集方式 " + type.name());
			return;
		}
		
		//校验通讯信息
		if(!collect.checkInfo(task.getAttrInfo())) {
			Log.info("验证通信基础信息失败：" + getLogContent(task) + " 变更采集状态：" + ReportStatus.transfer_base_info_validate_failure);
			builderAndPulisherData(task,ReportStatus.transfer_base_info_validate_failure,EventServiceTopic.REPORT_COLLECT_DATA_TOPIC);
			return;
		}
		// 发送数据
		if(!collect.collect(task.getAttrInfo(), protocolXML)) {
			Log.info("发送采集任务失败： " + getLogContent(task));
			if(ProtocolType.TaskDelete!=task.getData().getType()){  //  后边要根据任务协议类型做修改    待改   删除类发送失败不再重复发送
				builderAndPulisherData(task,ReportStatus.collect_task_send_failure,EventServiceTopic.REPORT_COLLECT_DATA_TOPIC);
			}
			return;
		}
		// 构建采集超时对象
		syncModel(task);
	}
	
	/**
	 * 同步任务对象
	 * @param taskInfo
	 */
	private void syncModel(CollectTask taskInfo) {
		String msgId = ((Query)taskInfo.getData().getQuery()).getMsgID() + "";
		//构建采集超时对象
		CollectTimeOutInfo ctuInfo = new CollectTimeOutInfo();
		ctuInfo.setCollectTaskID(msgId);
		ctuInfo.setTaskInfo(taskInfo);;
		ctuInfo.setTimer(createTimer(taskInfo));
		//将采集对象封装
		CollectTaskModel.getModel().add(ctuInfo);
	}
	
	/**
	 * 创建一个定时器对象
	 * @return
	 */
	private Timer createTimer(final CollectTask task) {
		Object wait_time = task.getAttrInfo().getTransferTimeOut();
		if(wait_time == null) {
			Log.warn("获取采集任务超时时间为空" + getLogContent(task));
			wait_time = CollectVariable.COLLECT_TIME_OUT;
		}
		Log.debug("超时时间lyc设置为：：**********"+wait_time);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				String msgId = ((Query)task.getData().getQuery()).getMsgID() + "";
				CollectTimeOutInfo info = CollectTaskModel.getModel().get(msgId);
				if(info == null){
					return;
				}
				Log.info("采集任务到期   消息ID:" + msgId + "," + getLogContent(info.getTaskInfo())
						+"   "+task.getBusTask().getTask_id()+"   "+task.getBusTask().getMonitor_code()+
						"   "+task.getBusTask().getFreq()+"   "+task.getBusTask().getTask_type_id());
				//将采集对象设置超时
				info.setTimeOut(true);
				timeOutDispose(info.getTaskInfo());
				//清除内存
				info.setTimer(null);
				info.setTaskInfo(null);
				info = null;
			}
		}, Integer.parseInt(wait_time.toString())*1000);
		return timer;
	}
	
	private void timeOutDispose(CollectTask task) {
		freeEquCode(task);
		CollectData data = ReportDataBuilder.buildTaskFailureData(task,ReportStatus.date_collect_time_out);
		//调取上报总线接口
		ARSFToolkit.sendEvent(EventServiceTopic.REPORT_COLLECT_DATA_TOPIC, data);
	}
	
	private void freeEquCode(CollectTask task) {
		long monitorId = task.getBusTask().getMonitor_id();
		String equCode = task.getAttrInfo().getFirstEquCode()+task.getBusTask().getMonitor_id();
		//取消接收机占用状态，设置为空闲
		if(null!=EquStatusControlMode.getInstance().getStationControl(monitorId)){
			EquStatusControlMode.getInstance().getStationControl(monitorId).setFreeStatusByEquCode(equCode);
			EquStatusControlMode.getInstance().getStationControl(monitorId).removeCollectTaskByEquCode(equCode);
		}
		//如果是占用消息，则取消占用计时器
		CollectTaskType type = task.getAttrInfo().getColTaskType();
		if(!type.equals(CollectTaskType.occup))
			return;
		if(null!=getCollectOccupID(task) && null!=CollectTaskContext.getModel().getTimer(getCollectOccupID(task))){
			CollectTaskContext.getModel().getTimer(getCollectOccupID(task)).cancel();
			CollectTaskContext.getModel().removeTime(getCollectOccupID(task));
		}
	}
	
	private String getCollectOccupID(CollectTask task) {
		Object id = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY);
		return id == null ? null : id.toString();
	}
	
	/**
	 * 构建并上报采集数据
	 * @param info
	 */
	private void builderAndPulisherData(CollectTask taskInfo,ReportStatus status,String topic) {
		CollectData info = ReportDataBuilder.buildTaskFailureData(taskInfo, status);
		//调取上报总线接口
		ARSFToolkit.sendEvent(topic, info);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
