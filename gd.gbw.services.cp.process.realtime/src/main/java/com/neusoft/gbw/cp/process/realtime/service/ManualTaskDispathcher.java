package com.neusoft.gbw.cp.process.realtime.service;

import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.realtime.context.CollectTaskModel;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;
import com.neusoft.np.arsf.net.core.NetEventType;

public class ManualTaskDispathcher {
	
	private CollectTaskModel taskModel = null;
	
	public ManualTaskDispathcher() {
		taskModel = CollectTaskModel.getInstance();
		
	}
	
//	public void taskNoControlDispatch(Map<String, String> syntMap) {
//		//如果是遥控站消息，直接返回处理
//		RecoveryMessageDTO recover = ctreateDTO();
//		String tmpStr = ConverterUtil.objToXml(recover);
//		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
//		String syntStr = NPJsonUtil.jsonValueString(syntMap);
//		Log.warn("[任务管理]手动任务非采集点不进行处理，直接返回下发成功 ,data=" + syntStr);
//		ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), syntStr);
//		sleep();
//		syntDBData();
//	}
	
	/**
	 * 自动运行图设置的任务
	 * @param taskList
	 */
	public void autoTaskDispose(List<Object> taskList) {
		String key = null;
		Object obj = taskList.get(0);
		if(obj instanceof CollectTask) {
			CollectTask task = (CollectTask)obj;
			key = getTaskUniqueKey(task);
		}
		taskModel.addManualAutoTask(taskList, key);
	}
	
	private String getTaskUniqueKey(CollectTask task) {
		String taskName = task.getBusTask().getTask_name();
		String taskType = task.getTaskType().name();
		return taskName + "_" + taskType;
	}

	public void taskDispatch(List<Object> taskList) {
		Object obj = taskList.get(0);
		//1、原逻辑，如果是延时的任务，才进行任务返回
//		TaskDTO dto = getDTO((CollectTask)obj);
//		String isTimelapse = dto.getIsTimelapse();
		//如果是延时的话，直接返回成功
//		if(isTimelapse.equals("1")) 
//			send((CollectTask)obj); 
//		else
//			taskModel.addManualTask(taskList);
		
		//2、新逻辑，接收到任务，直接向前台返回下发成功
		CollectTask task = (CollectTask)taskList.get(0);
		com.neusoft.gbw.cp.core.collect.TaskType taskType = task.getTaskType();
		if(!taskType.equals(com.neusoft.gbw.cp.core.collect.TaskType.qualityDel)){
			send((CollectTask)obj);
		}
		taskModel.addManualTask(taskList);
		
	}
	
	@SuppressWarnings("unchecked")
	private TaskDTO getDTO(CollectTask task) {
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String request = syntMap.get(NetEventProtocol.SYNT_REQUEST);
		TaskDTO dto = null;
		try {
			dto = (TaskDTO)ConverterUtil.xmlToObj(request);
		} catch (Exception e) {
			Log.error("[任务管理]JSON处理格式异常：", e);
		}
		return dto;
	} 
	
	private void send(CollectTask task) {
		RecoveryMessageDTO recover = ctreateDTO();
		String syntStr = getRestStr(recover, task);
		ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), syntStr);
	}
	
	private RecoveryMessageDTO ctreateDTO() {
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessType("0");
		recover.setSuccessDicDesc("成功");
		return recover;
	}
	
	@SuppressWarnings("unchecked")
	private String getRestStr(RecoveryMessageDTO recover, CollectTask task) {
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String tmpStr = ConverterUtil.objToXml(recover);
		TaskDTO dto = getDTO(task);
		//0 添加， 1修改， 2删除
		String operType = dto.getOperationTypeFlg();
		if(operType.equals("0")) 
			syntMap.put(NetEventProtocol.SYNT_TYPE_ID, "sendTaskEvents");
		else if(operType.equals("1"))
			syntMap.put(NetEventProtocol.SYNT_TYPE_ID, "editTaskEvents");
		else if(operType.equals("2"))
			syntMap.put(NetEventProtocol.SYNT_TYPE_ID, "deleteTaskEvents");
		else
			Log.warn("[任务管理]未找到TaskDTO中任务的操作类型 ");
		
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
		return NPJsonUtil.jsonValueString(syntMap);
	}
	
//	private void syntDBData() {
//		String msg = "success";
//		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_TASK_SYNT_DB_TOPIC, msg);
//	}
//	
//	private void sleep() {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//		}
//	}
}
