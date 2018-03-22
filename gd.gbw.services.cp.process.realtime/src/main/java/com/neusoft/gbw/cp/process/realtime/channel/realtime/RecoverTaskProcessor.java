package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;

/**
 * 回收不进行状态计数，不返回前台消息，前台手动回收只进行数据库查询
 * @author jh
 *
 */
public class RecoverTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
//		dispatchData(data);
		return null;
	} 

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
//		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
//		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
//		dispatchData(data);
		return null;
	}
	
//	private void dispatchData(CollectData data) {
//		Object obj = null;
//		ReportStatus status = data.getStatus();
//		switch(status) {
//		case date_collect_success:
//			obj = dispose(data);
//			break;
//		default:
//			Log.warn("[实时处理服务]手动回收任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
//			obj = disposeError(data);
//			break;
//		}
//		if(obj == null) 
//			return;
//		Log.debug("[实时处理服务]手动回收任务处理消息发送至前台，msg=" + obj);
//		sleep(9000);
//		sendTask(obj);
//	}
//
//	private Object dispose(CollectData data) {
//		RecoveryMessageDTO recover = null;
//		int task_id = data.getCollectTask().getBusTask().getTask_id();
//		TaskDTO dto = getDTO(data.getCollectTask());
//		if(dto == null) {
//			Log.warn("[任务管理]未找到任务中的TaskDTO对象 task_id=" + task_id);
//			return null;
//		}
//		
////		String cliendId = dto.getUniqueId();
//		String cliendId = "";
//		String key = task_id + "_" + cliendId;
//		if(!CollectTaskModel.getInstance().containsKey(key)) 
//			return null;
//		
//		Report report = (Report) data.getData().getReportData();
//		Return ret = report.getReportReturn();
//		String value = ret.getValue();
//		if (value.equals("0")) {
//			if(!isSuccess(key)) 
//				return null;
//			recover = createMsg(true, ret);
//		} else 
//			recover = createMsg(false, ret);
//		CollectTaskModel.getInstance().removeTask(key);
//		String syntStr = getRestStr(recover, data);
//		return syntStr;
//	}
//	
//	private Object disposeError(CollectData data) {
//		int task_id = data.getCollectTask().getBusTask().getTask_id();
//		TaskDTO dto = getDTO(data.getCollectTask());
//		if(dto == null) {
//			Log.warn("[任务管理]未找到任务中的TaskDTO对象 task_id=" + task_id);
//			return null;
//		}
////		String cliendId = dto.getUniqueId();
//		String cliendId = "";
//		String key = task_id + "_" + cliendId;
//		if(!CollectTaskModel.getInstance().containsKey(key)) 
//			return null;
//		RecoveryMessageDTO recover = new RecoveryMessageDTO();
//		recover.setSuccessType(ProcessConstants.TASK_SEND_ERROR_TYPE);
//		recover.setSuccessDicDesc(ProcessConstants.TASK_SEND_ERROR_DESC);
//		CollectTaskModel.getInstance().removeTask(key);
//		String syntStr = getRestStr(recover,data);
//		return syntStr;
//	}
//	
//	@SuppressWarnings("unchecked")
//	private String getRestStr(RecoveryMessageDTO recover, CollectData data) {
//		Map<String, String> syntMap = (Map<String, String>) data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
//		String tmpStr = ConverterUtil.objToXml(recover);
//		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
//		return NPJsonUtil.jsonValueString(syntMap);
//	}
//	
//	private RecoveryMessageDTO createMsg(boolean isSuccess, Return ret) {
//		RecoveryMessageDTO recover = new RecoveryMessageDTO();
//		if(isSuccess) {
//			recover.setSuccessType("0");
//			recover.setSuccessDicDesc("成功");
//		}else {
//			recover.setSuccessType(ret.getValue());
//			recover.setSuccessDicDesc(ret.getDesc());
//		}
//		return recover;
//	}
//	
//	@SuppressWarnings("unchecked")
//	private TaskDTO getDTO(CollectTask task) {
//		Object obj = task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
//		if(!(obj instanceof Map))
//			return null;
//		
//		Map<String, String> syntMap = (Map<String, String>) obj;
//		String request = syntMap.get(NetEventProtocol.SYNT_REQUEST);
//		TaskDTO dto = null;
//		try {
//			dto = (TaskDTO)ConverterUtil.xmlToObj(request);
//		} catch (Exception e) {
//			Log.error("[任务管理]JSON处理格式异常：", e);
//		}
//		return dto;
//	}
//	
//	private boolean isSuccess(String key) {
//		ManualTaskInfo info = CollectTaskModel.getInstance().getManualTaskInfo(key);
//		if(info == null)
//			return false;
//		
//		int taskSize = info.getTaskSize();
//		int recoverSize = info.getRecoverSize();
//		if(taskSize != recoverSize) {
//			return false;
//		}
//		return true;
//	}
//	
//	private String getLogMsg(CollectData data) {
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
//		buffer.append("taskID=" + data.getCollectTask().getBusTask().getTask_id() + ",");
//		buffer.append("freq=" + data.getCollectTask().getBusTask().getFreq());
//		return buffer.toString();
//	}
//	
//	private void sleep(long millis) {
//		try {
//			Thread.sleep(millis);;
//		} catch (InterruptedException e) {
//		}
//	}
	
}
