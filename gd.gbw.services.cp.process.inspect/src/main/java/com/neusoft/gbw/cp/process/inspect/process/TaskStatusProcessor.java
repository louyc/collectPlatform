package com.neusoft.gbw.cp.process.inspect.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.exception.InspectDisposeException;
import com.neusoft.gbw.cp.process.inspect.vo.InspectResultStore;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskStatusProcessor extends SendTaskProcessor implements ITaskProcess{
	
	private InspectTaskContext context = InspectTaskContext.getInstance();

	@Override
	public void disposeV8(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV8Data(data);
			break;
		default:
			Log.warn("[巡检服务]设置任务状态处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]设置任务状态处理消息发送至前台，msg=" + inspectList.toString());
		disposeData(inspectList);
//		storeInfo(inspectList);
//		updateInspectStatus(inspectList);
//		sendTask(inspectList);
		
	}

	@Override
	public void disposeV7(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV7Data(data);
			break;
		default:
			Log.warn("[巡检服务]设置任务状态处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]设置任务状态处理消息发送至前台，msg=" + inspectList.toString());
		disposeData(inspectList);
//		storeInfo(inspectList);
//		updateInspectStatus(inspectList);
//		sendTask(inspectList);
	}

	@Override
	public void disposeV6(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV6Data(data);
			break;
		default:
			Log.warn("[巡检服务]设置任务状态处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]设置任务状态处理消息发送至前台，msg=" + inspectList.toString());
		disposeData(inspectList);
//		storeInfo(inspectList);
//		updateInspectStatus(inspectList);
//		sendTask(inspectList);
	}

	@Override
	public void disposeV5(CollectData data) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			inspectList = disposeV5Data(data);
			break;
		default:
			Log.warn("[巡检服务]设置任务状态处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			inspectList = disposeError(data);
			break;
		}
//		Log.debug("[巡检服务]设置任务状态处理消息发送至前台，msg=" + inspectList.toString());
		disposeData(inspectList);
//		storeInfo(inspectList);
//		updateInspectStatus(inspectList);
//		sendTask(inspectList);
	}
	
	public void disposeTask(MoniInspectResultDTO inspectDTO, String desc,String typeTask) throws InspectDisposeException {
		//校验是否连通性超时，如果通信超时则不进行处理
//		if(!isNetSuccess(data))
//			return;
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		InspectResultStore taskExsitStore = getResultDto(inspectDTO, typeTask);
		if(typeTask.equals("streamVadioState")){
			if("".equals(desc)){
				taskExsitStore.setInspec_result(3);
				taskExsitStore.setInspec_message("无录音文件");
			}else{
				taskExsitStore.setInspec_result(0);
				taskExsitStore.setInspec_message(desc);
			}
		}else{
			if(desc.equals("失败")){
				taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
			}else if(desc.equals("该站点未设置任务")||desc.equals("")){
				taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_NULL);
			}else{
				taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
			}
			taskExsitStore.setInspec_finish_status(1);
			taskExsitStore.setInspec_message(desc);
		}
		inspectList.add(taskExsitStore);
//		Log.debug("[巡检服务]设置任务状态处理消息发送至前台，msg=" + inspectList.toString());
		disposeData(inspectList);
