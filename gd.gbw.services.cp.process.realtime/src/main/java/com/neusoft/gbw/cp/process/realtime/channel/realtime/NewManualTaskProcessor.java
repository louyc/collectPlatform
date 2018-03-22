package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.cp.process.realtime.vo.ManualTaskInfo;
import com.neusoft.gbw.cp.process.realtime.vo.ManualTaskStore;
import com.neusoft.gbw.cp.process.realtime.vo.TaskType;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskAppDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskSetCPMsgDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class NewManualTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
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
		com.neusoft.gbw.cp.core.collect.TaskType taskType = data.getCollectTask().getTaskType();
		switch(status) {
		case date_collect_success:
			//区分自动收测设置和手动设置，
			if(taskType.equals(com.neusoft.gbw.cp.core.collect.TaskType.system))
				disposeAuto(data);
			else
				obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]手动设置任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			if(taskType.equals(com.neusoft.gbw.cp.core.collect.TaskType.system))
				disposeAutoError(data);
			else
				obj = disposeError(data);
			break;
		}
		if(obj == null) 
			return;
		Log.debug("[实时处理服务]手动设置任务处理消息发送至前台，msg=" + obj);
		sendTask(obj);
		syntDBData();
	}
	
	private void disposeAuto(CollectData data) {
		String key = null;
		//获取通信类型,根据通信类型，获取计数对象中的key,手动任务key为task_id,自动任务key为task_name
		key = getTaskUniqueKey(data.getCollectTask());
		
		if(!CollectTaskModel.getInstance().containsKey(key)) 
			return;
		Report report = (Report) data.getData().getReportData();
		Return ret = report.getReportReturn();
		String value = ret.getValue();
		String type = ret.getType();
		//如果操作正常，或者在删除的时候未找到该任务都按照正常处理
		if(value.equals("0") || value.equals("102") || value.equals("101")) {
			//进行任务回收时间戳维护
			if(!value.equals("101"))
				operRecoverTaskInfo(data, type);
			//判断校验此任务是否全部设置完成,无论失败或者成功都算设置完成
			if(!isSuccess(key)) 
				return;
			sendDelTask(CollectTaskModel.getInstance().getManualTaskInfo(key).getDelStore());
			updateTaskSetStatus(data, 1);
		}else 
			updateTaskSetStatus(data, 0);//修改一个频率设置任务状态，0失败，1成功
		
		CollectTaskModel.getInstance().removeTask(key);
	}

	private Object dispose(CollectData data) {
		long task_id = data.getCollectTask().getBusTask().getTask_id();
		String freq = data.getCollectTask().getBusTask().getFreq();
		com.neusoft.gbw.cp.core.collect.TaskType taskType = data.getCollectTask().getTaskType();
		if(!taskType.equals(com.neusoft.gbw.cp.core.collect.TaskType.qualityDel)){  //非自动质量任务删除
			//获取DTO
			TaskAppDTO dto = getDTO(data.getCollectTask());
			if(dto == null) {
				Log.warn("[任务管理]未找到任务中的TaskDTO对象,task_id=" + task_id);
				return null;
			}
			//0 添加， 1修改， 2删除  由于修改走添加和删除操作。
			String operType = dto.getOperationTypeFlg();
			
			String key = null;
			//获取通信类型,根据通信类型，获取计数对象中的key,手动任务key为task_id,自动任务key为task_name
			String transType = getTransType(data.getCollectTask());
			switch(transType) {
			case ProcessConstants.REST_TYPE:
	//			String cliendId = dto.getUniqueId();
				String cliendId = "";
				key = task_id + "_" + cliendId;
				break;
			case ProcessConstants.JMS_TYPE:
				key = getTaskUniqueKey(data.getCollectTask());
				break;
			}
			
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
					operRecoverTaskInfo(data, type);
				//判断校验此任务是否全部设置完成,无论失败或者成功都算设置完成
				if(!isSuccess(key)) 
					return null;
				//如果任务下发成功后，任务操作为删除操作，则将数据库中的任务删除
				if(!operType.equals("0")) {
					//创建删除对象
					sendDelTask(CollectTaskModel.getInstance().getManualTaskInfo(key).getDelStore());
				}
				updateTaskSetStatus(data, 1);
			
			}else {
				updateTaskSetStatus(data, 0);//修改一个频率设置任务状态，0失败，1成功
			}
	//		//删除操作失败的任务，不进行还原，做一个定时机制进行删除操作
	//		//如果是错误的操作返回，如果是删除操作的话，就将状态回置成任务启用，并且回复前台下发失败
	//		else {
	//			if(operType.equals("2")) {
	//				sendReSetTask(store);
	//			}
	//		}
			TaskDTO taskDto = (TaskDTO) dto.getMap().values().iterator().next().get(0);
			JMSDTO jms = createMsg(data.getCollectTask(), taskDto, freq, ret.getType(), ret.getDesc(), data.getCollectTask().getData().getType().name());
			CollectTaskModel.getInstance().removeTask(key);
			return jms;
		}else{  //三满任务  自动删除
			String cliendId = "";
			String key = task_id + "_" + cliendId;
			if(!CollectTaskModel.getInstance().containsKey(key)) 
				return null;
			Report report = (Report) data.getData().getReportData();
			Return ret = report.getReportReturn();
			String value = ret.getValue();
			String type = ret.getType();
			//如果操作正常，或者在删除的时候未找到该任务都按照正常处理
			if(value.equals("0") || value.equals("101") || value.equals("102")) {
				//进行任务回收时间戳维护
				operRecoverTaskInfo(data, type);
				//判断校验此任务是否全部设置完成,无论失败或者成功都算设置完成
				if(!isSuccess(key)) 
					return null;
				//创建删除对象
				ManualTaskStore mts = new ManualTaskStore();
				mts.setTask_id(data.getCollectTask().getBusTask().getTask_id());
				mts.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
//				if(data.getCollectTask().getBusTask().getTask_type_id()!=6){
//					mts.setFreq_id(Integer.parseInt(data.getCollectTask().getBusTask().getFreq()));
//				}
				mts.setTask_type(data.getCollectTask().getTaskType().toString());
				sendDelTask(mts);
			}
			CollectTaskModel.getInstance().removeTask(key);
			return null;
		}
	}
	
	private void operRecoverTaskInfo(CollectData data, String task_type) {
		
		StoreInfo info = new StoreInfo();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", data.getCollectTask().getBusTask().getTask_id());
		dataMap.put("freq_id", data.getCollectTask().getBusTask().getTaskfreq_id());
		dataMap.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		dataMap.put("recover_time", data.getCollectTask().getBusTask().getUnit_begin());//回收时间从收测单元开始时间属性中提取
		dataMap.put("set_status", 1); //设置状态，1成功，0失败
		dataMap.put("task_type", getTaskType(data.getCollectTask().getData().getType().name()));
		dataMap.put("task_unique_id", getTaskID(data.getCollectTask()));
		ProtocolType ptype = data.getCollectTask().getData().getType();
		switch(ptype) {
		case TaskDelete:
			info.setLabel(ProcessConstants.StoreTopic.DELETE_MEASURE_TASK_RECOVER_TIME);
			break;
		default:
			info.setLabel(ProcessConstants.StoreTopic.INSERT_MEASURE_TASK_RECOVER_TIME);
			break;
		}
		info.setDataMap(dataMap);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	/**
	 * 唯一ID确认
	 * taskName+monitorCode+freq+taskType+taskMode + schedule信息
	 * @param task
	 * @param operNum
	 * @return
	 */
	public static String getTaskID(CollectTask task) {
		String taskId = task.getExpandObject(ExpandConstants.TASK_UNIQUE_ID).toString();
		return taskId;
	}
	
	private Object disposeError(CollectData data) {
		long task_id = data.getCollectTask().getBusTask().getTask_id();
		String freq = data.getCollectTask().getBusTask().getFreq();
		TaskAppDTO dto = getDTO(data.getCollectTask());
		if(dto == null) {
			Log.warn("[任务管理]未找到任务中的TaskDTO对象,task_id=" + task_id);
			return null;
		}
//		//0 添加， 1修改， 2删除  由于修改走添加和删除操作。
//		String operType = dto.getOperationTypeFlg();
//		if(operType.equals("2")) {
//			ManualTaskStore store = new ManualTaskStore();
//			store.setTask_id(task_id);
//			sendReSetTask(store);
//		}
		//修改任务设置状态
		updateTaskSetStatus(data, 0);
		String key = null;
		//获取通信类型,根据通信类型，获取计数对象中的key,手动任务key为task_id,自动任务key为task_name
		String transType = getTransType(data.getCollectTask());
		switch(transType) {
		case ProcessConstants.REST_TYPE:
//			String cliendId = dto.getUniqueId();
			String cliendId = "";
			key = task_id + "_" + cliendId;
			break;
		case ProcessConstants.JMS_TYPE:
			key = getTaskUniqueKey(data.getCollectTask());
			break;
		}
		
		ProtocolData prot = data.getCollectTask().getData();
		ProtocolType type = prot.getType().name().equals(ProtocolType.TaskDelete.name()) ? prot.getOriType() : prot.getType();
		//判断校验此任务是否全部设置完成,无论失败或者成功都算设置完成
		if(!isSuccess(key)) 
			return null;
		TaskDTO taskDto = (TaskDTO) dto.getMap().values().iterator().next().get(0);
		JMSDTO jms = createMsg(data.getCollectTask(), taskDto, freq,ProcessConstants.TASK_SEND_ERROR_TYPE, ProcessConstants.TASK_SEND_ERROR_DESC, type.name());
		CollectTaskModel.getInstance().removeTask(key);
		return jms;
	}
	
	private void disposeAutoError(CollectData data) {
		String key = null;
		key = getTaskUniqueKey(data.getCollectTask());
		//判断校验此任务是否全部设置完成,无论失败或者成功都算设置完成
		if(!isSuccess(key)) 
			return;
		CollectTaskModel.getInstance().removeTask(key);
	}
	
	private void updateTaskSetStatus(CollectData data, int status) {
		ManualTaskStore store = new ManualTaskStore();
		long task_id = data.getCollectTask().getBusTask().getTask_id();
		int freq_id = data.getCollectTask().getBusTask().getTaskfreq_id();
		long monitor_id = data.getCollectTask().getBusTask().getMonitor_id();
		store.setFreq_id(freq_id);
		store.setTask_id(task_id);
		store.setMonitor_id(monitor_id);
		store.setSet_status(status);
		store.setTask_type(getTaskType(data.getCollectTask().getData().getType().name()));
		//修改任务回收表中的任务设置状态
		sendUpdateSetTaskStatus(store);
		//修改任务总表中的任务状态
		sendUpdateBigTaskSetStatus(store);
	}
	
	private String getTaskType(String task_type) {
		String taskType = null;
		if(task_type.startsWith(TaskType.OffsetTask.name()))
			taskType = TaskType.OffsetTask.name();
		else if(task_type.startsWith(TaskType.QualityTask.name()))
			taskType = TaskType.QualityTask.name();
		else if(task_type.startsWith(TaskType.SpectrumTask.name()))
			taskType = TaskType.SpectrumTask.name();
		else if(task_type.startsWith(TaskType.StreamTask.name()))
			taskType = TaskType.StreamTask.name();
		return taskType;
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
	
	@SuppressWarnings("unchecked")
	private TaskAppDTO getDTO(CollectTask task) {
		TaskAppDTO dto = null;
		Object obj = task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		if(obj instanceof Map) {
			Map<String, String> syntMap = (Map<String, String>) obj;
			String request = syntMap.get(NetEventProtocol.SYNT_REQUEST);
			try {
				dto = (TaskAppDTO)ConverterUtil.xmlToObj(request);
			} catch (Exception e) {
				Log.error("[任务管理]JSON处理格式异常：", e);
			}
		}else if(obj instanceof TaskAppDTO)
			dto = (TaskAppDTO)obj;
		return dto;
	}
	
	private String getTransType(CollectTask task) {
		String type = null;
		Object obj = task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		if(obj instanceof Map) {
			type = ProcessConstants.REST_TYPE;
		}else if(obj instanceof TaskAppDTO)
			type = ProcessConstants.JMS_TYPE;
		return type;
	}
	
	private JMSDTO createMsg(CollectTask task, TaskDTO dto, String freq, String type, String desc, String task_type) {
		JMSDTO jms = new JMSDTO();
		TaskSetCPMsgDTO set = new TaskSetCPMsgDTO();
		set.setReturnDesc(getMsgDesc(task, freq, desc, task_type));
		set.setReturnType(type);
		set.setTaskDto(dto);
		jms.setObj((Serializable) set);
		jms.setTypeId(GBWMsgConstant.C_JMS_REAL_TASK_SET_RESPONSE_MSG);
		return jms;
	}
	
	private void syntDBData() {
		String msg = "success";
		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_TASK_SYNT_DB_TOPIC, msg);
	}
	
	private String getMsgDesc(CollectTask task, String freq, String desc, String task_type) {
		String task_name = task.getBusTask().getTask_name();
		String monitor_code = task.getBusTask().getMonitor_code();
		String taskdesc = "任务状态：" + desc + "，任务名称：" + task_name + "，站点：" + monitor_code;
		return taskdesc;
	}
	
	private String getTaskUniqueKey(CollectTask task) {
		String taskName = task.getBusTask().getTask_name();
		String taskType = task.getTaskType().name();
		return taskName + "_" + taskType;
	}
	
////	private void sleep() {
////		try {
////			Thread.sleep(2000);
////		} catch (InterruptedException e) {
////		}
////	}
	
//	private String getTaskTypeName(String task_type) {
//		String taskTypeName = null;
//		if(task_type.startsWith(TaskType.OffsetTask.name()))
//			taskTypeName = "频偏任务";
//		else if(task_type.startsWith(TaskType.QualityTask.name()))
//			taskTypeName = " 指标任务";
//		else if(task_type.startsWith(TaskType.SpectrumTask.name()))
//			taskTypeName = "频谱任务";
//		else if(task_type.startsWith(TaskType.StreamTask.name()))
//			taskTypeName = "录音任务";
//		else
//			taskTypeName = "删除任务";
//		return taskTypeName;
//	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code() + ",");
		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
		return buffer.toString();
	}
	
}
