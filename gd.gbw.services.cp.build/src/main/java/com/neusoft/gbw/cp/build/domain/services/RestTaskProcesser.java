package com.neusoft.gbw.cp.build.domain.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.collect.AbstractTaskBuilder;
import com.neusoft.gbw.cp.build.domain.conver.ITaskConver;
import com.neusoft.gbw.cp.build.domain.conver.RestTaskConver;
import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.FileRetrieve_File;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.RestType;
import com.neusoft.gbw.domain.exception.NXmlException;
import com.neusoft.gbw.domain.task.mgr.intf.dto.DownLoadFileDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskRecordDataDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;
import com.neusoft.np.arsf.net.core.NetEventType;

public class RestTaskProcesser implements ITaskProcess{

	private ITaskConver taskConver = null;

	public RestTaskProcesser() {
		taskConver = new RestTaskConver();
	}

	@Override
	public void taskProcess(Object obj) {
		String msg = (String) obj;
		Map<String, String> map = null;
		try {
			map = decode(msg);
			String typeID = map.get(NetEventProtocol.SYNT_TYPE_ID);
			RestType type = RestType.valueOf(typeID);
			switch(type) {
			//			case getRealTimeStream:
			//				sendRealStreamProcess(msg);
			//				break;
			//			case getRealTimeRecord:
			//				sendRealRecordProcess(msg);
			//				break;
			case editTaskEvents:
				editTaskProcess(msg);
				break;
			case deleteTaskEvents:
				deleteTaskProcess(msg);
				break;
			case sendTaskEvents:
				setTaskProcess(msg);
				break;
				//			case recoveryTaskEvents:
				//				recoverTaskProcess(msg);
				//				break;
			case dropMonitorClientLinks:
				dropClientTaskProcess(msg);
				break;
			case controlMonitor:
				sendControlMonitorProcess(msg);
				break;
			case saveMonitorKpi:
				sendQualityAlarmSetProcess(msg);
				break;
			case saveMonitorDev:
				sendEquAlarmSetProcess(msg);
				break;
			case saveMonitorBase:
				sendEquInitSetProcess(msg);
				break;
			case recoveryTaskRecordEvents:
				recoverTaskProcess(msg);
				break;
			}
		} catch(ConverException e) {
			Log.error("[构建服务]解析REST接口任务异常" + e.getMessage(), e);
		} catch (BuildException e) {
			Log.error("[构建服务]构建REST接口任务异常", e);
		}
	}

	private Map<String, String> decode(String task) throws ConverException {
		Map<String, String> attrs;
		try {
			attrs = NPJsonUtil.jsonToMap(task);
			return attrs;
		} catch (NMFormateException e) {
			throw new ConverException(e.getMessage());
		}
	}

	//	private void sendRealStreamProcess(String msg) throws ConverException, BuildException {
	//		List<BuildInfo> infoList = taskConver.conver(msg);
	//		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
	//		AbstractTaskBuilder build = null;
	//		CollectTask colTask = null;
	// 		for(BuildInfo info : infoList) {
	//			if (!taskBuildMap.containsKey(info.getType().getKey())) {
	//				Log.warn("");
	//				continue;
	//			}
	//			build = taskBuildMap.get(info.getType().getKey());
	//			colTask = build.buildTask(info);
	//			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
	// 		}
	//	}