//		storeInfo(inspectList);
//		updateInspectStatus(inspectList);
//		sendTask(inspectList);
	}
	
	private List<InspectResultStore> disposeError(CollectData data) {
		String errorDesc = "巡检任务状态查询执行过程中出现异常";
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		int taskSize = context.getInspectTaskSize(inspectDTO.getMonitorId());
		InspectResultStore taskExsitStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE);
		taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		
		taskExsitStore.setInspec_message(getTaskDesc(taskSize, taskSize, errorDesc));
		InspectResultStore taskRunStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE);
		taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		taskRunStore.setInspec_message(getTaskDesc(taskSize, taskSize, errorDesc));
		inspectList.add(taskExsitStore);
		inspectList.add(taskRunStore);
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV8Data(CollectData data) {
		int noExistCount = 0;
		int noRunCount = 0;
		String existDesc = "";
		String runDesc = "";
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		int taskSize = context.getInspectTaskSize(inspectDTO.getMonitorId());
		Map<String, String> taskMap = context.getTaskMap(inspectDTO.getMonitorId());
		InspectResultStore taskExsitStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE);
		InspectResultStore taskRunStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE);
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.other.TaskStatusQueryR taskQuery = (com.neusoft.gbw.cp.conver.v8.protocol.vo.other.TaskStatusQueryR) report.getReport();
		for(com.neusoft.gbw.cp.conver.v8.protocol.vo.other.TaskStatus status : taskQuery.getTaskStatuss()) {
			String statusDesc = status.getStatus();
			String taskId = status.getTaskID();
//			String desc = status.getDesc();
			if(statusDesc.equals("-1")) {//任务不存在
				noExistCount++;
				existDesc += noExistCount + "." + taskMap.get(taskId) + ",任务状态:站点下该任务可能不存在,";
			}
			
			if(statusDesc.equals("3")) {
				noRunCount++;
				runDesc += noRunCount + "." +taskMap.get(taskId) + ",任务状态:站点下该任务可能从未执行,";
			}
		}
		if(noExistCount != 0)
			taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		
		if(noRunCount != 0)
			taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		taskExsitStore.setInspec_message(getTaskDesc(taskSize, noExistCount, existDesc));
		taskRunStore.setInspec_message(getTaskDesc(taskSize, noRunCount, runDesc));
		inspectList.add(taskExsitStore);
		inspectList.add(taskRunStore);
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV7Data(CollectData data) {
		int noExistCount = 0;
		int noRunCount = 0;
		String existDesc = "";
		String runDesc = "";
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		int taskSize = context.getInspectTaskSize(inspectDTO.getMonitorId());
		Map<String, String> taskMap = context.getTaskMap(inspectDTO.getMonitorId());
		InspectResultStore taskExsitStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE);
		InspectResultStore taskRunStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE);
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatusQueryR taskQuery = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatusQueryR) report.getReport();
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.TaskStatus status : taskQuery.getTaskStatuss()) {
			String statusDesc = status.getStatus();
			String taskId = status.getTaskID();
//			String desc = status.getDesc();
			if(statusDesc.equals("-1")) {//任务不存在
				noExistCount++;
				existDesc += noExistCount + "." + taskMap.get(taskId) + ",任务状态:站点下该任务可能不存在,";
			}
			
			if(statusDesc.equals("3")) {
				noRunCount++;
				runDesc += noRunCount + "." +taskMap.get(taskId) + ",任务状态:站点下该任务可能从未执行,";
			}
		}
		if(noExistCount != 0)
			taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		
		if(noRunCount != 0)
			taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		taskExsitStore.setInspec_message(getTaskDesc(taskSize, noExistCount, existDesc));
		taskRunStore.setInspec_message(getTaskDesc(taskSize, noRunCount, runDesc));
		inspectList.add(taskExsitStore);
		inspectList.add(taskRunStore);
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV6Data(CollectData data) {
		int noExistCount = 0;
		int noRunCount = 0;
		String existDesc = "";
		String runDesc = "";
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		int taskSize = context.getInspectTaskSize(inspectDTO.getMonitorId());
		Map<String, String> taskMap = context.getTaskMap(inspectDTO.getMonitorId());
		InspectResultStore taskExsitStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE);
		InspectResultStore taskRunStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE);
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskStatusQueryR taskQuery = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskStatusQueryR) report.getReport();
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskStatus status : taskQuery.getTaskStatuss()) {
			String statusDesc = status.getStatus();
			String taskId = status.getTaskID();
//			String desc = status.getDesc();
			if(statusDesc.equals("-1")) {//任务不存在
				noExistCount++;
				existDesc += noExistCount + "." + taskMap.get(taskId) + ",任务状态:站点下该任务可能不存在,";
			}
			
			if(statusDesc.equals("3")) {
				noRunCount++;
				runDesc += noRunCount + "." +taskMap.get(taskId) + ",任务状态:站点下该任务可能从未执行,";
			}
		}
		if(noExistCount != 0)
			taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		
		if(noRunCount != 0)
			taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		taskExsitStore.setInspec_message(getTaskDesc(taskSize, noExistCount, existDesc));
		taskRunStore.setInspec_message(getTaskDesc(taskSize, noRunCount, runDesc));
		inspectList.add(taskExsitStore);
		inspectList.add(taskRunStore);
		return inspectList;
	}
	
	private List<InspectResultStore> disposeV5Data(CollectData data) {
		int noExistCount = 0;
		int noRunCount = 0;
		String existDesc = "";
		String runDesc = "";
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		CollectTask task = data.getCollectTask();
		MoniInspectResultDTO inspectDTO = (MoniInspectResultDTO) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		int taskSize = context.getInspectTaskSize(inspectDTO.getMonitorId());
		Map<String, String> taskMap = context.getTaskMap(inspectDTO.getMonitorId());
		InspectResultStore taskExsitStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE);
		InspectResultStore taskRunStore = getResultDto(inspectDTO, InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE);
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.TaskStatusQueryR taskQuery = (com.neusoft.gbw.cp.conver.v5.protocol.vo.other.TaskStatusQueryR) report.getReport();
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.TaskStatus status : taskQuery.getTaskStatuss()) {
			String statusDesc = status.getStatus();
			String taskId = status.getTaskID();
//			String desc = status.getDesc();
			if(statusDesc.equals("-1")) {//任务不存在
				noExistCount++;
				existDesc += noExistCount + "." + taskMap.get(taskId) + ",任务状态:站点下该任务可能不存在,";
			}
			
			if(statusDesc.equals("3")) {
				noRunCount++;
				runDesc += noRunCount + "." +taskMap.get(taskId) + ",任务状态:站点下该任务可能从未执行,";
			}
		}
		if(noExistCount != 0)
			taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskExsitStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		
		if(noRunCount != 0)
			taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		else taskRunStore.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		taskExsitStore.setInspec_message(getTaskDesc(taskSize, noExistCount, existDesc));
		taskRunStore.setInspec_message(getTaskDesc(taskSize, noRunCount, runDesc));
		inspectList.add(taskExsitStore);
		inspectList.add(taskRunStore);
		return inspectList;
	}
	
	private String getTaskDesc(int taskSize, int noCount, String errorDesc) {
		StringBuffer desc = new StringBuffer();
		desc.append("任务总数：" + taskSize + "个,");
		desc.append("成功总数：" + (taskSize- noCount) + "个,");
		desc.append("失败总数：" +  noCount + "个,");
		desc.append("失败描述：" +  errorDesc);
		return desc.toString();
	}
	
//	private String getInspectKey(MoniInspectResultDTO inspectDTO) {
//		return inspectDTO.getMonitorId() + "_" + inspectDTO.getTimeStamp();
//	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + data.getCollectTask().getBusTask().getMonitor_code());
		return buffer.toString();
	}

}
