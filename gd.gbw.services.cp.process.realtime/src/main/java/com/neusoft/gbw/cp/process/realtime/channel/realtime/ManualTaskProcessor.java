package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.ManualTaskInfo;
import com.neusoft.gbw.cp.process.realtime.vo.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

/**
 * 此类操作为原来关于任务设置采用同步接收的机制
 *
 */
public class ManualTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	} 

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}
	
	private void dispatchData(CollectData data) {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]手动设置任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
		if(obj == null) 
			return;
		Log.debug("[实时处理服务]手动设置任务处理消息发送至前台，msg=" + obj);
		sendTask(obj);
		sleep();
		syntDBData();
	}

	private Object dispose(CollectData data) {
		RecoveryMessageDTO recover = null;
		long task_id = data.getCollectTask().getBusTask().getTask_id();
		TaskDTO dto = getDTO(data.getCollectTask());
		if(dto == null) {
			Log.warn("[任务管理]未找到任务中的TaskDTO对象,task_id=" + task_id);
			return null;
		}
		//0 添加， 1修改， 2删除  由于修改走添加和删除操作。
		String operType = dto.getOperationTypeFlg();
//		String cliendId = dto.getUniqueId();
		String cliendId = "";
		String key = task_id + "_" + cliendId;
		if(!CollectTaskModel.getInstance().containsKey(key)) 
			return null;
		Report report = (Report) data.getData().getReportData();
		Return ret = report.getReportReturn();
		String value = ret.getValue();
		String type = ret.getType();
		//如果操作正常，或者在删除的时候未找到该任务都按照正常处理
		if(value.equals("0") || value.equals("102") || value.equals("101")) {
			//进行任务回收时间戳维护
			if(!value.equals("101"))
				updateRecoverTaskInfo(data, type);
			if(!isSuccess(key)) 
				return null;
			recover = createMsg(true, ret);
		}else 
			recover = createMsg(false, ret);
		
		CollectTaskModel.getInstance().removeTask(key);
		String syntStr = getRestStr(recover,data,operType);
		return syntStr;
	}
	
	private void updateRecoverTaskInfo(CollectData data, String task_type) {
		
		StoreInfo info = new StoreInfo();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("freq_id", data.getCollectTask().getBusTask().getTaskfreq_id());
		dataMap.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		dataMap.put("recover_time", data.getCollectTask().getBusTask().getUnit_begin());//回收时间从收测单元开始时间属性中提取
		ProtocolType ptype = data.getCollectTask().getData().getType();
		switch(ptype) {
		case TaskDelete:
			info.setLabel(ProcessConstants.StoreTopic.DELETE_MEASURE_TASK_RECOVER_TIME);
			break;
		default:
			info.setLabel(ProcessConstants.StoreTopic.INSERT_MEASURE_TASK_RECOVER_TIME);
			if(task_type.startsWith(TaskType.OffsetTask.name()))
				dataMap.put("task_type", TaskType.OffsetTask);
			else if(task_type.startsWith(TaskType.QualityTask.name()))
				dataMap.put("task_type", TaskType.QualityTask);
			else if(task_type.startsWith(TaskType.SpectrumTask.name()))
				dataMap.put("task_type", TaskType.SpectrumTask);
			else if(task_type.startsWith(TaskType.StreamTask.name()))
				dataMap.put("task_type", TaskType.StreamTask);
			break;
		}
		info.setDataMap(dataMap);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	private Object disposeError(CollectData data) {
		long task_id = data.getCollectTask().getBusTask().getTask_id();
		TaskDTO dto = getDTO(data.getCollectTask());
		if(dto == null) {
			Log.warn("[任务管理]未找到任务中的TaskDTO对象 task_id=" + task_id);
			return null;
		}
		//0 添加， 1修改， 2删除  由于修改走添加和删除操作。
		String operType = dto.getOperationTypeFlg();
//		String cliendId = dto.getUniqueId();
		String cliendId = "";
		String key = task_id + "_" + cliendId;
		if(!CollectTaskModel.getInstance().containsKey(key)) 
			return null;
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessType(ProcessConstants.TASK_SEND_ERROR_TYPE);
		recover.setSuccessDicDesc(ProcessConstants.TASK_SEND_ERROR_DESC);
		CollectTaskModel.getInstance().removeTask(key);
		String syntStr = getRestStr(recover,data,operType);
		return syntStr;
	}
	
	@SuppressWarnings("unchecked")
	private String getRestStr(RecoveryMessageDTO recover, CollectData data,String operType) {
		Map<String, String> syntMap = (Map<String, String>) data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String tmpStr = ConverterUtil.objToXml(recover);
		//0 添加， 1修改， 2删除  由于修改走添加和删除操作,所以要在发送rest消息时将类型ID置回。
		if("0".equals(operType)) 
			syntMap.put(NetEventProtocol.SYNT_TYPE_ID, "sendTaskEvents");
		else if("1".equals(operType))
			syntMap.put(NetEventProtocol.SYNT_TYPE_ID, "editTaskEvents");
		else if("2".equals(operType))
			syntMap.put(NetEventProtocol.SYNT_TYPE_ID, "deleteTaskEvents");
		else
			Log.warn("[任务下发]未找到TaskDTO中任务的操作类型 ");
		
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
		return NPJsonUtil.jsonValueString(syntMap);
	}
	
	private boolean isSuccess(String key) {
		ManualTaskInfo info = CollectTaskModel.getInstance().getManualTaskInfo(key);
		if(info == null)
			return false;
		
		int taskSize = info.getTaskSize();
		int recoverSize = info.getRecoverSize();
		if(taskSize != recoverSize) {
			return false;
		}
		return true;
		
	}
	
	private RecoveryMessageDTO createMsg(boolean isSuccess, Return ret) {
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		if(isSuccess) {
			recover.setSuccessType("0");
			recover.setSuccessDicDesc("成功");
		}else {
			recover.setSuccessType(ret.getValue());
			recover.setSuccessDicDesc(ret.getDesc());
		}
		return recover;
	}
	
	@SuppressWarnings("unchecked")
	private TaskDTO getDTO(CollectTask task) {
		Object obj = task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		if(!(obj instanceof Map))
			return null;
		
		Map<String, String> syntMap = (Map<String, String>) obj;
		String request = syntMap.get(NetEventProtocol.SYNT_REQUEST);
		TaskDTO dto = null;
		try {
			dto = (TaskDTO)ConverterUtil.xmlToObj(request);
		} catch (Exception e) {
			Log.error("[任务管理]JSON处理格式异常：", e);
		}
		return dto;
	}
	
	private void syntDBData() {
		String msg = "success";
		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_TASK_SYNT_DB_TOPIC, msg);
	}
	
	private void sleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code() + ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
	
}
