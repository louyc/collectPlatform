package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.vo.manual.RecordRecover;
import com.neusoft.gbw.cp.process.measure.vo.manual.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskRecoverCPMsgDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;
import com.neusoft.np.arsf.net.core.NetEventType;

public class RecordTaskListenHandler extends NMService {
	
	private CollectTaskModel model = CollectTaskModel.getInstance();
	
	@Override
	public void run() {
		Map<String , RecordRecover> taskMap = null;
		boolean recoverStatus = false;
		while(isThreadRunning()) {
			taskMap = model.getRecordRecoverMap();
			if(taskMap.isEmpty()) {
				sleep(1000);
				continue;
			}
			for(RecordRecover info : taskMap.values()) {
				if(info == null)
					continue;
				CollectTask data = info.getData();
				//确认是否回收完成
				if(compareTime(info.getTime())) {
					com.neusoft.gbw.cp.core.collect.TaskType type = data.getTaskType();
					if(type== null || !type.equals("system")) //不是系统任务，暂时不进行系统时间更新
						return;
					Log.debug("本批次录音文件接收超时，taskId=" + data.getBusTask().getTask_id());
					sendMsg(data, ProcessConstants.TASK_RECOVER_TIME_OUT, ProcessConstants.LeakageReason.DATE_COLLECT_TIME_OUT); 
					model.removeRecordTask(getKey(data));
					break;
				}
				
				int taskSize = info.getRecordSize();
				int recoverSize = info.getRecoverSize();
				if(recoverSize == taskSize) {
					//向web发送消息
					recoverStatus = info.isRecordStatus();
					Log.debug("本批次录音文件接收成功，taskId=" + data.getBusTask().getTask_id()
							+"recoverStatus=="+recoverStatus);
					if(recoverStatus)
						sendMsg(data, ProcessConstants.TASK_RECOVER_SUCCESS, ProcessConstants.LeakageReason.DATE_COLLECT_SUCCESS); 
					else sendMsg(data, ProcessConstants.TASK_RECOVER_FAUIL, ProcessConstants.LeakageReason.DATE_COLLECT_FAUIL); 
					model.removeRecordTask(getKey(data));
					break;
				}
			}
			
//			sleep(3000);
		}
	}
	
	private Object sendMsg(CollectTask task, String status, String msg) {
		
//		sendTask(obj);
		
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessType(status);
		recover.setSuccessDicDesc(msg);
		Object syntStr = getRestStr(recover, task);
		Log.debug("本批次录音文件接收完成,向前台发送回收消息，syntStr=" + syntStr);
		sendTask(syntStr);
//		ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), syntStr);
		return syntStr;
	}
	
	public void sendTask(Object syntObj) {
		if (syntObj instanceof JMSDTO) {
			ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, syntObj);
		} else if (syntObj instanceof String) {
			ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), (String)syntObj);
		} else {
			Log.warn("实时任务处理类型不存在，" + syntObj);
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getRestStr(RecoveryMessageDTO recover, CollectTask task) {
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String tmpStr = ConverterUtil.objToXml(recover);
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
		return NPJsonUtil.jsonValueString(syntMap);
	}
	
	private boolean compareTime(long time) {
		boolean flag = false;
		long systemTime = System.currentTimeMillis();
		if(systemTime - time > ProcessConstants.MANUAL_RECOVER_TASK_TIME_OUT) 
			flag = true;
		return flag;
		
	}
	
	private String getKey(CollectTask data) {
		long taskId = data.getBusTask().getTask_id();
		String type = data.getTaskType().name();
		return taskId + type;
	}
	
	public void updateRecoverCount(CollectData data, boolean status) {
		String key = data.getCollectTask().getBusTask().getTask_id() + "";
		CollectTaskModel.getInstance().updateRecoverCount(key, status);
	}
	
	public void updateRecoverTime(CollectData data, TaskType type) {
		StoreInfo info = new StoreInfo();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("time_id", data.getCollectTask().getBusTask().getTime_id());
		dataMap.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		dataMap.put("task_type", type);
		dataMap.put("recover_time", data.getCollectTask().getBusTask().getRecover_end());//回收时间从收测单元结束时间属性中提取
		info.setDataMap(dataMap);
		info.setLabel(ProcessConstants.StoreTopic.UPDATE_MANUAL_TASK_RECOVER_TIME);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);	
	}
	
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