	private void sendControlMonitorProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}

	private void editTaskProcess(String msg) throws ConverException, BuildException {
		List<CollectTask> allList = new ArrayList<CollectTask>();
		List<CollectTask> delList = new ArrayList<CollectTask>();
		List<CollectTask> setList = new ArrayList<CollectTask>();
		//		List<Map<String, String>> noCollectSite = new ArrayList<Map<String, String>>();
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("[任务修改]未找到指定的任务构建类型 BuildType=" + info.getType().getKey());
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			//			int type =  info.getDevice().getType_id();
			//			if(type == 2) {
			//				Log.warn("[构建服务]手动任务操作不处理遥控站类型任务 type=" + type);
			//				noCollectSite.add((Map<String, String>)((Task)info.getBuisness()).getExpandObj());
			//				continue;
			//			}
			CollectTask colTask = build.buildTask(info);
			BusinessTaskType busType = info.getType().getBusTaskType();
			switch(busType) {
			case measure_manual_set:
				setList.add(colTask);
				break;
			case measure_manual_del:
				delList.add(colTask);
				break;
			default:
				break;
			}
		}
		allList.addAll(setList);
		allList.addAll(delList);

		if(!allList.isEmpty()) {
			ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, allList);
			for(CollectTask task : delList) {
				sleep(3000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
			for(CollectTask task : setList) {
				sleep(3000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
		}
		infoList.clear();
		infoList = null;
		// 		else {
		// 			//将非采集点任务发送至处理，返回处理消息
		//	 		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, noCollectSite);
		// 		}

	}

	//	private String converMsg(String msg,RestType type) throws ConverException {
	//		String newMsg = null;
	//		Map<String, String> map  = decode(msg);
	//		map.put(NetEventProtocol.SYNT_TYPE_ID, type.name());
	//		newMsg = NPJsonUtil.mapToJson(map);
	//		return newMsg;
	//	}

	private void deleteTaskProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		List<CollectTask> colList = new ArrayList<CollectTask>();
		//		List<Map<String, String>> noCollectSite = new ArrayList<Map<String, String>>();
		AbstractTaskBuilder build = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("[任务修改]未找到指定的任务构建类型 BuildType=" + info.getType().getKey());
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			//			int type =  info.getDevice().getType_id();
			//			if(type == 2) {
			//				Log.warn("[构建服务]手动任务操作不处理遥控站类型任务 type=" + type);
			//				noCollectSite.add((Map<String, String>)((Task)info.getBuisness()).getExpandObj());
			//				continue;
			//			}
			CollectTask colTask = build.buildTask(info);
			colList.add(colTask);
		}
		if(!colList.isEmpty()) {
			//将下发任务发送至实时处理服务
			ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, colList);
			//发送至采集
			for(CollectTask task : colList) {
				sleep(2000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
		}
		infoList.clear();
		infoList = null;
		// 		else {
		// 			//将非采集点任务发送至处理，返回处理消息
		//	 		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, noCollectSite);
		// 		}
	}

	private void setTaskProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		List<CollectTask> colList = new ArrayList<CollectTask>();
		//		List<Map<String, String>> noCollectSite = new ArrayList<Map<String, String>>();
		AbstractTaskBuilder build = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("[构建服务]手动任务操作未找到指定的任务构建类型, BuildType=" + info.getType().getKey());
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			//			int type =  info.getDevice().getType_id();
			//			if(type == 2) {
			//				Log.warn("[构建服务]手动任务操作不处理遥控站类型任务 type=" + type);
			//				noCollectSite.add((Map<String, String>)((Task)info.getBuisness()).getExpandObj());
			//				continue;
			//			}
			CollectTask colTask = build.buildTask(info);
			colList.add(colTask);
		}

		//判断是否是下发的只有遥控站任务
		if(!colList.isEmpty()) {
			//将下发任务发送至实时处理服务
			ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, colList);
			//发送至采集
			for(CollectTask task : colList) {
				sleep(2000);
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
			}
		}
		infoList.clear();
		infoList = null;
		// 		else {
		// 			//将非采集点任务发送至处理，返回处理消息
		//	 		ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_SET_TASK_PROCESS_TOPIC, noCollectSite);
		// 		}
	}
	@SuppressWarnings("unchecked")
	private void recoverTaskProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		List<CollectTask> colList = new ArrayList<CollectTask>();
		AbstractTaskBuilder build = null;

		if(null!=infoList && infoList.size()>0){
			for(BuildInfo info : infoList) {
				if (!taskBuildMap.containsKey(info.getType().getKey())) {
					Log.warn("");
					continue;
				}
				build = taskBuildMap.get(info.getType().getKey());
				CollectTask colTask = build.buildTask(info);
				colList.add(colTask);
				if(!colList.isEmpty()){
					if(infoList.get(0).isUseFtp()){
						Object obj = info.getBuisness();
						Map<String, String> map = (HashMap<String, String>)obj;
						String request = map.get(NetEventProtocol.SYNT_REQUEST);
						DownLoadFileDTO dto =null;
						try {
							dto = (DownLoadFileDTO)ConverterUtil.xmlToObj(request);
						} catch (NXmlException e) {
							Log.debug(e);
						}
						int i = 0;
						List<TaskRecordDataDTO> taskList = dto.getTaskRecordList();
						for(TaskRecordDataDTO taskDTO: taskList) {
							boolean b = BusinessUtils.copyZip(info.getDevice().getFtp_path(),taskDTO.getFilename(),colTask);
							if(!b){
								i++;
							}
						}
						Object syntStr = null;
						if(taskList.size()==i){  //全部回收失败
							RecoveryMessageDTO recover = new RecoveryMessageDTO();
							recover.setSuccessType("1");
							recover.setSuccessDicDesc("数据采集失败");
							syntStr = getRestStr(recover, colTask);
							Log.debug("本批次录音文件接收完成,向前台发送回收消息，syntStr=" + syntStr);
						}else{ //部分回收成功
							RecoveryMessageDTO recover = new RecoveryMessageDTO();
							recover.setSuccessType("0");
							recover.setSuccessDicDesc("数据采集成功");
							syntStr = getRestStr(recover, colTask);
							Log.debug("本批次录音文件接收完成,向前台发送回收消息，syntStr=" + syntStr);
						}
						ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), (String)syntStr);
					}else{
						//将下发任务发送至实时处理服务
						ARSFToolkit.sendEvent(EventServiceTopic.MANUAL_RECOVER_RECORD_FILE_PROCESS_TOPIC, colList);
						//发送至采集
						for(CollectTask task : colList) 
							ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, task);
					}
				}
			}
		}
		infoList.clear();
		infoList = null;
	}
	@SuppressWarnings("unchecked")
	private String getRestStr(RecoveryMessageDTO recover, CollectTask task) {
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String tmpStr = ConverterUtil.objToXml(recover);
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
		return NPJsonUtil.jsonValueString(syntMap);
	}
	private void dropClientTaskProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}

	private void sendQualityAlarmSetProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}

	private void sendEquAlarmSetProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}
	//设备初始化设置
	private void sendEquInitSetProcess(String msg) throws ConverException, BuildException {
		List<BuildInfo> infoList = taskConver.conver(msg);
		Map<String, AbstractTaskBuilder> taskBuildMap = BuildContext.getInstance().getTaskBuildMap();
		AbstractTaskBuilder build = null;
		CollectTask colTask = null;
		for(BuildInfo info : infoList) {
			if (!taskBuildMap.containsKey(info.getType().getKey())) {
				Log.warn("");
				continue;
			}
			build = taskBuildMap.get(info.getType().getKey());
			colTask = build.buildTask(info);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, colTask);
		}
		infoList.clear();
		infoList = null;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Log.debug(this.getClass().getName()+" 线程休眠中断",e);
		}
	}

}
